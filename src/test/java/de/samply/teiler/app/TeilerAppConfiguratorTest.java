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
    private ConfigurableApplicationContext teilerCoreContext1;

    @BeforeAll
    static void beforeAll() {
        initializeTeilerApp1();
        initializeTeilerApp2();
        initializeTeilerApp3();
    }

    private static void initializeTeilerApp1() {

        teilerApp1 = new TeilerApp();

        teilerApp1.setName("name1");
        teilerApp1.setTitle("title1");
        teilerApp1.setDescription("description1");
        teilerApp1.setExternLink(false);
        teilerApp1.setSourceLink("sourceLink1");
        TeilerAppRole[] roles = {TeilerAppRole.TEILER_USER};
        teilerApp1.setRoles(roles);

        //TODO:
        //teilerApp2.setRouterLink("name1");
        //teilerApp2.setSingleSpaLink("@samply/de/name1");

    }

    private static void initializeTeilerApp2() {

        teilerApp2 = new TeilerApp();

        teilerApp2.setName("name2");
        teilerApp2.setTitle("title2");
        teilerApp2.setDescription("description2");
        teilerApp2.setExternLink(false);
        teilerApp2.setSourceLink("sourceLink2");
        TeilerAppRole[] roles = {TeilerAppRole.TEILER_USER, TeilerAppRole.TEILER_PUBLIC};
        teilerApp2.setRoles(roles);

        //TODO:
        //teilerApp2.setRouterLink("name2");
        //teilerApp2.setSingleSpaLink("@samply/de/name2");

    }

    private static void initializeTeilerApp3() {

        teilerApp3 = new TeilerApp();

        teilerApp3.setName("name2" + languge2);
        teilerApp3.setTitle("title2" + languge2);
        teilerApp3.setDescription("description2" + languge2);
        teilerApp3.setExternLink(false);
        teilerApp3.setSourceLink("sourceLink2" + languge2);
        TeilerAppRole[] roles = {TeilerAppRole.TEILER_USER, TeilerAppRole.TEILER_PUBLIC};
        teilerApp3.setRoles(roles);

        //TODO:
        //teilerApp3.setRouterLink("name2");
        //teilerApp3.setSingleSpaLink("@samply/de/name2");

    }

    private SpringApplication createTeilerCore() {
        return new SpringApplicationBuilder(TeilerCoreApplication.class)
                .properties(TeilerCoreConst.DEFAULT_LANGUAGE + '=' + defaultLanguage,
                        "APPLICATION_PORT=" + APPLICATION_PORT,
                        "TAPP1_NAME=" + teilerApp1.getName(),
                        "TAPP1_TITLE=" + teilerApp1.getTitle(),
                        "TAPP1_DESCRIPTION=" + teilerApp1.getDescription(),
                        "TAPP1_ISEXTERNALLINK=" + teilerApp1.isExternLink(),
                        "TAPP1_SOURCELINK=" + teilerApp1.getSourceLink(),
                        "TAPP1_ROLES=" + String.join(",", Arrays.stream(teilerApp1.getRoles()).map(TeilerAppRole::toString).toArray(String[]::new)),
                        "TAPP2_NAME=" + teilerApp2.getName(),
                        "TAPP2_TITLE=" + teilerApp2.getTitle(),
                        "TAPP2_DESCRIPTION=" + teilerApp2.getDescription(),
                        "TAPP2_ISEXTERNALLINK=" + teilerApp2.isExternLink(),
                        "TAPP2_SOURCELINK=" + teilerApp2.getSourceLink(),
                        "TAPP2_ROLES=" + String.join(",", Arrays.stream(teilerApp2.getRoles()).map(TeilerAppRole::toString).toArray(String[]::new)),
                        "TAPP2_EN_NAME=" + teilerApp3.getName(),
                        "TAPP2_EN_TITLE=" + teilerApp3.getTitle(),
                        "TAPP2_EN_DESCRIPTION=" + teilerApp3.getDescription(),
                        "TAPP2_EN_SOURCELINK=" + teilerApp3.getSourceLink()
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
        String[] languages = {defaultLanguage, languge2};
        Arrays.stream(languages).forEach(language ->
                teilerAppConfigurator.getTeilerApps(language).forEach(teilerApp -> {
                    if (teilerApp.getName().equals(teilerApp1.getName())) {
                        generatedTeilerApp1.set(teilerApp);
                    } else if (teilerApp.getName().equals(teilerApp2.getName())) {
                        generatedTeilerApp2.set(teilerApp);
                    } else if (teilerApp.getName().equals(teilerApp3.getName())) {
                        generatedTeilerApp3.set(teilerApp);
                    }
                }));

        assertTrue(Objects.equals(generatedTeilerApp1.get(), teilerApp1));
        assertTrue(Objects.equals(generatedTeilerApp2.get(), teilerApp2));
        assertTrue(Objects.equals(generatedTeilerApp3.get(), teilerApp3));

    }

}
