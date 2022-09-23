package de.samply.teiler.app;

import de.samply.teiler.core.TeilerCoreConst;
import de.samply.teiler.ui.TeilerUiConfigurator;
import de.samply.teiler.utils.EnvironmentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class TeilerAppConfigurator {

    private final String defaultLanguage;
    private final String[] teilerUiLanguages;
    private final String projectOrganisation;
    private Map<String, Map<Integer, TeilerApp>> languageAppIdTeilerAppMap = new HashMap<>();

    public TeilerAppConfigurator(@Value(TeilerCoreConst.DEFAULT_LANGUAGE_SV) String defaultLanguage,
                                 @Value(TeilerCoreConst.PROJECT_ORGANISATION_SV) String projectOrganisation,
                                 @Autowired TeilerUiConfigurator teilerUiConfigurator,
                                 @Autowired Environment environment) {
        this.defaultLanguage = defaultLanguage.toLowerCase();
        this.teilerUiLanguages = teilerUiConfigurator.getTeilerUiLanguages();
        this.projectOrganisation = projectOrganisation;

        initializeLanguageTeilerAppMap(environment);
        expandNoLanguageValues();
        expandTeilerAppsToTeilerUiLanguages();
        addAutomaticGeneratedValues();
    }

    private void initializeLanguageTeilerAppMap(Environment environment) {
        EnvironmentUtils.addKeyValuesFromEnvironment((AbstractEnvironment) environment, TeilerAppUtils::isTeilerApp, this::addKeyValue);
    }

    private void addKeyValue(String key, String value) {

        Integer appId = TeilerAppUtils.getAppId(key);
        String language = TeilerAppUtils.getLanguage(key);
        TeilerApp teilerApp = getTeilerApp(appId, language);

        TeilerAppUtils.addKeyValue(key, value, teilerApp);

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

    private void expandTeilerAppsToTeilerUiLanguages() {
        List<Integer> definedAppIds = languageAppIdTeilerAppMap.values().stream().map(appIdTeilerAppMap ->
                appIdTeilerAppMap.keySet()).flatMap(Collection::stream).toList();


        Arrays.stream(teilerUiLanguages).forEach(language -> {
            Map<Integer, TeilerApp> appIdTeilerAppMap = languageAppIdTeilerAppMap.get(language);
            definedAppIds.stream()
                    .filter(appId -> !appIdTeilerAppMap.keySet().contains(appId))
                    .forEach(appId -> appIdTeilerAppMap.put(appId, getDefaultTeilerApp(appId).clone()));
        });

    }

    private TeilerApp getDefaultTeilerApp(Integer appId) {
        TeilerApp teilerApp = languageAppIdTeilerAppMap.get(defaultLanguage).get(appId);
        if (teilerApp == null) {
            AtomicReference<TeilerApp> teilerAppAtomicReference = new AtomicReference<>();
            languageAppIdTeilerAppMap.values().stream()
                    .map(appIdTeilerAppMap -> appIdTeilerAppMap.get(appId))
                    .filter(teilerAppTemp -> teilerAppTemp != null)
                    .forEach(teilerAppTemp -> teilerAppAtomicReference.set(teilerAppTemp));
            teilerApp = teilerAppAtomicReference.get();
        }

        return teilerApp;
    }

    private void addAutomaticGeneratedValues() {
        languageAppIdTeilerAppMap.keySet().forEach(language -> {
            languageAppIdTeilerAppMap.get(language).values().forEach(teilerApp -> {
                teilerApp.setRouterLink(language.toLowerCase() + '/' + teilerApp.getName());
                teilerApp.setSingleSpaLink('@' + projectOrganisation + '/' + teilerApp.getRouterLink());
                if (teilerApp.getExternLink() == null){
                    teilerApp.setExternLink(TeilerCoreConst.IS_EXTERNAL_LINK_DEFAULT);
                }
                if (teilerApp.getActivated() == null){
                    teilerApp.setActivated(TeilerCoreConst.IS_ACTIVATED_DEFAULT);
                }
            });
        });

    }

    public Collection<TeilerApp> getTeilerApps(String language) {
        Map<Integer, TeilerApp> appIdTeilerAppMap = languageAppIdTeilerAppMap.get(language.toLowerCase());
        return (appIdTeilerAppMap != null) ? appIdTeilerAppMap.values() : new ArrayList<>();
    }

}
