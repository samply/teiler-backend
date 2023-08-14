package de.samply.teiler.utils;

import de.samply.teiler.app.TeilerApp;
import de.samply.teiler.core.TeilerCoreConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;

@Component
public class Ping {

    private final Logger logger = LoggerFactory.getLogger(Ping.class);
    private boolean followRedirects = true;
    private int connectTimeoutInSeconds;
    private int readTimeoutInSeconds;
    private ProxyManager proxyManager;

    public Ping(
            ProxyManager proxyManager,
            @Value(TeilerCoreConst.PING_CONNECTION_TIMEOUT_IN_SECONDS_SV) int connectTimeoutInSeconds,
            @Value(TeilerCoreConst.PING_READ_TIMEOUT_IN_SECONDS_SV) int readTimeoutInSeconds) {
        this.proxyManager = proxyManager;
        this.connectTimeoutInSeconds = connectTimeoutInSeconds;
        this.readTimeoutInSeconds = readTimeoutInSeconds;
    }

    public void updatePing(TeilerApp teilerApp) {
        updatePingFrontendUrl(teilerApp);
        updatePingBackendUrl(teilerApp);
    }

    private void updatePingFrontendUrl(TeilerApp teilerApp) {
        String frontendUrl = (teilerApp.getSourceCheckUrl() != null) ? teilerApp.getSourceCheckUrl() : teilerApp.getSourceUrl();
        if (frontendUrl != null) {
            if (teilerApp.getSingleSpaMainJs() != null) {
                frontendUrl = UriComponentsBuilder.fromHttpUrl(frontendUrl).path('/' + teilerApp.getSingleSpaMainJs()).toUriString();
            }
            teilerApp.setFrontendReachable(ping(frontendUrl));
        }
    }

    private void updatePingBackendUrl(TeilerApp teilerApp) {
        String backendUrl = (teilerApp.getBackendCheckUrl() != null) ? teilerApp.getBackendCheckUrl() : teilerApp.getBackendUrl();
        if (backendUrl != null) {
            teilerApp.setBackendReachable(ping(backendUrl));
        }
    }


    public boolean ping(String url) {
        return (url != null && !url.isEmpty()) ? openConnectionAndPing(url) : false;
    }

    private boolean openConnectionAndPing(String url) {
        try (CloseableHttpUrlConnection httpUrlConnection = new CloseableHttpUrlConnection(url)) {
            logger.info("Ping to " + url + "...");
            return ping(httpUrlConnection);
        } catch (IOException e) {
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
