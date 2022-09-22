package de.samply.teiler.app;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class TeilerAppUtils {

    public final static String APP_PREFIX = "TAPP";
    public final static String NAME_SUFFIX = "NAME";
    public final static String TITLE_SUFFIX = "TITLE";
    public final static String DESCRIPTION_SUFFIX = "DESCRIPTION";
    public final static String SOURCE_LINK_SUFFIX = "SOURCELINK";
    public final static String IS_EXTERNAL_LINK_SUFFIX = "ISEXTERNALLINK";
    public final static String ROLES_SUFFIX = "ROLES";

    private final static Map<Function<String, Boolean>, BiConsumer<TeilerApp, String>> teilerAppSetterMap = initializeTeilerAppSetterMap();

    public static boolean isName(String key) {
        return key.contains(NAME_SUFFIX);
    }

    public static boolean isTitle(String key) {
        return key.contains(TITLE_SUFFIX);
    }

    public static boolean isDescription(String key) {
        return key.contains(DESCRIPTION_SUFFIX);
    }

    public static boolean isSourceLink(String key) {
        return key.contains(SOURCE_LINK_SUFFIX);
    }

    public static boolean isExternalLink(String key) {
        return key.contains(IS_EXTERNAL_LINK_SUFFIX);
    }

    public static boolean isRoles(String key) {
        return key.contains(ROLES_SUFFIX);
    }

    public static Integer getAppId(String key) {
        return Integer.valueOf(key.substring(APP_PREFIX.length(), key.indexOf('_')));
    }

    public static String getLanguage(String key) {
        String language = null;
        if (containsLanguage(key)) {
            int index1 = key.indexOf('_')+1;
            int index2 = index1 + key.substring(index1).indexOf('_');
            language = key.substring(index1, index2);
        }

        return language;
    }

    public static boolean containsLanguage(String key) {
        return (StringUtils.countOccurrencesOf(key, "_") >= 2);
    }

    private static Map<Function<String, Boolean>, BiConsumer<TeilerApp, String>> initializeTeilerAppSetterMap() {

        Map<Function<String, Boolean>, BiConsumer<TeilerApp, String>> teilerAppSetterMap = new HashMap<>();

        teilerAppSetterMap.put(TeilerAppUtils::isName, TeilerApp::setName);
        teilerAppSetterMap.put(TeilerAppUtils::isTitle, TeilerApp::setTitle);
        teilerAppSetterMap.put(TeilerAppUtils::isDescription, TeilerApp::setDescription);
        teilerAppSetterMap.put(TeilerAppUtils::isSourceLink, TeilerApp::setSourceLink);
        teilerAppSetterMap.put(TeilerAppUtils::isExternalLink, TeilerApp::setExternLink);
        teilerAppSetterMap.put(TeilerAppUtils::isRoles, (teilerApp, value) -> teilerApp.setRoles(Arrays.stream(value.trim().split(",")).map(role -> TeilerAppRole.valueOf(role)).toArray(TeilerAppRole[]::new)));

        return teilerAppSetterMap;

    }

    public static void addKeyValue(String key, String value, TeilerApp teilerApp) {
        teilerAppSetterMap.keySet().stream().filter(is -> is.apply(key)).forEach(is -> teilerAppSetterMap.get(is).accept(teilerApp, value));
    }


}
