package de.samply.teiler.backend;

import de.samply.teiler.app.TeilerApp;
import de.samply.teiler.app.TeilerAppConfigurator;
import de.samply.teiler.config.ConfigBlock;
import de.samply.teiler.config.ConfigBlocksConfigurator;
import de.samply.teiler.singlespa.ImportsMapConfigurator;
import de.samply.teiler.utils.CorsChecker;
import de.samply.teiler.utils.ProjectVersion;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
public class TeilerBackendController {

    private final String projectVersion = ProjectVersion.getProjectVersion();
    private CorsChecker corsChecker;
    private TeilerAppConfigurator teilerAppConfigurator;
    private ImportsMapConfigurator importsMapConfigurator;
    private ConfigBlocksConfigurator configBlocksConfigurator;
    private String defaultLanguage;


    private String teilerBackendAssetsDirectory;
    private static final Logger logger = LoggerFactory.getLogger(TeilerBackendController.class);

    private final ResourceLoader resourceLoader;

    public TeilerBackendController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping(TeilerBackendConst.INFO_PATH)
    public ResponseEntity<String> info() {
        return new ResponseEntity<>(projectVersion, HttpStatus.OK);
    }

    @GetMapping(TeilerBackendConst.APPS_PATH)
    public ResponseEntity<TeilerApp[]> getApps(@PathVariable String language, HttpServletRequest request) {

        HttpHeaders httpHeaders = createBasicHeaders(request);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(fetchApps(language), httpHeaders, HttpStatus.OK);

    }

    @GetMapping(TeilerBackendConst.IMPORT_MAP_PATH)
    public ResponseEntity<String> getImportMap(HttpServletRequest request) {

        HttpHeaders httpHeaders = createBasicHeaders(request);
        httpHeaders.set("Content-Type", "application/importmap+json");

        return new ResponseEntity<>(importsMapConfigurator.getImportsMaps().toString(), httpHeaders, HttpStatus.OK);

    }

    @GetMapping(TeilerBackendConst.CONFIG_PATH)
    public ResponseEntity<ConfigBlock[]> getApps(HttpServletRequest request) {

        HttpHeaders httpHeaders = createBasicHeaders(request);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(configBlocksConfigurator.getConfigBlocks(), httpHeaders, HttpStatus.OK);

    }


    @GetMapping(TeilerBackendConst.ASSETS_PATH + "/{fileName}")
    public ResponseEntity<Resource> getTeilerBackendAsset(@PathVariable String fileName) {
        try {
            // Pfad zur Datei
            Path filePath = Paths.get(teilerBackendAssetsDirectory, fileName);

            // Überprüfen, ob die Datei existiert
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            // Laden der Datei als Ressource
            Resource resource = resourceLoader.getResource("file:" + filePath.toString());

            String contentType = servletContext.getMimeType(fileName);

            // Rückgabe der Datei
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);
        } catch (Exception e) {
            logger.error("Fehler beim Laden der Datei", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

/*
    @GetMapping(TeilerBackendConst.ASSETS_PATH + "/colors/{colorName}")
    public ResponseEntity<String> getBackgroundColor(@PathVariable String colorName) {
        try {
            // Laden der JSON
            Resource resource = resourceLoader.getResource("classpath:colors.json");
            InputStream inputStream = resource.getInputStream();

            // Einlesen der JSON in String
            StringBuilder jsonContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
            }

            String jsonString = jsonContent.toString();
            Map<String, String> colors = parseJsonColors(jsonString);

            // Überprüfen, ob die Farbe vorhanden ist
            if (!colors.containsKey(colorName)) {
                return ResponseEntity.notFound().build();
            }

            // Rückgabe der Farbe
            return ResponseEntity.ok().body(colors.get(colorName));
        } catch (IOException e) {
            logger.error("Fehler beim Laden der Farbe", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Manuelles Parsen des JSON-Strings, um die Farben zu extrahieren
    private Map<String, String> parseJsonColors(String jsonString) {
        Map<String, String> colors = new HashMap<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        for (String key : jsonObject.keySet()) {
            colors.put(key, jsonObject.getString(key));
        }
        return colors;
    }

*/

    private TeilerApp[] fetchApps(String language) {
        if (language == null) {
            language = defaultLanguage;
        }
        return teilerAppConfigurator.getTeilerApps(language).toArray(TeilerApp[]::new);
    }

    private HttpHeaders createBasicHeaders(HttpServletRequest request) {

        HttpHeaders httpHeaders = new HttpHeaders();

        String originUrl = request.getHeader(HttpHeaders.ORIGIN);
        if (corsChecker.isOriginUrlAllowed(originUrl)) {
            httpHeaders.set("Access-Control-Allow-Origin", originUrl);
        }

        return httpHeaders;

    }

    @Autowired
    public void setCorsChecker(CorsChecker corsChecker) {
        this.corsChecker = corsChecker;
    }

    @Autowired
    public void setTeilerAppConfigurator(TeilerAppConfigurator teilerAppConfigurator) {
        this.teilerAppConfigurator = teilerAppConfigurator;
    }

    @Autowired
    public void setImportsMapConfigurator(ImportsMapConfigurator importsMapConfigurator) {
        this.importsMapConfigurator = importsMapConfigurator;
    }

    @Autowired
    public void setConfigBlocksConfigurator(ConfigBlocksConfigurator configBlocksConfigurator) {
        this.configBlocksConfigurator = configBlocksConfigurator;
    }

    @Autowired
    public void setDefaultLanguage(@Value(TeilerBackendConst.DEFAULT_LANGUAGE_SV) String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    @Autowired
    public void setTeilerBackendAssetsDirectory(@Value(TeilerBackendConst.TEILER_BACKEND_ASSETS_DIRECTORY_SV) String teilerBackendAssetsDirectory) {
        this.teilerBackendAssetsDirectory = teilerBackendAssetsDirectory;
    }
    @Autowired
    private ServletContext servletContext;

}
