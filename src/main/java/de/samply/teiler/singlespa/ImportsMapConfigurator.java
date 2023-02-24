package de.samply.teiler.singlespa;

import de.samply.teiler.app.TeilerAppConfigurator;
import de.samply.teiler.core.TeilerCoreConst;
import de.samply.teiler.ui.TeilerUiConfigurator;
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

    public ImportsMapConfigurator(@Value(TeilerCoreConst.TEILER_ROOT_CONFIG_URL_SV) String teilerRootConfigUrl,
                                  @Autowired SingleSpaLinkGenerator singleSpaLinkGenerator,
                                  @Autowired TeilerAppConfigurator teilerAppConfigurator,
                                  @Autowired TeilerUiConfigurator teilerUiConfigurator) {

        logger.info("Initialize imports map configurator...");
        JSONObject imports = new JSONObject();

        imports.put(
                singleSpaLinkGenerator.generateSingleSpaLink(TeilerCoreConst.SINGLE_SPA_ROOT_CONFIG),
                singleSpaLinkGenerator.generateSingleSpaSourceLinkForRootConfig(teilerRootConfigUrl));

        Arrays.stream(teilerUiConfigurator.getTeilerUiLanguages()).forEach(language -> imports.put(
                singleSpaLinkGenerator.generateSingleSpaLink(TeilerCoreConst.TEILER_UI_APP_NAME, language),
                singleSpaLinkGenerator.generateSingleSpaSourceLink(teilerUiConfigurator.getTeilerUiUrl(language))));

        teilerAppConfigurator.getTeilerApps().stream()
                .filter(teilerApp -> !teilerApp.getExternLink())
                .forEach(teilerApp -> imports.put(
                        teilerApp.getSingleSpaLink(),
                        singleSpaLinkGenerator.generateSingleSpaSourceLink(teilerApp.getSourceUrl(), teilerApp.getSingleSpaMainJs())));

        importsMaps.put(TeilerCoreConst.SINGLE_SPA_IMPORTS, imports);
        logger.info("Imports map configurated.");

    }

    public JSONObject getImportsMaps() {
        return importsMaps;
    }

}
