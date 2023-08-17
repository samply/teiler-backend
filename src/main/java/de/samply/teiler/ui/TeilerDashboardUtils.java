package de.samply.teiler.ui;

import de.samply.teiler.backend.TeilerBackendConst;
import org.springframework.util.StringUtils;

public class TeilerDashboardUtils {


    public static boolean isTeilerDashboard(String key) {
        return key.contains(TeilerBackendConst.TEILER_DASHBOARD_PREFIX);
    }

    public static boolean isUrl(String key) {
        return key.contains(TeilerBackendConst.URL_SUFFIX);
    }

    public static String getLanguage(String key) {
        String language = null;
        if (containsLanguage(key)) {
            key = key.substring(TeilerBackendConst.TEILER_DASHBOARD_PREFIX.length());
            int index1 = key.indexOf('_') + 1;
            int index2 = index1 + key.substring(index1).indexOf('_');
            language = key.substring(index1, index2).toLowerCase();
        }

        return language;
    }

    public static boolean containsLanguage(String key) {
        return (StringUtils.countOccurrencesOf(key, "_") >= 3);
    }

}
