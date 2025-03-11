package de.samply.teiler.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EnvironmentUtils {

    private final Logger logger = LoggerFactory.getLogger(EnvironmentUtils.class);
    private final AbstractEnvironment environment;

    public EnvironmentUtils(Environment environment) {
        this.environment = (AbstractEnvironment) environment;
    }

    public void addKeyValuesFromEnvironment(Function<String, Boolean> isKeyToBeAdded, BiConsumer<String, String> addKeyValue) {
        logger.info("Reading environment variables...");

        Map<String, String> collectedKeyValues = new LinkedHashMap<>();

        // Collect variables from different sources
        // Property Sources (application.properties, application.yml, etc.)
        collectedKeyValues.putAll(collectKeyValuesFromPropertySources(isKeyToBeAdded));
        // System Properties (-Dproperty=value when running the application)
        collectedKeyValues.putAll(collectKeyValues(environment.getSystemProperties(), isKeyToBeAdded));
        // System Environment Variables
        collectedKeyValues.putAll(collectKeyValues(environment.getSystemEnvironment(), isKeyToBeAdded));


        logger.info("Processing collected environment variables...");

        // Process each key-value pair only once
        collectedKeyValues.forEach(addKeyValue);

        logger.info("Environment variable processing finished.");
    }

    private Map<String, String> collectKeyValues(Map<String, Object> keyValues, Function<String, Boolean> isKeyToBeAdded) {
        return keyValues.entrySet().stream()
                .filter(entry -> isKeyToBeAdded.apply(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue()), (v1, v2) -> v1, LinkedHashMap::new));
    }

    private Map<String, String> collectKeyValuesFromPropertySources(Function<String, Boolean> isKeyToBeAdded) {
        Map<String, String> keyValues = new LinkedHashMap<>();

        environment.getPropertySources().stream()
                .filter(p -> p instanceof EnumerablePropertySource)
                .map(p -> (EnumerablePropertySource<?>) p)
                .map(EnumerablePropertySource::getPropertyNames)
                .flatMap(Arrays::stream)
                .distinct()
                .filter(isKeyToBeAdded::apply)
                .forEach(key -> keyValues.putIfAbsent(key, environment.getProperty(key)));
        return keyValues;
    }

}
