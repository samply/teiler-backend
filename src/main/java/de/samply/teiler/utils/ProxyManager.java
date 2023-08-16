package de.samply.teiler.utils;

import de.samply.teiler.backend.TeilerBackendConst;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.Optional;

@Component
public class ProxyManager {

    private Proxy httpProxy;
    private Proxy httpsProxy;

    public ProxyManager(@Value(TeilerBackendConst.HTTP_PROXY_SV) String httpProxy,
                        @Value(TeilerBackendConst.HTTPS_PROXY_SV) String httpsProxy) {
        Optional<Proxy> proxy = fetchProxyInfo(httpProxy);
        if (proxy.isPresent()){
            this.httpProxy = proxy.get();
        }
        proxy = fetchProxyInfo(httpsProxy);
        if (proxy.isPresent()){
            this.httpsProxy = proxy.get();
        }
    }

    private Optional<Proxy> fetchProxyInfo(String httpProxy) {
        String proxyHost = null;
        Integer proxyPort = null;
        if (httpProxy != null && !httpProxy.isEmpty()) {
            int index = httpProxy.indexOf("://");
            if (index > 0) {
                httpProxy = httpProxy.substring(index + "://".length());
            }
            String[] httpProxySplitted = httpProxy.split(":");
            if (httpProxySplitted.length > 1 && !httpProxySplitted[1].isEmpty() && NumberUtils.isCreatable(httpProxySplitted[1])) {
                proxyPort = NumberUtils.createInteger(httpProxySplitted[1]);
            }
            if (httpProxySplitted.length > 0 && !httpProxySplitted[0].isEmpty()) {
                proxyHost = httpProxySplitted[0];
            }
        }
        return (proxyHost != null && proxyPort != null) ?
                Optional.of(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort))) :
                Optional.empty();
    }

    public HttpURLConnection openConnection(@NonNull String url) throws IOException {
        Proxy proxy = Proxy.NO_PROXY;
        if (url.contains("https") && httpsProxy != null){
            proxy = httpsProxy;
        } else if (httpProxy != null){
            proxy = httpProxy;
        }
        return (HttpURLConnection) new URL(url).openConnection(proxy);
    }

}
