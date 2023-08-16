package de.samply.teiler.backend;

import de.samply.teiler.app.TeilerApp;
import de.samply.teiler.app.TeilerAppConfigurator;
import de.samply.teiler.app.TeilerAppRole;
import de.samply.teiler.utils.ProjectVersion;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeilerBackendApplicationTests {

    private static String defaultLanguage = "DE";
    private static String language2 = "EN";
    private static String[] languages = {defaultLanguage, language2};
    private static final String APPLICATION_PORT = "8383";
    private static TeilerApp teilerApp1;
    private static TeilerApp teilerApp2;
    private static TeilerApp teilerApp3;
    private static TeilerApp teilerApp4;
    private static Map<String,TeilerApp[]> languageOriginalTeilerApps;
    private static String enTeilerUiUrl = "http://teiler-ui:8540";
    private static String deTeilerUiUrl = "http://teiler-ui:8541";
    private static String rootConfigUrl = "http://root-config:9000";

    private static JSONObject importMaps;
    private ConfigurableApplicationContext teilerBackendContext1;

    @BeforeAll
    static void beforeAll() throws JSONException {

        teilerApp1 = initializeTeilerApp1();
        teilerApp2 = initializeTeilerApp2();
        teilerApp3 = initializeTeilerApp3();
        teilerApp4 = initializeTeilerApp4();


        languageOriginalTeilerApps = new HashMap<>();
        TeilerApp[] defaultLanguageTeilerApps = {teilerApp1, teilerApp2};
        languageOriginalTeilerApps.put(defaultLanguage, defaultLanguageTeilerApps);
        TeilerApp[] language2TeilerApps = {teilerApp3, teilerApp4};
        languageOriginalTeilerApps.put(language2, language2TeilerApps);

        TeilerApp[] teilerApps = {teilerApp1, teilerApp2, teilerApp3, teilerApp4};
        importMaps = initializeImportMap(teilerApps);

    }

    private static TeilerApp initializeTeilerApp1() {

        TeilerApp teilerApp = new TeilerApp();

        teilerApp.setName("name1");
        teilerApp.setTitle("title1");
        teilerApp.setDescription("description1");
        teilerApp.setExternLink(false);
        teilerApp.setSourceUrl("http://sourceurl1:9292");
        TeilerAppRole[] roles = {TeilerAppRole.TEILER_USER};
        teilerApp.setRoles(roles);
        teilerApp.setRouterLink("name1");
        teilerApp.setSingleSpaLink("@samply/de/name1");
        teilerApp.setBackendUrl("http://backendurl1:4000");
        teilerApp.setActivated(true);
        teilerApp.setIconSourceUrl("http://www.myicons.com/iconurl1");
        teilerApp.setSingleSpaMainJs("samply-name1.js");
        teilerApp.setLocal(true);
        teilerApp.setBackendReachable(false);
        teilerApp.setFrontendReachable(false);
        teilerApp.setInMenu(true);
        teilerApp.setRouterLinkExtension("/:id");
        teilerApp.setSubroutes("[{path: 'myRoute', teilerAppName: 'my-component'}]");

        return teilerApp;

    }

    private static TeilerApp initializeTeilerApp2() {

        TeilerApp teilerApp = new TeilerApp();

        teilerApp.setName("name2");
        teilerApp.setTitle("title2");
        teilerApp.setDescription("description2");
        teilerApp.setExternLink(false);
        teilerApp.setSourceUrl("http://sourceurl2:7070");
        TeilerAppRole[] roles = {TeilerAppRole.TEILER_USER, TeilerAppRole.TEILER_PUBLIC};
        teilerApp.setRoles(roles);
        teilerApp.setRouterLink("name2");
        teilerApp.setSingleSpaLink("@samply/de/name2");
        teilerApp.setActivated(false);
        teilerApp.setOrder(1);
        teilerApp.setIconClass("bi bi-emoji-sunglasses");
        teilerApp.setSingleSpaMainJs("main.js");
        teilerApp.setLocal(false);
        teilerApp.setFrontendReachable(false);
        teilerApp.setInMenu(true);

        return teilerApp;

    }

    private static TeilerApp initializeTeilerApp3() {

        TeilerApp teilerApp = new TeilerApp();

        teilerApp.setName("name2");
        teilerApp.setTitle("title2" + language2);
        teilerApp.setDescription("description2" + language2);
        teilerApp.setExternLink(false);
        teilerApp.setSourceUrl("http://sourceurl2:7070/" + language2.toLowerCase());
        TeilerAppRole[] roles = {TeilerAppRole.TEILER_USER, TeilerAppRole.TEILER_PUBLIC};
        teilerApp.setRoles(roles);
        teilerApp.setRouterLink("en/name2");
        teilerApp.setSingleSpaLink("@samply/en/name2");
        teilerApp.setActivated(false);
        teilerApp.setOrder(1);
        teilerApp.setIconClass("bi bi-emoji-sunglasses");
        teilerApp.setSingleSpaMainJs("main.js");
        teilerApp.setLocal(false);
        teilerApp.setFrontendReachable(false);
        teilerApp.setInMenu(true);

        return teilerApp;

    }

    private static TeilerApp initializeTeilerApp4() {

        TeilerApp teilerApp = new TeilerApp();

        teilerApp.setName("name1");
        teilerApp.setTitle("title1");
        teilerApp.setDescription("description1");
        teilerApp.setExternLink(false);
        teilerApp.setSourceUrl("http://sourceurl1:9292");
        TeilerAppRole[] roles = {TeilerAppRole.TEILER_USER};
        teilerApp.setRoles(roles);
        teilerApp.setRouterLink("en/name1");
        teilerApp.setSingleSpaLink("@samply/en/name1");
        teilerApp.setBackendUrl("http://backendurl1:4000");
        teilerApp.setActivated(true);
        teilerApp.setIconSourceUrl("http://www.myicons.com/iconurl1");
        teilerApp.setSingleSpaMainJs("samply-name1.js");
        teilerApp.setLocal(true);
        teilerApp.setBackendReachable(false);
        teilerApp.setFrontendReachable(false);
        teilerApp.setInMenu(true);
        teilerApp.setRouterLinkExtension("/:id");
        teilerApp.setSubroutes("[{path: 'myRoute', teilerAppName: 'my-component'}]");

        return teilerApp;

    }

    private static JSONObject initializeImportMap(TeilerApp[] originalTeilerApps) throws JSONException {

        JSONObject importMaps = new JSONObject();
        JSONObject imports = new JSONObject();
        importMaps.put(TeilerBackendConst.SINGLE_SPA_IMPORTS, imports);
        imports.put("@samply/root-config", generateSingleSpaUrl(rootConfigUrl) + "/samply-root-config.js");
        imports.put("@samply/en/teiler-ui", generateSingleSpaUrl(enTeilerUiUrl) + "/main.js");
        imports.put("@samply/de/teiler-ui", generateSingleSpaUrl(deTeilerUiUrl) + "/main.js");
        Arrays.stream(originalTeilerApps).forEach(teilerApp -> addToImports(
                imports,
                teilerApp.getSingleSpaLink(),
                generateSingleSpaUrl(teilerApp.getSourceUrl()) + '/' + ((teilerApp.getSingleSpaMainJs() != null) ? teilerApp.getSingleSpaMainJs() : "main.js")));

        return importMaps;

    }

    private static String generateSingleSpaUrl(String url) {
        return url.substring("http:".length());
    }

    private static void addToImports(JSONObject imports, String key, String value) {
        try {
            imports.put(key, value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private SpringApplication createTeilerBackend() {
        return new SpringApplicationBuilder(TeilerBackendApplication.class)
                .properties(TeilerBackendConst.DEFAULT_LANGUAGE + '=' + defaultLanguage,
                        "APPLICATION_PORT=" + APPLICATION_PORT,
                        "TEILER_APP1_NAME=" + teilerApp1.getName(),
                        "TEILER_APP1_TITLE=" + teilerApp1.getTitle(),
                        "TEILER_APP1_DESCRIPTION=" + teilerApp1.getDescription(),
                        "TEILER_APP1_ROUTERLINKEXTENSION=" + teilerApp1.getRouterLinkExtension(),
                        "TEILER_APP1_ISEXTERNALLINK=" + teilerApp1.getExternLink(),
                        "TEILER_APP1_SOURCEURL=" + teilerApp1.getSourceUrl(),
                        "TEILER_APP1_BACKENDURL=" + teilerApp1.getBackendUrl(),
                        "TEILER_APP1_ICONSOURCEURL=" + teilerApp1.getIconSourceUrl(),
                        "TEILER_APP1_SINGLESPAMAINJS=" + teilerApp1.getSingleSpaMainJs(),
                        "TEILER_APP1_SUBROUTES=" + teilerApp1.getSubroutes(),
                        "TEILER_APP1_ROLES=" + String.join(",", Arrays.stream(teilerApp1.getRoles()).map(TeilerAppRole::toString).toArray(String[]::new)),
                        "TEILER_APP2_NAME=" + teilerApp2.getName(),
                        "TEILER_APP2_TITLE=" + teilerApp2.getTitle(),
                        "TEILER_APP2_DESCRIPTION=" + teilerApp2.getDescription(),
                        "TEILER_APP2_ISEXTERNALLINK=" + teilerApp2.getExternLink(),
                        "TEILER_APP2_DE_SOURCEURL=" + teilerApp2.getSourceUrl(),
                        "TEILER_APP2_ROLES=" + String.join(",", Arrays.stream(teilerApp2.getRoles()).map(TeilerAppRole::toString).toArray(String[]::new)),
                        "TEILER_APP2_EN_NAME=" + teilerApp3.getName(),
                        "TEILER_APP2_EN_TITLE=" + teilerApp3.getTitle(),
                        "TEILER_APP2_EN_DESCRIPTION=" + teilerApp3.getDescription(),
                        "TEILER_APP2_EN_SOURCEURL=" + teilerApp3.getSourceUrl(),
                        "TEILER_APP2_ISACTIVATED=" + teilerApp3.getActivated(),
                        "TEILER_APP2_ISLOCAL=" + teilerApp3.getLocal(),
                        "TEILER_APP2_ICONCLASS=" + teilerApp3.getIconClass(),
                        "TEILER_APP2_ORDER=" + teilerApp3.getOrder(),
                        "TEILER_UI_EN_URL=" + enTeilerUiUrl,
                        "TEILER_UI_DE_URL=" + deTeilerUiUrl,
                        "TEILER_ROOT_CONFIG_URL=" + rootConfigUrl,
                        "CONFIG_ENV_VAR_PATH=ccp.conf",
                        "TEILER_CONFIG_UPDATER_CRON=0 1 * * * *",
                        "TEILER_ROOT_CONFIG_HTTP_RELATIVE_PATH="
                ).build();
    }


    @BeforeEach
    void setUp() {
        teilerBackendContext1 = createTeilerBackend().run();
    }

    @AfterEach
    void tearDown() {
        teilerBackendContext1.close();
    }

    @Test
    void getTeilerApps() {
        TeilerAppConfigurator teilerAppConfigurator = teilerBackendContext1.getBean(TeilerAppConfigurator.class);
        Arrays.stream(languages).forEach(language -> checkTeilerApps(teilerAppConfigurator.getTeilerApps(language),language));
    }

    private void checkTeilerApps(List<TeilerApp> teilerApps, String language) {

        Map<String, TeilerApp> routerLinkTeilerAppMap = new HashMap<>();
        teilerApps.forEach(teilerApp -> routerLinkTeilerAppMap.put(teilerApp.getName(), teilerApp));

        Arrays.stream(languageOriginalTeilerApps.get(language)).forEach(originalTeilerApp -> {
            TeilerApp generatedTeilerApp = routerLinkTeilerAppMap.get(originalTeilerApp.getName());
            assertEquals(originalTeilerApp, generatedTeilerApp);
        });

    }

    @Test
    void getTeilerAppsPerHttpRequest() {

        List<TeilerApp> generatedTeilerApps = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        Arrays.stream(languages).forEach(language -> {
            ResponseEntity<TeilerApp[]> response = restTemplate.getForEntity(getTeilerBackendUrl(APPLICATION_PORT) + "/apps/" + language, TeilerApp[].class);
            checkTeilerApps(Arrays.stream(response.getBody()).toList(), language);
        });

    }

    @Test
    void testInfo() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(getTeilerBackendUrl(APPLICATION_PORT) + TeilerBackendConst.INFO_PATH, String.class);
        assertEquals(ProjectVersion.getProjectVersion(), response.getBody());
    }

    @Test
    void testImportMaps() throws JSONException {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(getTeilerBackendUrl(APPLICATION_PORT) + TeilerBackendConst.IMPORT_MAP_PATH, String.class);

        JSONObject generatedImports = new JSONObject(response.getBody()).getJSONObject(TeilerBackendConst.SINGLE_SPA_IMPORTS);
        JSONObject originalImports = importMaps.getJSONObject(TeilerBackendConst.SINGLE_SPA_IMPORTS);

        Map<String, String> generatedImportsMap = extractImports(generatedImports);
        Map<String, String> originalImportsMap = extractImports(originalImports);

        originalImportsMap.keySet().forEach(key -> assertEquals(originalImportsMap.get(key), generatedImportsMap.get(key)));

    }

    private Map<String, String> extractImports(JSONObject imports) throws JSONException {
        Map<String, String> importsMap = new HashMap<>();

        for (Iterator key = imports.keys(); key.hasNext(); ) {
            String element1 = (String) key.next();
            String element2 = (String) imports.get(element1);
            importsMap.put(element1, element2);
        }

        return importsMap;
    }

    private String getTeilerBackendUrl(String port) {
        return "http://localhost:" + port;
    }

}
