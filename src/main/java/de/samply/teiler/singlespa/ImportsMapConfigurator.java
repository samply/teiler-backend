package de.samply.teiler.singlespa;

import de.samply.teiler.app.TeilerAppConfigurator;
import de.samply.teiler.backend.TeilerBackendConst;
import de.samply.teiler.ui.TeilerDashboardConfigurator;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ImportsMapConfigurator {

    private final static Logger logger = LoggerFactory.getLogger(ImportsMapConfigurator.class);
    private JSONObject importsMaps = new JSONObject();

    public ImportsMapConfigurator(@Value(TeilerBackendConst.TEILER_ORCHESTRATOR_URL_SV) String teilerOrchestratorUrl,
                                  @Autowired SingleSpaLinkGenerator singleSpaLinkGenerator,
                                  @Autowired TeilerAppConfigurator teilerAppConfigurator,
                                  @Autowired TeilerDashboardConfigurator teilerDashboardConfigurator) {

        logger.info("Initialize imports map configurator...");
        JSONObject imports = new JSONObject();

        imports.put(
                singleSpaLinkGenerator.generateSingleSpaLink(TeilerBackendConst.SINGLE_SPA_ROOT_CONFIG),
                singleSpaLinkGenerator.generateSingleSpaSourceLinkForRootConfig(teilerOrchestratorUrl));

        Arrays.stream(teilerDashboardConfigurator.getTeilerDashboardLanguages()).forEach(language -> imports.put(
                singleSpaLinkGenerator.generateSingleSpaLink(TeilerBackendConst.TEILER_DASHBOARD_APP_NAME, language),
                singleSpaLinkGenerator.generateSingleSpaSourceLink(teilerDashboardConfigurator.getTeilerDashboardUrl(language))));

        teilerAppConfigurator.getTeilerApps().stream()
                .filter(teilerApp -> !teilerApp.getExternLink())
                .forEach(teilerApp -> imports.put(
                        teilerApp.getSingleSpaLink(),
                        singleSpaLinkGenerator.generateSingleSpaSourceLink(teilerApp.getSourceUrl(), teilerApp.getSingleSpaMainJs())));

        importsMaps.put(TeilerBackendConst.SINGLE_SPA_IMPORTS, imports);
        logger.info("Imports map configurated.");

    }

    public JSONObject getImportsMaps() {
        return importsMaps;
    }

}
