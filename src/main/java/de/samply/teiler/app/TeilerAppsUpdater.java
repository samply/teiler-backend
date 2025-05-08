package de.samply.teiler.app;

import de.samply.teiler.backend.TeilerBackendConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * This component is responsible for updating Teiler app configurations on a scheduled basis.
 * <p>
 * It uses two separate schedules:
 * - During the first 5 minutes after the application starts, the update runs every 30 seconds.
 * - After 5 minutes, it switches to a regular update schedule defined in {@link TeilerBackendConst#TEILER_CONFIG_UPDATER_CRON_SV}.
 * <p>
 * The runtime condition (`startTime`) ensures only one of the two scheduled methods performs updates at any given time.
 */
@Component
public class TeilerAppsUpdater {

    private final TeilerAppConfigurator teilerAppConfigurator;
    private final Instant startTime;

    /**
     * Initializes the updater and records the application start time.
     *
     * @param teilerAppConfigurator dependency that performs the actual update logic.
     */
    public TeilerAppsUpdater(@Autowired TeilerAppConfigurator teilerAppConfigurator) {
        this.teilerAppConfigurator = teilerAppConfigurator;
        this.startTime = Instant.now();
    }

    /**
     * Scheduled task that runs every 30 seconds **only** during the first 5 minutes after application startup.
     * It delegates to {@link TeilerAppConfigurator#updateLanguageAppIdTeilerAppMap()} if the condition is met.
     */
    @Scheduled(cron = TeilerBackendConst.UPDATE_APPS_IN_THE_FIRST_PERIOD_CRON_EXPRESSION)
    public void updateTeilerAppsInTheFirstFiveMinutes() {
        if (Duration.between(startTime, Instant.now()).toMinutes() < TeilerBackendConst.FIRST_PERIOD_FOR_UPDATING_APPS_FREQUENTLY_IN_MINUTES) {
            teilerAppConfigurator.updateLanguageAppIdTeilerAppMap();
        }
    }

    /**
     * Scheduled task that runs based on the regular cron expression defined in constants.
     * This method is active only **after** the first 5 minutes since application startup.
     */
    @Scheduled(cron = TeilerBackendConst.TEILER_CONFIG_UPDATER_CRON_SV)
    public void updateTeilerApps() {
        if (Duration.between(startTime, Instant.now()).toMinutes() >= TeilerBackendConst.FIRST_PERIOD_FOR_UPDATING_APPS_FREQUENTLY_IN_MINUTES) {
            teilerAppConfigurator.updateLanguageAppIdTeilerAppMap();
        }
    }

}