package de.samply.teiler.core;

import de.samply.teiler.utils.ProjectVersion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeilerCoreController {

    private final String projectVersion = ProjectVersion.getProjectVersion();

    @GetMapping(TeilerCoreConst.INFO_URL)
    public ResponseEntity<String> info() {
        return new ResponseEntity<>(projectVersion, HttpStatus.OK);
    }

}
