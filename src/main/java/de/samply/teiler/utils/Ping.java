package de.samply.teiler.utils;

import de.samply.teiler.app.TeilerApp;
import de.samply.teiler.backend.TeilerBackendConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.function.Consumer;

@Component
public class Ping {

    private final Logger logger = LoggerFactory.getLogger(Ping.class);
    private boolean followRedirects = true;
    private int connectTimeoutInSeconds;
    private int readTimeoutInSeconds;
    private ProxyManager proxyManager;

    public Ping(
            ProxyManager proxyManager,
            @Value(TeilerBackendConst.PING_CONNECTION_TIMEOUT_IN_SECONDS_SV) int connectTimeoutInSeconds,
            @Value(TeilerBackendConst.PING_READ_TIMEOUT_IN_SECONDS_SV) int readTimeoutInSeconds) {
        this.proxyManager = proxyManager;
        this.connectTimeoutInSeconds = connectTimeoutInSeconds;
        this.readTimeoutInSeconds = readTimeoutInSeconds;
    }

    public void updatePing(TeilerApp teilerApp, PingSession pingSession) {
        updatePingFrontendUrl(teilerApp, pingSession);
        updatePingBackendUrl(teilerApp, pingSession);
    }

    private void updatePingFrontendUrl(TeilerApp teilerApp, PingSession pingSession) {
        String frontendUrl = (teilerApp.getSourceCheckUrl() != null) ? teilerApp.getSourceCheckUrl() : teilerApp.getSourceUrl();
        if (frontendUrl != null) {
            if (teilerApp.getSingleSpaMainJs() != null) {
                frontendUrl = UriComponentsBuilder.fromHttpUrl(frontendUrl).path('/' + teilerApp.getSingleSpaMainJs()).toUriString();
            }
            pingUrlAndAddToTeilerApp(frontendUrl, pingSession, teilerApp::setFrontendReachable);
        }
    }

    private void updatePingBackendUrl(TeilerApp teilerApp, PingSession pingSession) {
        String backendUrl = (teilerApp.getBackendCheckUrl() != null) ? teilerApp.getBackendCheckUrl() : teilerApp.getBackendUrl();
        pingUrlAndAddToTeilerApp(backendUrl, pingSession, teilerApp::setBackendReachable);
    }

    private void pingUrlAndAddToTeilerApp(String url, PingSession pingSession, Consumer<Boolean> pingResultConsumer) {
        if (url != null) {
            pingSession.isUrlReachable(url).ifPresentOrElse(
                    urlReachable -> pingResultConsumer.accept(urlReachable),
                    () -> {
                        boolean pingResult = ping(url);
                        pingSession.addUrlReachable(url, pingResult);
                        pingResultConsumer.accept(pingResult);
                    });
        }
    }

    public boolean ping(String url) {
        return (url != null && !url.isEmpty()) ? openConnectionAndPing(url) : false;
    }

    private boolean openConnectionAndPing(String url) {
        try (CloseableHttpUrlConnection httpUrlConnection = new CloseableHttpUrlConnection(url)) {
            logger.info("Ping to " + url + "...");
            boolean result = ping(httpUrlConnection);
            logger.info("[" + ((result) ? "reachable" : "unreachable") + "]");
            return result;
        } catch (IOException e) {
            logger.info("[unreachable]");
            return false;
        }
    }

    private boolean ping(CloseableHttpUrlConnection closeableHttpUrlConnection) throws IOException {
        return closeableHttpUrlConnection.getHttpUrlConnection().getResponseCode() == HttpURLConnection.HTTP_OK;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    private class CloseableHttpUrlConnection implements Closeable {

        private HttpURLConnection httpUrlConnection;

        public CloseableHttpUrlConnection(String url) throws IOException {
            httpUrlConnection = proxyManager.openConnection(url);
            httpUrlConnection.setInstanceFollowRedirects(followRedirects);
            httpUrlConnection.setConnectTimeout(connectTimeoutInSeconds * 1000);
            httpUrlConnection.setReadTimeout(readTimeoutInSeconds * 1000);
        }

        @Override
        public void close() throws IOException {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }

        public HttpURLConnection getHttpUrlConnection() {
            return httpUrlConnection;
        }
    }

}
