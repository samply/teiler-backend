package de.samply.teiler.app;

import de.samply.teiler.backend.TeilerBackendConst;
import de.samply.teiler.singlespa.SingleSpaLinkGenerator;
import de.samply.teiler.ui.TeilerDashboardConfigurator;
import de.samply.teiler.utils.EnvironmentUtils;
import de.samply.teiler.utils.Ping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class TeilerAppConfigurator {

    private final static Logger logger = LoggerFactory.getLogger(TeilerAppConfigurator.class);
    private final String defaultLanguage;

    private final String teilerOrchestratorHttpRelativePath;
    private final String[] teilerDashboardLanguages;
    private SingleSpaLinkGenerator singleSpaLinkGenerator;
    private Map<String, Map<Integer, TeilerApp>> languageAppIdTeilerAppMap = new HashMap<>();
    private Ping ping;

    public TeilerAppConfigurator(@Value(TeilerBackendConst.DEFAULT_LANGUAGE_SV) String defaultLanguage,
                                 @Value(TeilerBackendConst.TEILER_ORCHESTRATOR_HTTP_RELATIVE_PATH_SV) String teilerOrchestratorHttpRelativePath,
                                 @Autowired SingleSpaLinkGenerator singleSpaLinkGenerator,
                                 @Autowired TeilerDashboardConfigurator teilerDashboardConfigurator,
                                 @Autowired Environment environment,
                                 @Autowired Ping ping) {
        this.defaultLanguage = defaultLanguage.toLowerCase();
        this.teilerOrchestratorHttpRelativePath = fetchTeilerOrchestratorHttpRelativePath(teilerOrchestratorHttpRelativePath);
        this.teilerDashboardLanguages = teilerDashboardConfigurator.getTeilerDashboardLanguages();
        this.singleSpaLinkGenerator = singleSpaLinkGenerator;
        this.ping = ping;

        logger.info("Initialize Teiler App Config...");
        initializeLanguageTeilerAppMap(environment);
        logger.info("Expand no languages values...");
        expandNoLanguageValues();
        logger.info("Expand Teiler apps to teiler UI languages...");
        expandTeilerAppsToTeilerDashboardLanguages();
        logger.info("Add automatic generated values...");
        addAutomaticGeneratedValues();
        logger.info("Update language app id teiler app map...");
        updateLanguageAppIdTeilerAppMap();
    }

    private String fetchTeilerOrchestratorHttpRelativePath(String teilerOrchestratorHttpRelativePath) {
        String result = "";
        if (teilerOrchestratorHttpRelativePath != null && teilerOrchestratorHttpRelativePath.length() > 0) {
            result = (teilerOrchestratorHttpRelativePath.charAt(0) == '/') ? teilerOrchestratorHttpRelativePath.substring(1) : teilerOrchestratorHttpRelativePath;
        }
        return result;
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

    private void expandTeilerAppsToTeilerDashboardLanguages() {
        List<Integer> definedAppIds = languageAppIdTeilerAppMap.values().stream().map(appIdTeilerAppMap ->
                appIdTeilerAppMap.keySet()).flatMap(Collection::stream).toList();


        Arrays.stream(teilerDashboardLanguages).forEach(language -> {
            Map<Integer, TeilerApp> appIdTeilerAppMap = languageAppIdTeilerAppMap.get(language);
            if (appIdTeilerAppMap == null) {
                appIdTeilerAppMap = new HashMap<>();
                languageAppIdTeilerAppMap.put(language, appIdTeilerAppMap);
            }
            definedAppIds.stream()
                    .filter(appId -> !languageAppIdTeilerAppMap.get(language).keySet().contains(appId))
                    .forEach(appId -> languageAppIdTeilerAppMap.get(language).put(appId, getDefaultTeilerApp(appId).clone()));
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
                teilerApp.setRouterLink(generateRouterLink(teilerApp, language));
                teilerApp.setSingleSpaLink(singleSpaLinkGenerator.generateSingleSpaLink(teilerApp.getName(), language));
                if (teilerApp.getExternLink() == null) {
                    teilerApp.setExternLink(TeilerBackendConst.IS_EXTERNAL_LINK_DEFAULT);
                }
                if (teilerApp.getExternLink() == false && teilerApp.getSingleSpaMainJs() == null) {
                    teilerApp.setSingleSpaMainJs(TeilerBackendConst.SINGLE_SPA_DEFAULT_MAIN_JS);
                }
                if (teilerApp.getActivated() == null) {
                    teilerApp.setActivated(TeilerBackendConst.IS_ACTIVATED_DEFAULT);
                }
                if (teilerApp.getLocal() == null) {
                    teilerApp.setLocal(TeilerBackendConst.IS_LOCAL_DEFAULT);
                }
                if (teilerApp.getInMenu() == null) {
                    teilerApp.setInMenu(TeilerBackendConst.IN_MENU_DEFAULT);
                }
            });
        });

    }

    private String generateRouterLink(TeilerApp teilerApp, String language) {
        String routerLink = (language.equals(defaultLanguage)) ? teilerApp.getName() : language + '/' + teilerApp.getName();
        String root = (teilerOrchestratorHttpRelativePath.length() > 0 && routerLink.length() > 0) ? teilerOrchestratorHttpRelativePath + '/' : teilerOrchestratorHttpRelativePath;
        return root + routerLink;
    }

    @Scheduled(cron = TeilerBackendConst.CHECK_URLS_CRON_EXPRESSION_SV)
    public void updateLanguageAppIdTeilerAppMap() {
        getLanguageAppIdTeilerAppMap().values().stream()
                .map(appIdTeilerAppMap -> appIdTeilerAppMap.values())
                .flatMap(Collection::stream).toList()
                .forEach(teilerApp -> ping.updatePing(teilerApp));

    }

    public List<TeilerApp> getTeilerApps(String language) {
        Map<Integer, TeilerApp> appIdTeilerAppMap = getLanguageAppIdTeilerAppMap().get(language.toLowerCase());
        return (appIdTeilerAppMap != null) ? appIdTeilerAppMap.values().stream().toList() : new ArrayList<>();
    }

    public List<TeilerApp> getTeilerApps() {
        return getLanguageAppIdTeilerAppMap().values().stream()
                .map(appIdTeilerAppMap -> appIdTeilerAppMap.values()).flatMap(Collection::stream).toList();
    }

    private synchronized Map<String, Map<Integer, TeilerApp>> getLanguageAppIdTeilerAppMap() {
        return languageAppIdTeilerAppMap;
    }


}
