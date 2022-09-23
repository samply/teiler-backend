package de.samply.teiler.root;

import de.samply.teiler.app.TeilerAppConfigurator;
import de.samply.teiler.core.TeilerCoreConst;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImportsMapConfigurator {

    private JSONObject importsMaps = new JSONObject();

    public ImportsMapConfigurator(@Autowired TeilerAppConfigurator teilerAppConfigurator) {

        JSONObject imports = new JSONObject();
        teilerAppConfigurator.getTeilerApps().forEach(teilerApp -> imports.put(teilerApp.getSingleSpaLink(), teilerApp.getSourceLink()));
        importsMaps.put(TeilerCoreConst.SINGLE_SPA_IMPORTS, imports);

    }

    public JSONObject getImportsMaps(){
        return importsMaps;
    }

}
