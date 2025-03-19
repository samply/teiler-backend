package de.samply.teiler.ui;

import de.samply.teiler.backend.TeilerBackendConst;
import de.samply.teiler.utils.EnvironmentUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class TeilerDashboardConfigurator {

    private final static Logger logger = LoggerFactory.getLogger(TeilerDashboardConfigurator.class);
    private final String defaultLanguage;
    private final Map<String, String> dashboardVariables = new HashMap<>();
    private final Map<String, String> languageUrlMap = new HashMap<>();

    public TeilerDashboardConfigurator(@Value(TeilerBackendConst.DEFAULT_LANGUAGE_SV) String defaultLanguage,
                                       EnvironmentUtils environmentUtils) {
        this.defaultLanguage = defaultLanguage;
        initializeLanguageUrlMap(environmentUtils);
        initializeDashboardVariables(environmentUtils);
    }

    private void initializeLanguageUrlMap(EnvironmentUtils environmentUtils) {
        logger.info("Initialize Teiler UI config...");
        environmentUtils.addKeyValuesFromEnvironment(TeilerDashboardUtils::isTeilerDashboard, this::addKeyValue);
    }

    private void initializeDashboardVariables(EnvironmentUtils environmentUtils) {
        logger.info("Initialize Dashboard Variables...");
        environmentUtils.addKeyValuesFromEnvironment(TeilerDashboardUtils::isTeilerDashboard, this::addDashboardVariable);
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

    private void addDashboardVariable(String key, String value) {
        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
            // The prefix is removed
            this.dashboardVariables.put(
                    key.trim().substring((TeilerBackendConst.TEILER_DASHBOARD_PREFIX + "_").length()).toLowerCase(),
                    value);
        }
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

    public Optional<String> fetchTeilerDashboardVariable(String key, String language) {
        if (StringUtils.isEmpty(key)) {
            return Optional.empty();
        }
        if (StringUtils.isEmpty(language)) {
            language = defaultLanguage;
        }
        language = language.toLowerCase();
        key = key.trim().toLowerCase();

        String languageAndKey = language + "_" + key;

        String result = dashboardVariables.get(languageAndKey);
        if (result != null) {
            return Optional.of(result);
        }
        result = dashboardVariables.get(key);
        return Optional.ofNullable(result);
    }

}
