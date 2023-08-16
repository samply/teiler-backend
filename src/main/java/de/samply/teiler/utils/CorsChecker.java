package de.samply.teiler.utils;

import de.samply.teiler.backend.TeilerBackendConst;
import de.samply.teiler.ui.TeilerUiConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CorsChecker {

    Set<String> allowedOriginUrls = new HashSet<>();

    public CorsChecker(@Value(TeilerBackendConst.TEILER_ROOT_CONFIG_URL_SV) String teilerRootConfigUrl,
                       @Autowired TeilerUiConfigurator teilerUiConfigurator) {

        if (teilerRootConfigUrl != null) {
            allowedOriginUrls.add(teilerRootConfigUrl);
        }

        Arrays.stream(teilerUiConfigurator.getTeilerUiLanguages())
                .forEach(language -> allowedOriginUrls.add(teilerUiConfigurator.getTeilerUiUrl(language)));

    }

    public boolean isOriginUrlAllowed(String originUrl){
        return allowedOriginUrls.contains(originUrl);
    }

}
