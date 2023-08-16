package de.samply.teiler.backend;

import de.samply.teiler.config.ConfigBlock;
import de.samply.teiler.config.ConfigBlocksConfigurator;
import de.samply.teiler.singlespa.ImportsMapConfigurator;
import de.samply.teiler.app.TeilerApp;
import de.samply.teiler.app.TeilerAppConfigurator;
import de.samply.teiler.utils.CorsChecker;
import de.samply.teiler.utils.ProjectVersion;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TeilerBackendController {

    private final String projectVersion = ProjectVersion.getProjectVersion();
    private CorsChecker corsChecker;
    private TeilerAppConfigurator teilerAppConfigurator;
    private ImportsMapConfigurator importsMapConfigurator;
    private ConfigBlocksConfigurator configBlocksConfigurator;
    private String defaultLanguage;

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

}
