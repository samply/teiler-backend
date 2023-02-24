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
import java.net.URL;

@Component
public class Ping {

    private final Logger logger = LoggerFactory.getLogger(Ping.class);
    private boolean followRedirects = true;
    private int connectTimeoutInSeconds;
    private int readTimeoutInSeconds;

    public Ping(
            @Value(TeilerCoreConst.PING_CONNECTION_TIMEOUT_IN_SECONDS_SV) int connectTimeoutInSeconds,
            @Value(TeilerCoreConst.PING_READ_TIMEOUT_IN_SECONDS_SV) int readTimeoutInSeconds) {
        this.connectTimeoutInSeconds = connectTimeoutInSeconds;
        this.readTimeoutInSeconds = readTimeoutInSeconds;
    }

    public void updatePing(TeilerApp teilerApp) {
        updatePingFrontendUrl(teilerApp);
        updatePingBackendUrl(teilerApp);
    }

    private void updatePingFrontendUrl(TeilerApp teilerApp) {
        if (teilerApp.getSourceUrl() != null) {
            String frontendUrl = (teilerApp.getSingleSpaMainJs() != null) ?
                    UriComponentsBuilder
                            .fromHttpUrl(teilerApp.getSourceUrl())
                            .path('/' + teilerApp.getSingleSpaMainJs()).toUriString()
                    : teilerApp.getSourceUrl();

            teilerApp.setFrontendReachable(ping(frontendUrl));
        }
    }

    private void updatePingBackendUrl(TeilerApp teilerApp) {
        if (teilerApp.getBackendUrl() != null) {
            teilerApp.setBackendReachable(ping(teilerApp.getBackendUrl()));
        }
    }


    public boolean ping(String url) {
        return (url != null) ? openConnectionAndPing(url) : false;
    }

    private boolean openConnectionAndPing(String url) {
        try (CloseableHttpUrlConnection httpUrlConnection = new CloseableHttpUrlConnection(url)){
            logger.info("Ping to "+ url + "...");
            return ping (httpUrlConnection);
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
            httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
            httpUrlConnection.setInstanceFollowRedirects(followRedirects);
            httpUrlConnection.setConnectTimeout(connectTimeoutInSeconds*1000);
            httpUrlConnection.setReadTimeout(readTimeoutInSeconds*1000);
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
