package de.samply.teiler.core;

public class TeilerCoreConst {

    public final static String APP_NAME = "teiler-core";

    // REST API path
    public final static String INFO_PATH = "/info";
    public final static String APPS_PATH = "/apps/{language}";
    public final static String CONFIG_PATH = "/config";
    public final static String IMPORT_MAP_PATH = "/import-map";

    // Environment Variables
    public final static String DEFAULT_LANGUAGE = "DEFAULT_LANGUAGE";
    public final static String PROJECT_ORGANISATION = "PROJECT_ORGANISATION";
    public final static String APP_PREFIX = "TEILER_APP";
    public final static String NAME_SUFFIX = "NAME";
    public final static String TITLE_SUFFIX = "TITLE";
    public final static String DESCRIPTION_SUFFIX = "DESCRIPTION";
    public final static String SOURCE_URL_SUFFIX = "SOURCEURL";
    public final static String IS_EXTERNAL_LINK_SUFFIX = "ISEXTERNALLINK";
    public final static String IS_ACTIVATED_SUFFIX = "ISACTIVATED";
    public final static String IS_LOCAL_SUFFIX = "ISLOCAL";
    public final static String ROLES_SUFFIX = "ROLES";
    public final static String ICON_CLASS_SUFFIX = "ICONCLASS";
    public final static String ICON_SOURCE_URL_SUFFIX = "ICONSOURCEURL";
    public final static String BACKEND_URL_SUFFIX = "BACKENDURL";
    public final static String ORDER_SUFFIX = "ORDER";
    public final static String SINGLE_SPA_MAIN_JS_SUFFIX = "SINGLESPAMAINJS";
    public final static String TEILER_UI_PREFIX = "TEILER_UI";
    public final static String URL_SUFFIX = "URL";
    public final static String TEILER_ROOT_CONFIG_URL = "TEILER_ROOT_CONFIG_URL";
    public final static String CONFIG_ENV_VAR_FILENAME = "CONFIG_ENV_VAR_FILENAME";


    public final static boolean IS_EXTERNAL_LINK_DEFAULT = false;
    public final static boolean IS_ACTIVATED_DEFAULT = true;
    public final static boolean IS_LOCAL_DEFAULT = true;
    public final static String SINGLE_SPA_IMPORTS = "imports";
    public final static String SINGLE_SPA_ROOT_CONFIG = "root-config";
    public final static String SINGLE_SPA_DEFAULT_MAIN_JS = "main.js";
    public final static String TEILER_UI_APP_NAME = "teiler-ui";


    // Spring Values
    public final static String HEAD_SV = "${";
    public final static String BOTTOM_SV = "}";
    public final static String DEFAULT_LANGUAGE_SV = HEAD_SV + DEFAULT_LANGUAGE + ":EN"+ BOTTOM_SV;
    public final static String PROJECT_ORGANISATION_SV = HEAD_SV + PROJECT_ORGANISATION + ":samply" + BOTTOM_SV;
    public final static String TEILER_ROOT_CONFIG_URL_SV = HEAD_SV + TEILER_ROOT_CONFIG_URL + ":#{null}" + BOTTOM_SV;

    public final static String CONFIG_ENV_VAR_FILENAME_SV = HEAD_SV + CONFIG_ENV_VAR_FILENAME + BOTTOM_SV;

}
