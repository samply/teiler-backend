package de.samply.teiler.config;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigBlocksConfiguratorTest {

    @Test
    void getConfigBlocks() {
        ConfigBlocksConfigurator configBlocksConfigurator = new ConfigBlocksConfigurator("ccp.conf", new DefaultResourceLoader());
        ConfigBlock[] configBlocks = configBlocksConfigurator.getConfigBlocks();
        assertEquals(10, configBlocks.length);
        //TODO: complete tests
    }

    @Test
    void getConfigBlocks2() {
        ConfigBlocksConfigurator configBlocksConfigurator = new ConfigBlocksConfigurator("./src/main/resources/ccp.conf", new DefaultResourceLoader());
        ConfigBlock[] configBlocks = configBlocksConfigurator.getConfigBlocks();
        assertEquals(10, configBlocks.length);
        //TODO: complete tests
    }

}
