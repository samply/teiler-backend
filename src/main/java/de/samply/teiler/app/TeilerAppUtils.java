package de.samply.teiler.app;

import de.samply.teiler.core.TeilerCoreConst;
import org.springframework.util.StringUtils;

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

    public static boolean isName(String key) {
        return key.contains(TeilerCoreConst.NAME_SUFFIX);
    }

    public static boolean isTitle(String key) {
        return key.contains(TeilerCoreConst.TITLE_SUFFIX);
    }

    public static boolean isDescription(String key) {
        return key.contains(TeilerCoreConst.DESCRIPTION_SUFFIX);
    }

    public static boolean isSourceLink(String key) {
        return key.contains(TeilerCoreConst.SOURCE_LINK_SUFFIX);
    }

    public static boolean isExternalLink(String key) {
        return key.contains(TeilerCoreConst.IS_EXTERNAL_LINK_SUFFIX);
    }
    public static boolean isActivated(String key) {
        return key.contains(TeilerCoreConst.IS_ACTIVATED_SUFFIX);
    }

    public static boolean isRoles(String key) {
        return key.contains(TeilerCoreConst.ROLES_SUFFIX);
    }

    public static boolean isIconClass(String key) {
        return key.contains(TeilerCoreConst.ICON_CLASS);
    }

    public static boolean isIconSourceUrl(String key) {
        return key.contains(TeilerCoreConst.ICON_SOURCE_URL);
    }

    public static boolean isOrder(String key) {
        return key.contains(TeilerCoreConst.ORDER);
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

        teilerAppSetterMap.put(TeilerAppUtils::isName, TeilerApp::setName);
        teilerAppSetterMap.put(TeilerAppUtils::isTitle, TeilerApp::setTitle);
        teilerAppSetterMap.put(TeilerAppUtils::isDescription, TeilerApp::setDescription);
        teilerAppSetterMap.put(TeilerAppUtils::isSourceLink, TeilerApp::setSourceLink);
        teilerAppSetterMap.put(TeilerAppUtils::isExternalLink, TeilerApp::setExternLink);
        teilerAppSetterMap.put(TeilerAppUtils::isActivated, TeilerApp::setActivated);
        teilerAppSetterMap.put(TeilerAppUtils::isIconClass, TeilerApp::setIconClass);
        teilerAppSetterMap.put(TeilerAppUtils::isIconSourceUrl, TeilerApp::setIconSourceUrl);
        teilerAppSetterMap.put(TeilerAppUtils::isOrder, TeilerApp::setOrder);
        teilerAppSetterMap.put(TeilerAppUtils::isRoles, (teilerApp, value) -> teilerApp.setRoles(
                Arrays.stream(value.trim().split(",")).map(role -> TeilerAppRole.valueOf(role)).toArray(TeilerAppRole[]::new)));

        return teilerAppSetterMap;

    }

    public static void addKeyValue(String key, String value, TeilerApp teilerApp) {
        teilerAppSetterMap.keySet().stream().filter(is -> is.apply(key)).forEach(is -> teilerAppSetterMap.get(is).accept(teilerApp, value));
    }


}
