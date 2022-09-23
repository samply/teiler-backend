package de.samply.teiler.app;

import de.samply.teiler.core.TeilerCoreApplication;
import de.samply.teiler.core.TeilerCoreConst;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

class TeilerAppConfiguratorTest {

    private static String defaultLanguage = "DE";
    private static String languge2 = "EN";
    private static final String APPLICATION_PORT = "8383";
    private static TeilerApp teilerApp1;
    private static TeilerApp teilerApp2;
    private static TeilerApp teilerApp3;
    private static TeilerApp teilerApp4;
    private ConfigurableApplicationContext teilerCoreContext1;

    @BeforeAll
    static void beforeAll() {
        teilerApp1 = initializeTeilerApp1();
        teilerApp2 = initializeTeilerApp2();
        teilerApp3 = initializeTeilerApp3();
        teilerApp4 = initializeTeilerApp4();
    }

    private static TeilerApp initializeTeilerApp1() {

        TeilerApp teilerApp = new TeilerApp();

        teilerApp.setName("name1");
        teilerApp.setTitle("title1");
        teilerApp.setDescription("description1");
        teilerApp.setExternLink(false);
        teilerApp.setSourceLink("sourceLink1");
        TeilerAppRole[] roles = {TeilerAppRole.TEILER_USER};
        teilerApp.setRoles(roles);
        teilerApp.setRouterLink("de/name1");
        teilerApp.setSingleSpaLink("@samply/de/name1");
        teilerApp.setActivated(true);

        return teilerApp;

    }

    private static TeilerApp initializeTeilerApp2() {

        TeilerApp teilerApp = new TeilerApp();

        teilerApp.setName("name2");
        teilerApp.setTitle("title2");
        teilerApp.setDescription("description2");
        teilerApp.setExternLink(false);
        teilerApp.setSourceLink("sourceLink2");
        TeilerAppRole[] roles = {TeilerAppRole.TEILER_USER, TeilerAppRole.TEILER_PUBLIC};
        teilerApp.setRoles(roles);
        teilerApp.setRouterLink("de/name2");
        teilerApp.setSingleSpaLink("@samply/de/name2");
        teilerApp.setActivated(false);
        teilerApp.setOrder(1);
        teilerApp.setIconClass("bi bi-emoji-sunglasses");
        teilerApp.setIconSourceUrl("iconurl2");

        return teilerApp;

    }

    private static TeilerApp initializeTeilerApp3() {

        TeilerApp teilerApp = new TeilerApp();

        teilerApp.setName("name2");
        teilerApp.setTitle("title2" + languge2);
        teilerApp.setDescription("description2" + languge2);
        teilerApp.setExternLink(false);
        teilerApp.setSourceLink("sourceLink2" + languge2);
        TeilerAppRole[] roles = {TeilerAppRole.TEILER_USER, TeilerAppRole.TEILER_PUBLIC};
        teilerApp.setRoles(roles);
        teilerApp.setRouterLink("en/name2");
        teilerApp.setSingleSpaLink("@samply/en/name2");
        teilerApp.setActivated(false);
        teilerApp.setOrder(1);
        teilerApp.setIconClass("bi bi-emoji-sunglasses");
        teilerApp.setIconSourceUrl("iconurl2");


        return teilerApp;

    }

    private static TeilerApp initializeTeilerApp4() {

        TeilerApp teilerApp = new TeilerApp();

        teilerApp.setName("name1");
        teilerApp.setTitle("title1");
        teilerApp.setDescription("description1");
        teilerApp.setExternLink(false);
        teilerApp.setSourceLink("sourceLink1");
        TeilerAppRole[] roles = {TeilerAppRole.TEILER_USER};
        teilerApp.setRoles(roles);
        teilerApp.setRouterLink("en/name1");
        teilerApp.setSingleSpaLink("@samply/en/name1");
        teilerApp.setActivated(true);

        return teilerApp;

    }

