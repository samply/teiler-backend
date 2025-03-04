package de.samply.teiler.app;

import de.samply.teiler.backend.TeilerBackendConst;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class TeilerAppUtils {

    private final static Map<Function<String, Boolean>, BiConsumer<TeilerApp, String>> teilerAppSetterMap = initializeTeilerAppSetterMap();

    public static boolean isTeilerApp(String key) {
        return key.startsWith(TeilerBackendConst.APP_PREFIX);
    }

    private static boolean isSuffix(String key, String suffix) {
        return key != null && key.contains("_" + suffix);
    }

    public static Integer getAppId(String key) {
        key = key.substring(TeilerBackendConst.APP_PREFIX.length());
        return Integer.valueOf(key.substring(0, key.indexOf('_')));
    }

    public static String getLanguage(String key) {
        String language = null;
        if (containsLanguage(key)) {
            key = key.substring(TeilerBackendConst.APP_PREFIX.length());
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

        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.NAME_SUFFIX), TeilerApp::setName);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.TITLE_SUFFIX), TeilerApp::setTitle);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.DESCRIPTION_SUFFIX), TeilerApp::setDescription);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.SOURCE_URL_SUFFIX), TeilerApp::setSourceUrl);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.SOURCE_CHECK_URL_SUFFIX), TeilerApp::setSourceCheckUrl);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.IS_EXTERNAL_LINK_SUFFIX), TeilerApp::setExternLink);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.IS_ACTIVATED_SUFFIX), TeilerApp::setActivated);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.IS_LOCAL_SUFFIX), TeilerApp::setLocal);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.ICON_CLASS_SUFFIX), TeilerApp::setIconClass);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.ICON_SOURCE_URL_SUFFIX), TeilerApp::setIconSourceUrl);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.BACKEND_URL_SUFFIX), TeilerApp::setBackendUrl);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.BACKEND_CHECK_URL_SUFFIX), TeilerApp::setBackendCheckUrl);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.ORDER_SUFFIX), TeilerApp::setOrder);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.ROUTER_LINK_EXTENSION_SUFFIX), TeilerApp::setRouterLinkExtension);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.IN_MENU_SUFFIX), TeilerApp::setInMenu);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.SUBROUTES_SUFFIX), TeilerApp::setSubroutes);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.SINGLE_SPA_MAIN_JS_SUFFIX), TeilerApp::setSingleSpaMainJs);
        teilerAppSetterMap.put(key -> isSuffix(key, TeilerBackendConst.ROLES_SUFFIX), (teilerApp, value) -> teilerApp.setRoles(
                Arrays.stream(value.trim().split(",")).map(role -> TeilerAppRole.valueOf(role)).toArray(TeilerAppRole[]::new)));

        return teilerAppSetterMap;

    }

    public static void addKeyValue(String key, String value, TeilerApp teilerApp) {
        teilerAppSetterMap.keySet().stream().filter(is -> is.apply(key)).forEach(is -> teilerAppSetterMap.get(is).accept(teilerApp, value));
    }


}
