package de.samply.teiler.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigBlocksConfiguratorTest {

    @Test
    void getConfigBlocks() {
        ConfigBlocksConfigurator configBlocksConfigurator = new ConfigBlocksConfigurator("ccp.conf");
        ConfigBlock[] configBlocks = configBlocksConfigurator.getConfigBlocks();
        assertEquals(10, configBlocks.length);
        //TODO: complete tests
    }

}
