package de.samply.teiler.utils;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class EnvironmentUtils {

    public static void addKeyValuesFromEnvironment(AbstractEnvironment environment, Function<String, Boolean> isKeyToBeAdded, BiConsumer<String, String> addKeyValue) {
        addKeyValuesFromEnvironment(environment.getSystemEnvironment(), isKeyToBeAdded, addKeyValue);
        addKeyValuesFromEnvironment(environment.getSystemProperties(), isKeyToBeAdded, addKeyValue);
        addKeyValuesFromEnvironmentPropertySources(environment, isKeyToBeAdded, addKeyValue);
    }

    private static void addKeyValuesFromEnvironment(Map<String, Object> keyValues, Function<String, Boolean> isKeyToBeAdded, BiConsumer<String, String> addKeyValue) {
        keyValues.keySet().stream().filter(isKeyToBeAdded::apply).forEach(key -> addKeyValue.accept(key, (String) keyValues.get(key)));
    }

    private static void addKeyValuesFromEnvironmentPropertySources(AbstractEnvironment environment, Function<String, Boolean> isKeyToBeAdded, BiConsumer<String, String> addKeyValue) {
        environment.getPropertySources().stream().filter(p -> p instanceof EnumerablePropertySource)
                .map(p -> ((EnumerablePropertySource) p).getPropertyNames()).flatMap(Arrays::stream).distinct()
                .filter(isKeyToBeAdded::apply).forEach(key -> addKeyValue.accept(key, environment.getProperty(key)));
    }

}