    private SpringApplication createTeilerCore() {
        return new SpringApplicationBuilder(TeilerCoreApplication.class)
                .properties(TeilerCoreConst.DEFAULT_LANGUAGE + '=' + defaultLanguage,
                        "APPLICATION_PORT=" + APPLICATION_PORT,
                        "TEILER_APP1_NAME=" + teilerApp1.getName(),
                        "TEILER_APP1_TITLE=" + teilerApp1.getTitle(),
                        "TEILER_APP1_DESCRIPTION=" + teilerApp1.getDescription(),
                        "TEILER_APP1_ISEXTERNALLINK=" + teilerApp1.getExternLink(),
                        "TEILER_APP1_SOURCELINK=" + teilerApp1.getSourceLink(),
                        "TEILER_APP1_ROLES=" + String.join(",", Arrays.stream(teilerApp1.getRoles()).map(TeilerAppRole::toString).toArray(String[]::new)),
                        "TEILER_APP2_NAME=" + teilerApp2.getName(),
                        "TEILER_APP2_TITLE=" + teilerApp2.getTitle(),
                        "TEILER_APP2_DESCRIPTION=" + teilerApp2.getDescription(),
                        "TEILER_APP2_ISEXTERNALLINK=" + teilerApp2.getExternLink(),
                        "TEILER_APP2_SOURCELINK=" + teilerApp2.getSourceLink(),
                        "TEILER_APP2_ROLES=" + String.join(",", Arrays.stream(teilerApp2.getRoles()).map(TeilerAppRole::toString).toArray(String[]::new)),
                        "TEILER_APP2_EN_NAME=" + teilerApp3.getName(),
                        "TEILER_APP2_EN_TITLE=" + teilerApp3.getTitle(),
                        "TEILER_APP2_EN_DESCRIPTION=" + teilerApp3.getDescription(),
                        "TEILER_APP2_EN_SOURCELINK=" + teilerApp3.getSourceLink(),
                        "TEILER_APP2_ISACTIVATED=" + teilerApp3.getActivated(),
                        "TEILER_APP2_ICONCLASS=" + teilerApp3.getIconClass(),
                        "TEILER_APP2_ICONSOURCEURL=" + teilerApp3.getIconSourceUrl(),
                        "TEILER_APP2_ORDER=" + teilerApp3.getOrder(),
                        "TEILER_UI_EN_URL=" + "asdf",
                        "TEILER_UI_DE_URL=" + "fdsa"
                ).build();
    }


    @BeforeEach
    void setUp() {
        teilerCoreContext1 = createTeilerCore().run();
    }

    @AfterEach
    void tearDown() {
        teilerCoreContext1.close();
    }

    @Test
    void getTeilerApps() {

        TeilerAppConfigurator teilerAppConfigurator = teilerCoreContext1.getBean(TeilerAppConfigurator.class);
        AtomicReference<TeilerApp> generatedTeilerApp1 = new AtomicReference<>();
        AtomicReference<TeilerApp> generatedTeilerApp2 = new AtomicReference<>();
        AtomicReference<TeilerApp> generatedTeilerApp3 = new AtomicReference<>();
        AtomicReference<TeilerApp> generatedTeilerApp4 = new AtomicReference<>();
        String[] languages = {defaultLanguage, languge2};
        Arrays.stream(languages).forEach(language ->
                teilerAppConfigurator.getTeilerApps(language).forEach(teilerApp -> {
                    if (teilerApp.getRouterLink().equals(teilerApp1.getRouterLink())) {
                        generatedTeilerApp1.set(teilerApp);
                    } else if (teilerApp.getRouterLink().equals(teilerApp2.getRouterLink())) {
                        generatedTeilerApp2.set(teilerApp);
                    } else if (teilerApp.getRouterLink().equals(teilerApp3.getRouterLink())) {
                        generatedTeilerApp3.set(teilerApp);
                    } else if (teilerApp.getRouterLink().equals(teilerApp4.getRouterLink())) {
                        generatedTeilerApp4.set(teilerApp);
                    }
                }));

        assertTrue(Objects.equals(generatedTeilerApp1.get(), teilerApp1));
        assertTrue(Objects.equals(generatedTeilerApp2.get(), teilerApp2));
        assertTrue(Objects.equals(generatedTeilerApp3.get(), teilerApp3));
        assertTrue(Objects.equals(generatedTeilerApp4.get(), teilerApp4));

    }

}
