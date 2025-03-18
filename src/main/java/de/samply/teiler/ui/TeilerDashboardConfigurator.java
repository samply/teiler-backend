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

@Component
public class TeilerDashboardConfigurator {

    private final static Logger logger = LoggerFactory.getLogger(TeilerDashboardConfigurator.class);
    private final String defaultLanguage;
    private final Map<String, String> variables = new HashMap<>();
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
            this.variables.put(key, value);
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

    public Optional<String> fetchTeilerDashboardVariable(String key) {
        if (StringUtils.isEmpty(key)) {
            return Optional.empty();
        }

        key = key.trim();
        if (!TeilerDashboardUtils.isTeilerDashboard(key)) {
            return Optional.empty();
        }

        // Try direct lookup
        String result = variables.get(key);
        if (result != null) {
            return Optional.of(result);
        }

        // Try looking for language-specific key
        Optional<String> language = fetchLanguage(key);
        if (language.isPresent()) {
            String languageKey = removeLanguage(key);
            result = variables.get(languageKey);
            if (result != null) {
                return Optional.of(result);
            }

            // Try default language fallback
            if (!language.get().equalsIgnoreCase(defaultLanguage)) {
                String defaultLangKey = addDefaultLanguage(key);
                result = variables.get(defaultLangKey);
                if (result != null) {
                    return Optional.of(result);
                }
            }
        } else {
            // No language found, try adding default language
            String defaultLangKey = addDefaultLanguage(key);
            result = variables.get(defaultLangKey);
            if (result != null) {
                return Optional.of(result);
            }
        }

        return Optional.empty();
    }

    private Optional<String> fetchLanguage(String key) {
        String[] parts = key.split("_");

        // Ensure at least 3 parts exist: PREFIX + LANGUAGE + SUFFIX
        if (parts.length < 3) {
            return Optional.empty(); // No language present
        }

        // The language is always the second-last element
        String language = parts[parts.length - 2];

        return Optional.of(language);
    }

    private String addDefaultLanguage(String key) {
        List<String> elements = new ArrayList<>(Arrays.asList(key.split("_")));

        // Ensure at least PREFIX + SUFFIX exists
        if (elements.size() < 2) {
            return key;
        }

        // Insert default language before the last element (i.e., before SUFFIX)
        elements.add(elements.size() - 1, defaultLanguage);
        return String.join("_", elements);
    }

    private String removeLanguage(String key) {
        List<String> elements = new ArrayList<>(Arrays.asList(key.split("_")));

        // Ensure at least PREFIX + LANGUAGE + SUFFIX exists
        if (elements.size() < 3) {
            return key;
        }

        // Remove the second-last element (assumed to be the language)
        elements.remove(elements.size() - 2);
        return String.join("_", elements);
    }

}
