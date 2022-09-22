package de.samply.teiler.app;

import de.samply.teiler.core.TeilerCoreConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Component
public class TeilerAppConfigurator {

    private String defaultLanguage;
    private String projectOrganisation;
    private AbstractEnvironment environment;
    private Map<String, Map<Integer, TeilerApp>> languageAppIdTeilerAppMap = new HashMap<>();

    public TeilerAppConfigurator(@Value(TeilerCoreConst.DEFAULT_LANGUAGE_SV) String defaultLanguage,
                                 @Value(TeilerCoreConst.PROJECT_ORGANISATION_SV) String projectOrganisation,
                                 @Autowired Environment environment) {
        this.defaultLanguage = defaultLanguage;
        this.projectOrganisation = projectOrganisation;
        this.environment = (AbstractEnvironment) environment;

        initializeLanguageTeilerAppMap();
        expandNoLanguageValues();
        addAutomativGeneratedValues();
    }


    private void initializeLanguageTeilerAppMap() {
        initializeLanguageTeilerAppMap(environment.getSystemEnvironment());
        initializeLanguageTeilerAppMap(environment.getSystemProperties());
        environment.getPropertySources().stream().filter(p -> p instanceof EnumerablePropertySource)
                .map(p -> ((EnumerablePropertySource) p).getPropertyNames()).flatMap(Arrays::stream).distinct()
                .filter(this::isTeilerApp).forEach(key -> addKeyValue(key, environment.getProperty(key)));
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

    private void expandNoLanguageValues() {
        convert(languageAppIdTeilerAppMap).forEach(this::expandNoLanguageValues);
    }

    private void expandNoLanguageValues(Map<String, TeilerApp> languageTeilerAppsMap) {
        try {
            expandNoLanguageValues_WithoutManagementException(languageTeilerAppsMap);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    private void expandNoLanguageValues_WithoutManagementException(Map<String, TeilerApp> languageTeilerAppsMap) throws IntrospectionException {
        Arrays.stream(Introspector.getBeanInfo(TeilerApp.class).getPropertyDescriptors()).forEach(propertyDescriptor -> {
            TeilerApp defaultLanguageTeilerApp = languageTeilerAppsMap.get(defaultLanguage);
            languageTeilerAppsMap.values().stream()
                    .filter(teilerApp -> teilerApp != defaultLanguageTeilerApp)
                    .forEach(teilerApp -> expandValue(defaultLanguageTeilerApp, teilerApp, propertyDescriptor));
        });
    }

    private void expandValue(TeilerApp originTeilerApp, TeilerApp goalTeilerApp, PropertyDescriptor propertyDescriptor) {
        try {
            expandValue_WithoutManagementException(originTeilerApp, goalTeilerApp, propertyDescriptor);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void expandValue_WithoutManagementException(TeilerApp originTeilerApp, TeilerApp goalTeilerApp, PropertyDescriptor propertyDescriptor) throws InvocationTargetException, IllegalAccessException {
        Object goalTeilerAppField = propertyDescriptor.getReadMethod().invoke(goalTeilerApp);
        if (goalTeilerAppField == null) {
            Object originTeilerAppField = propertyDescriptor.getReadMethod().invoke(originTeilerApp);
            if (originTeilerAppField != null) {
                propertyDescriptor.getWriteMethod().invoke(goalTeilerApp, originTeilerAppField);
            }
        }
    }

    private List<Map<String, TeilerApp>> convert(Map<String, Map<Integer, TeilerApp>> languageAppIdTeilerAppMap) {

        Map<Integer, Map<String, TeilerApp>> appIdLanguageTeilerAppsMaps = new HashMap<>();
        languageAppIdTeilerAppMap.keySet().forEach(language -> {
            Map<Integer, TeilerApp> appIdTeilerAppMap = languageAppIdTeilerAppMap.get(language);
            appIdTeilerAppMap.keySet().forEach(appId ->
                    {
                        Map<String, TeilerApp> languageTeilerAppMap = appIdLanguageTeilerAppsMaps.get(appId);
                        if (languageTeilerAppMap == null) {
                            languageTeilerAppMap = new HashMap<>();
                            appIdLanguageTeilerAppsMaps.put(appId, languageTeilerAppMap);
                        }
                        languageTeilerAppMap.put(language, appIdTeilerAppMap.get(appId));
                    }
            );
        });

        return appIdLanguageTeilerAppsMaps.values().stream().toList();

    }

    private void addAutomativGeneratedValues() {
        languageAppIdTeilerAppMap.keySet().forEach(language -> {
            languageAppIdTeilerAppMap.get(language).values().forEach(teilerApp -> {
                teilerApp.setRouterLink(language.toLowerCase() + '/' + teilerApp.getName());
                teilerApp.setSingleSpaLink('@' + projectOrganisation + '/' + teilerApp.getRouterLink());
            });
        });

    }

    public Collection<TeilerApp> getTeilerApps(String language) {
        Map<Integer, TeilerApp> appIdTeilerAppMap = languageAppIdTeilerAppMap.get(language);
        return (appIdTeilerAppMap != null) ? appIdTeilerAppMap.values() : new ArrayList<>();
    }

}
