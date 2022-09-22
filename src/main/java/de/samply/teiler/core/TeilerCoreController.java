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

    @GetMapping(TeilerCoreConst.INFO_PATH)
    public ResponseEntity<String> info() {
        return new ResponseEntity<>(projectVersion, HttpStatus.OK);
    }



}
