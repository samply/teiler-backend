package de.samply.teiler.utils;

import de.samply.teiler.backend.TeilerBackendConst;
import de.samply.teiler.ui.TeilerDashboardConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CorsChecker {

    Set<String> allowedOriginUrls = new HashSet<>();

    public CorsChecker(@Value(TeilerBackendConst.TEILER_ORCHESTRATOR_URL_SV) String teilerOrchestratorUrl,
                       @Autowired TeilerDashboardConfigurator teilerDashboardConfigurator) {

        if (teilerOrchestratorUrl != null) {
            allowedOriginUrls.add(teilerOrchestratorUrl);
        }

        Arrays.stream(teilerDashboardConfigurator.getTeilerDashboardLanguages())
                .forEach(language -> allowedOriginUrls.add(teilerDashboardConfigurator.getTeilerDashboardUrl(language)));

    }

    public boolean isOriginUrlAllowed(String originUrl){
        return allowedOriginUrls.contains(originUrl);
    }

}
