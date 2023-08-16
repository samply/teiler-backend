package de.samply.teiler.ui;

import de.samply.teiler.backend.TeilerBackendConst;
import de.samply.teiler.utils.EnvironmentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TeilerUiConfigurator {

    private final static Logger logger = LoggerFactory.getLogger(TeilerUiConfigurator.class);
    private final String defaultLanguage;

    private Map<String, String> languageUrlMap = new HashMap<>();

    public TeilerUiConfigurator(@Value(TeilerBackendConst.DEFAULT_LANGUAGE_SV) String defaultLanguage,
                                @Autowired Environment environment) {
        this.defaultLanguage = defaultLanguage;
        initializeLanguageUrlMap(environment);
    }

    private void initializeLanguageUrlMap(Environment environment) {
        logger.info("Initialize Teiler UI config...");
        EnvironmentUtils.addKeyValuesFromEnvironment((AbstractEnvironment) environment, TeilerUiUtils::isTeilerUi, this::addKeyValue);
    }

    private void addKeyValue(String key, String value) {
        if (TeilerUiUtils.isUrl(key)){
            addUrl(key, value);
        }
    }

    private void addUrl (String key, String value){

        String language = TeilerUiUtils.getLanguage(key);
        if (language == null) {
            language = defaultLanguage;
        }
        languageUrlMap.put(language.toLowerCase(), value);

    }

    public String getTeilerUiUrl() {
        return getTeilerUiUrl(defaultLanguage);
    }

    public String getTeilerUiUrl(String language) {
        return languageUrlMap.get(language.toLowerCase());
    }

    public String[] getTeilerUiUrls() {
        return languageUrlMap.keySet().stream().map(key -> languageUrlMap.get(key)).toArray(String[]::new);
    }

    public String[] getTeilerUiLanguages() {
        return languageUrlMap.keySet().toArray(String[]::new);
    }

}
