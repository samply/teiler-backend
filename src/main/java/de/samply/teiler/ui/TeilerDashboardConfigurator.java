package de.samply.teiler.ui;

import de.samply.teiler.backend.TeilerBackendConst;
import de.samply.teiler.utils.EnvironmentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TeilerDashboardConfigurator {

    private final static Logger logger = LoggerFactory.getLogger(TeilerDashboardConfigurator.class);
    private final String defaultLanguage;

    private Map<String, String> languageUrlMap = new HashMap<>();

    public TeilerDashboardConfigurator(@Value(TeilerBackendConst.DEFAULT_LANGUAGE_SV) String defaultLanguage,
                                       EnvironmentUtils environmentUtils) {
        this.defaultLanguage = defaultLanguage;
        initializeLanguageUrlMap(environmentUtils);
    }

    private void initializeLanguageUrlMap(EnvironmentUtils environmentUtils) {
        logger.info("Initialize Teiler UI config...");
        environmentUtils.addKeyValuesFromEnvironment(TeilerDashboardUtils::isTeilerDashboard, this::addKeyValue);
    }

    private void addKeyValue(String key, String value) {
        if (TeilerDashboardUtils.isUrl(key)) {
            addUrl(key, value);
        }
    }

    private void addUrl(String key, String value) {

        String language = TeilerDashboardUtils.getLanguage(key);
        if (language == null) {
            language = defaultLanguage;
        }
        languageUrlMap.put(language.toLowerCase(), value);

    }

    public String getTeilerDashboardUrl() {
        return getTeilerDashboardUrl(defaultLanguage);
    }

    public String getTeilerDashboardUrl(String language) {
        return languageUrlMap.get(language.toLowerCase());
    }

    public String[] getTeilerDashboardUrls() {
        return languageUrlMap.keySet().stream().map(key -> languageUrlMap.get(key)).toArray(String[]::new);
    }

    public String[] getTeilerDashboardLanguages() {
        return languageUrlMap.keySet().toArray(String[]::new);
    }

}
