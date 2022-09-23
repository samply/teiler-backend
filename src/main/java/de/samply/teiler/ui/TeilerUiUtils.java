package de.samply.teiler.ui;

import de.samply.teiler.core.TeilerCoreConst;
import org.springframework.util.StringUtils;

public class TeilerUiUtils {


    public static boolean isTeilerUi(String key) {
        return key.contains(TeilerCoreConst.TEILER_UI_PREFIX);
    }

    public static boolean isUrl(String key) {
        return key.contains(TeilerCoreConst.URL_SUFFIX);
    }

    public static String getLanguage(String key) {
        String language = null;
        if (containsLanguage(key)) {
            key = key.substring(TeilerCoreConst.TEILER_UI_PREFIX.length());
            int index1 = key.indexOf('_') + 1;
            int index2 = index1 + key.substring(index1).indexOf('_');
            language = key.substring(index1, index2);
        }

        return language;
    }

    public static boolean containsLanguage(String key) {
        return (StringUtils.countOccurrencesOf(key, "_") >= 3);
    }

}
