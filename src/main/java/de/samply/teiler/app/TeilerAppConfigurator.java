package de.samply.teiler.app;

import de.samply.teiler.core.TeilerCoreConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TeilerAppConfigurator {

    private String defaultLanguage;
    private AbstractEnvironment environment;

    public TeilerAppConfigurator(@Value(TeilerCoreConst.DEFAULT_LANGUAGE_SV) String defaultLanguage, @Autowired Environment environment) {
        this.defaultLanguage = defaultLanguage;
        this.environment = (AbstractEnvironment) environment;

        initializeLanguageTeilerAppMap();
    }

    private Map<String, Map<Integer, TeilerApp>> languageAppIdTeilerAppMap = new HashMap<>();


    private void initializeLanguageTeilerAppMap() {
        initializeLanguageTeilerAppMap(environment.getSystemEnvironment());
        initializeLanguageTeilerAppMap(environment.getSystemProperties());
        environment.getPropertySources().stream().filter(p -> p instanceof EnumerablePropertySource)
                .map(p -> ((EnumerablePropertySource) p).getPropertyNames()).flatMap(Arrays::stream).distinct()
                .filter(this::isTeilerApp).forEach(key -> addKeyValue(key, environment.getProperty(key)));
        //System.getenv().keySet().stream().filter(this::isAppEnvVar).forEach(envVar -> addEnvVar(envVar, System.getenv(envVar)));
    }

    private void initializeLanguageTeilerAppMap(Map<String, Object> keyValues) {
        keyValues.keySet().stream().filter(this::isTeilerApp).forEach(key -> addKeyValue(key, (String) keyValues.get(key)));
    }

    private boolean isTeilerApp(String envVar) {
        return envVar.startsWith(TeilerAppUtils.APP_PREFIX);
    }

    private void addKeyValue(String envVar, String value) {

        Integer appId = TeilerAppUtils.getAppId(envVar);
        String language = TeilerAppUtils.getLanguage(envVar);
        TeilerApp teilerApp = getTeilerApp(appId, language);

        TeilerAppUtils.addKeyValue(envVar, value, teilerApp);

    }


    private TeilerApp getTeilerApp(Integer appId, String language) {

        if (language == null) {
            language = defaultLanguage;
        }
        Map<Integer, TeilerApp> appIdTeilerAppMap = languageAppIdTeilerAppMap.get(language);
        if (appIdTeilerAppMap == null) {
            appIdTeilerAppMap = new HashMap<>();
            languageAppIdTeilerAppMap.put(language, appIdTeilerAppMap);
        }
        TeilerApp teilerApp = appIdTeilerAppMap.get(appId);
        if (teilerApp == null) {
            teilerApp = new TeilerApp();
            appIdTeilerAppMap.put(appId, teilerApp);
        }

        return teilerApp;

    }

    public Collection<TeilerApp> getTeilerApps(String language) {
        Map<Integer, TeilerApp> appIdTeilerAppMap = languageAppIdTeilerAppMap.get(language);
        return (appIdTeilerAppMap != null) ? appIdTeilerAppMap.values() : new ArrayList<>();
    }

}
