package de.samply.teiler.core;

public class TeilerCoreConst {

    public final static String APP_NAME = "teiler-core";

    // REST API path
    public final static String INFO_PATH = "/info";
    public final static String APPS_PATH = "/apps/{language}";
    public final static String IMPORT_MAP_PATH = "/import-map";

    // Environment Variables
    public final static String DEFAULT_LANGUAGE = "DEFAULT_LANGUAGE";
    public final static String PROJECT_ORGANISATION = "PROJECT_ORGANISATION";
    public final static String APP_PREFIX = "TEILER_APP";
    public final static String NAME_SUFFIX = "NAME";
    public final static String TITLE_SUFFIX = "TITLE";
    public final static String DESCRIPTION_SUFFIX = "DESCRIPTION";
    public final static String SOURCE_LINK_SUFFIX = "SOURCELINK";
    public final static String IS_EXTERNAL_LINK_SUFFIX = "ISEXTERNALLINK";
    public final static String IS_ACTIVATED_SUFFIX = "ISACTIVATED";
    public final static String ROLES_SUFFIX = "ROLES";
    public final static String ICON_CLASS = "ICONCLASS";
    public final static String ICON_SOURCE_URL = "ICONSOURCEURL";
    public final static String ORDER = "ORDER";
    public final static String TEILER_UI_PREFIX = "TEILER_UI";
    public final static String URL_SUFFIX = "URL";
    public final static String TEILER_ROOT_CONFIG_URL = "TEILER_ROOT_CONFIG_URL";


    public final static boolean IS_EXTERNAL_LINK_DEFAULT = false;
    public final static boolean IS_ACTIVATED_DEFAULT = true;
    public final static String SINGLE_SPA_IMPORTS = "imports";


    // Spring Values
    public final static String DEFAULT_LANGUAGE_SV = "${" + DEFAULT_LANGUAGE + ":EN}";
    public final static String PROJECT_ORGANISATION_SV = "${" + PROJECT_ORGANISATION + ":samply}";
    public final static String TEILER_ROOT_CONFIG_URL_SV = "${" + TEILER_ROOT_CONFIG_URL + ":#{null}}";

}
