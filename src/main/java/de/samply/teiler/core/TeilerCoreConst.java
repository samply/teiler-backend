package de.samply.teiler.core;

public class TeilerCoreConst {

    public final static String APP_NAME = "teiler-core";

    // REST API path
    public final static String INFO_PATH = "/info";
    public final static String SERVICE_URL_PATH = "/serviceUrl/{serviceName}";

    // Services
    public final static String NNGM_SERVICE = "NNGM";
    public final static String QUALITY_REPORT_SERVICE = "QUALITY_REPORT";

    public final static String NNGM_URL_SV = "${" + NNGM_SERVICE + "_URL:#{null}}";
    public final static String QUALITY_REPORT_URL_SV = "${" + QUALITY_REPORT_SERVICE + "_URL:#{null}}";

}
