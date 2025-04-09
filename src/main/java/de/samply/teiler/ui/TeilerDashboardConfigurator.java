package de.samply.teiler.ui;

import de.samply.teiler.backend.TeilerBackendConst;
import de.samply.teiler.utils.EnvironmentUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TeilerDashboardConfigurator {

    private final static Logger logger = LoggerFactory.getLogger(TeilerDashboardConfigurator.class);
    private final String defaultLanguage;
    private final Map<String, String> dashboardVariables = new HashMap<>();
    private final Map<String, String> languageUrlMap = new HashMap<>();
    private final Set<String> languages;
    private final Map<String, Map<String,String>> languageDashboardVariablesCache = new HashMap<>();

    public TeilerDashboardConfigurator(@Value(TeilerBackendConst.DEFAULT_LANGUAGE_SV) String defaultLanguage,
                                       EnvironmentUtils environmentUtils) {
        this.defaultLanguage = defaultLanguage;
        this.languages = fetchLanguages();
        initializeLanguageUrlMap(environmentUtils);
        initializeDashboardVariables(environmentUtils);
    }

    private Set<String> fetchLanguages() {
        return java.util.Arrays.stream(Locale.getAvailableLocales())
                .map(Locale::getLanguage)
                .filter(lang -> !lang.isEmpty())
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
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
                    key.trim().substring((TeilerBackendConst.TEILER_DASHBOARD_PREFIX + "_").length()).toUpperCase(),
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
        language = language.toUpperCase();
        key = key.trim().toUpperCase();

        String languageAndKey = language + "_" + key;

        String result = dashboardVariables.get(languageAndKey);
        if (result != null) {
            return Optional.of(result);
        }
        result = dashboardVariables.get(key);
        return Optional.ofNullable(result);
    }

    public Map<String, String> fetchDashboardVariables(String language) {
        Map<String, String> result = new HashMap<>();
        if (StringUtils.isEmpty(language)) {
            language = defaultLanguage;
        }
        String finalLanguage = language.trim().toUpperCase();
        Map<String, String> cachedResult = languageDashboardVariablesCache.get(finalLanguage);
        if (cachedResult != null) {
            return cachedResult;
        }
        dashboardVariables.keySet().stream()
                .filter(key -> isVariableLanguage(key, finalLanguage))
                .forEach(key -> result.put(removeLanguageFromVariable(key), dashboardVariables.get(key)));
        languageDashboardVariablesCache.put(finalLanguage, result);
        return result;
    }

    private boolean isVariableLanguage(String variable, String language) {
        String[] elements = variable.split("_");
        if (elements.length > 1) {
            // If it is the same language
            if (elements[0].equals(language)) {
                return true;
            }
            // If the first element is not a language:
            // There is no language specified for the variable, so it is valid for all languages
            return !(languages.contains(elements[0]));
        }
        // If there is not a specific language in the variable, it is considered for all languages
        return true;
    }

    private String removeLanguageFromVariable(String variable){
        ArrayList<String> elements = new ArrayList<>(Arrays.asList(variable.split("_")));
        if (elements.size() > 1 && languages.contains(elements.get(0))) {
            elements.remove(0);
            return String.join("_", elements);
        }
        return variable;
    }

}
