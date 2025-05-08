package de.samply.teiler.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PingSession {

    private Map<String, Boolean> urlReachableMap = new HashMap<>();

    public Optional<Boolean> isUrlReachable(String url) {
        return Optional.ofNullable(urlReachableMap.get(url));
    }

    public void addUrlReachable (String url, boolean isReachable){
        urlReachableMap.put(url, isReachable);
    }

}
