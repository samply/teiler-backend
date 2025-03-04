package de.samply.teiler.config;

import de.samply.teiler.backend.TeilerBackendConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class ConfigBlocksConfigurator {

    private final static Logger logger = LoggerFactory.getLogger(ConfigBlocksConfigurator.class);
    private ConfigBlock[] configBlocks;

    public ConfigBlocksConfigurator(@Value(TeilerBackendConst.CONFIG_ENV_VAR_PATH_SV) String configEnvVarPath,
                                    @Autowired ResourceLoader resourceLoader) {
        parseConfigEnvVarFile(resourceLoader, configEnvVarPath);
    }

    private void parseConfigEnvVarFile(ResourceLoader resourceLoader, String configEnvVarPath) {
        try (Stream<String> lines = fetchFileLines(resourceLoader, configEnvVarPath)) {
            generateConfigBlocks(lines);
        } catch (IOException e) {
            logger.error("Error reading teiler config file", e);
            throw new RuntimeException(e);
        }
    }

    private void generateConfigBlocks(Stream<String> lines) {
        logger.info("Generating config blocks...");
        ConfigBuilder configBuilder = new ConfigBuilder();
        lines.forEach(line -> configBuilder.addLine(line));
        configBlocks = configBuilder.build();
        logger.info("Config blocks generated.");
    }

    private Stream<String> fetchFileLines(ResourceLoader resourceLoader, String configEnvVarPath) throws IOException {
        return Files.lines(fetchPath(resourceLoader, configEnvVarPath));
    }

    private Path fetchPath(ResourceLoader resourceLoader, String configEnvVarFilename) throws IOException {

        Path resultPath = Paths.get(configEnvVarFilename);
        if (resultPath != null && Files.isReadable(resultPath)){
            logger.info("Teiler Config File is not readable");
        }
        if (resultPath == null || Files.notExists(resultPath)) {
            resultPath = resourceLoader.getResource("classpath:/" + configEnvVarFilename).getFile().toPath();
        }
        return resultPath;
    }

    public ConfigBlock[] getConfigBlocks() {
        return configBlocks;
    }

}
