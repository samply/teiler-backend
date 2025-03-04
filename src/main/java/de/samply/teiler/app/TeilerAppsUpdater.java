package de.samply.teiler.app;

import de.samply.teiler.backend.TeilerBackendConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TeilerAppsUpdater {

    private TeilerAppConfigurator teilerAppConfigurator;

    public TeilerAppsUpdater(@Autowired TeilerAppConfigurator teilerAppConfigurator) {
        this.teilerAppConfigurator = teilerAppConfigurator;
    }

    @Scheduled(cron = TeilerBackendConst.TEILER_CONFIG_UPDATER_CRON_SV)
    public void updateTeilerApps(){
        this.teilerAppConfigurator.updateLanguageAppIdTeilerAppMap();
    }

}
