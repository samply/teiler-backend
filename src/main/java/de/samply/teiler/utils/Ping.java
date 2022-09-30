package de.samply.teiler.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Ping {

    private final static Logger logger = LoggerFactory.getLogger(Ping.class);
    private static boolean followRedirects = true;

    public static boolean ping(String url) {
        return (url != null) ? openConnectionAndPing(url) : false;
    }

    private static boolean openConnectionAndPing(String url) {
        try (CloseableHttpUrlConnection httpUrlConnection = new CloseableHttpUrlConnection(url)){
            return ping (httpUrlConnection);
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean ping(CloseableHttpUrlConnection closeableHttpUrlConnection) throws IOException {
        return closeableHttpUrlConnection.getHttpUrlConnection().getResponseCode() == HttpURLConnection.HTTP_OK;
    }

    public static void setFollowRedirects(boolean followRedirects) {
        Ping.followRedirects = followRedirects;
    }

    private static class CloseableHttpUrlConnection implements Closeable {

        private HttpURLConnection httpUrlConnection;

        public CloseableHttpUrlConnection(String url) throws IOException {
            httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
            httpUrlConnection.setInstanceFollowRedirects(followRedirects);
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
