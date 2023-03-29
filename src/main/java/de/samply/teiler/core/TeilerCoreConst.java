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
    public final static String TEILER_ROOT_CONFIG_HTTP_RELATIVE_PATH = "TEILER_ROOT_CONFIG_HTTP_RELATIVE_PATH";
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
    public final static String ROUTER_LINK_EXTENSION_SUFFIX = "ROUTERLINKEXTENSION";
    public final static String SINGLE_SPA_MAIN_JS_SUFFIX = "SINGLESPAMAINJS";
    public final static String TEILER_UI_PREFIX = "TEILER_UI";
    public final static String URL_SUFFIX = "URL";
    public final static String IN_MENU_SUFFIX = "INMENU";
    public final static String SUBROUTES_SUFFIX = "SUBROUTES";
    public final static String TEILER_ROOT_CONFIG_URL = "TEILER_ROOT_CONFIG_URL";
    public final static String CONFIG_ENV_VAR_PATH = "CONFIG_ENV_VAR_PATH";
    public final static String TEILER_CONFIG_UPDATER_CRON = "TEILER_CONFIG_UPDATER_CRON";
    public final static String TEILER_CORE_ASSETS_DIRECTORY = "TEILER_CORE_ASSETS_DIRECTORY";
    public final static String PING_CONNECTION_TIMEOUT_IN_SECONDS = "PING_CONNECTION_TIMEOUT_IN_SECONDS";
    public final static String PING_READ_TIMEOUT_IN_SECONDS = "PING_READ_TIMEOUT_IN_SECONDS";


    // Spring Values
    public final static String HEAD_SV = "${";
    public final static String BOTTOM_SV = "}";
    public final static String DEFAULT_LANGUAGE_SV = HEAD_SV + DEFAULT_LANGUAGE + ":EN" + BOTTOM_SV;
    public final static String TEILER_ROOT_CONFIG_HTTP_RELATIVE_PATH_SV = HEAD_SV + TEILER_ROOT_CONFIG_HTTP_RELATIVE_PATH + ":#{''}" + BOTTOM_SV;
    public final static String PROJECT_ORGANISATION_SV = HEAD_SV + PROJECT_ORGANISATION + ":samply" + BOTTOM_SV;
    public final static String TEILER_ROOT_CONFIG_URL_SV = HEAD_SV + TEILER_ROOT_CONFIG_URL + ":#{null}" + BOTTOM_SV;
    public final static String CONFIG_ENV_VAR_PATH_SV = HEAD_SV + CONFIG_ENV_VAR_PATH + BOTTOM_SV;
    public final static String TEILER_CONFIG_UPDATER_CRON_SV = HEAD_SV + TEILER_CONFIG_UPDATER_CRON + ":#{'-'}" + BOTTOM_SV;
    public final static String TEILER_CORE_ASSETS_DIRECTORY_SV = HEAD_SV + TEILER_CORE_ASSETS_DIRECTORY + ":#{null}" + BOTTOM_SV;
    public final static String PING_CONNECTION_TIMEOUT_IN_SECONDS_SV = HEAD_SV + PING_CONNECTION_TIMEOUT_IN_SECONDS + ":5" + BOTTOM_SV;
    public final static String PING_READ_TIMEOUT_IN_SECONDS_SV = HEAD_SV + PING_READ_TIMEOUT_IN_SECONDS + ":5" + BOTTOM_SV;


    // Other constants
    public final static boolean IS_EXTERNAL_LINK_DEFAULT = false;
    public final static boolean IS_ACTIVATED_DEFAULT = true;
    public final static boolean IS_LOCAL_DEFAULT = true;
    public final static boolean IN_MENU_DEFAULT = true;
    public final static String SINGLE_SPA_IMPORTS = "imports";
    public final static String SINGLE_SPA_ROOT_CONFIG = "root-config";
    public final static String SINGLE_SPA_DEFAULT_MAIN_JS = "main.js";
    public final static String TEILER_UI_APP_NAME = "teiler-ui";

}
