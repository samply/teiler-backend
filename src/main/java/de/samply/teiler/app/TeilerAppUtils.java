package de.samply.teiler.app;

import de.samply.teiler.core.TeilerCoreConst;
import de.samply.teiler.utils.Ping;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class TeilerAppUtils {

    private final static Map<Function<String, Boolean>, BiConsumer<TeilerApp, String>> teilerAppSetterMap = initializeTeilerAppSetterMap();

    public static boolean isTeilerApp(String key) {
        return key.startsWith(TeilerCoreConst.APP_PREFIX);
    }

    private static boolean isSuffix(String key, String suffix) {
        return key != null && key.contains("_" + suffix);
    }

    public static Integer getAppId(String key) {
        key = key.substring(TeilerCoreConst.APP_PREFIX.length());
        return Integer.valueOf(key.substring(0, key.indexOf('_')));
    }

    public static String getLanguage(String key) {
        String language = null;
        if (containsLanguage(key)) {
            key = key.substring(TeilerCoreConst.APP_PREFIX.length());
            int index1 = key.indexOf('_') + 1;
            int index2 = index1 + key.substring(index1).indexOf('_');
            language = key.substring(index1, index2).toLowerCase();
        }

        return language;
    }

    public static boolean containsLanguage(String key) {
        return (StringUtils.countOccurrencesOf(key, "_") >= 3);
    }

    private static Map<Function<String, Boolean>, BiConsumer<TeilerApp, String>> initializeTeilerAppSetterMap() {

        Map<Function<String, Boolean>, BiConsumer<TeilerApp, String>> teilerAppSetterMap = new HashMap<>();

        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.NAME_SUFFIX), TeilerApp::setName);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.TITLE_SUFFIX), TeilerApp::setTitle);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.DESCRIPTION_SUFFIX), TeilerApp::setDescription);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.SOURCE_URL_SUFFIX), TeilerApp::setSourceUrl);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.IS_EXTERNAL_LINK_SUFFIX), TeilerApp::setExternLink);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.IS_ACTIVATED_SUFFIX), TeilerApp::setActivated);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.IS_LOCAL_SUFFIX), TeilerApp::setLocal);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.ICON_CLASS_SUFFIX), TeilerApp::setIconClass);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.ICON_SOURCE_URL_SUFFIX), TeilerApp::setIconSourceUrl);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.BACKEND_URL_SUFFIX), TeilerApp::setBackendUrl);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.ORDER_SUFFIX), TeilerApp::setOrder);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.ROUTER_LINK_EXTENSION_SUFFIX), TeilerApp::setRouterLinkExtension);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.IN_MENU_SUFFIX), TeilerApp::setInMenu);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.SINGLE_SPA_MAIN_JS_SUFFIX), TeilerApp::setSingleSpaMainJs);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerCoreConst.ROLES_SUFFIX), (teilerApp, value) -> teilerApp.setRoles(
                Arrays.stream(value.trim().split(",")).map(role -> TeilerAppRole.valueOf(role)).toArray(TeilerAppRole[]::new)));

        return teilerAppSetterMap;

    }

    public static void addKeyValue(String key, String value, TeilerApp teilerApp) {
        teilerAppSetterMap.keySet().stream().filter(is -> is.apply(key)).forEach(is -> teilerAppSetterMap.get(is).accept(teilerApp, value));
    }

    public static void updatePing(TeilerApp teilerApp) {
        updatePingFrontendUrl(teilerApp);
        updatePingBackendUrl(teilerApp);
    }

    private static void updatePingFrontendUrl(TeilerApp teilerApp) {
        if (teilerApp.getSourceUrl() != null) {
            String frontendUrl = (teilerApp.getSingleSpaMainJs() != null) ?
                    UriComponentsBuilder
                            .fromHttpUrl(teilerApp.getSourceUrl())
                            .path('/' + teilerApp.getSingleSpaMainJs()).toUriString()
                    : teilerApp.getSourceUrl();

            teilerApp.setFrontendReachable(Ping.ping(frontendUrl));
        }
    }

    private static void updatePingBackendUrl(TeilerApp teilerApp) {
        if (teilerApp.getBackendUrl() != null) {
            teilerApp.setBackendReachable(Ping.ping(teilerApp.getBackendUrl()));
        }
    }

}
