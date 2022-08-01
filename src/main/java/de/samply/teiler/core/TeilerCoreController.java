package de.samply.teiler.core;

import de.samply.teiler.utils.ProjectVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeilerCoreController {

    private final String projectVersion = ProjectVersion.getProjectVersion();
    private TeilerCoreServices teilerCoreServices;

    @Autowired
    public void setTeilerCoreServices(TeilerCoreServices teilerCoreServices) {
        this.teilerCoreServices = teilerCoreServices;
    }

    @GetMapping(TeilerCoreConst.INFO_PATH)
    public ResponseEntity<String> info() {
        return new ResponseEntity<>(projectVersion, HttpStatus.OK);
    }

    @GetMapping(TeilerCoreConst.SERVICE_URL_PATH)
    public ResponseEntity<String> getServiceUrl(@PathVariable String serviceName) {

        String responseUrl = switch (serviceName) {
            case TeilerCoreConst.NNGM_SERVICE -> teilerCoreServices.getnNgmUrl();
            case TeilerCoreConst.QUALITY_REPORT_SERVICE -> teilerCoreServices.getQualityReportUrl();
            default -> null;
        };

        return (responseUrl != null) ? new ResponseEntity<>(responseUrl, HttpStatus.OK)
                : ResponseEntity.notFound().build();

    }


}
