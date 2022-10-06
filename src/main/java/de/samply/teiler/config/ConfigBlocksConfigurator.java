package de.samply.teiler.config;

import de.samply.teiler.core.TeilerCoreConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class ConfigBlocksConfigurator {

    private ConfigBlock[] configBlocks;

    public ConfigBlocksConfigurator(@Value(TeilerCoreConst.CONFIG_ENV_VAR_PATH_SV) String configEnvVarPath,
                                    @Autowired ResourceLoader resourceLoader) {
        parseConfigEnvVarFile(resourceLoader, configEnvVarPath);
    }

    private void parseConfigEnvVarFile(ResourceLoader resourceLoader, String configEnvVarPath) {
        try (Stream<String> lines = fetchFileLines(resourceLoader, configEnvVarPath)) {
            generateConfigBlocks(lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateConfigBlocks(Stream<String> lines) {
        ConfigBuilder configBuilder = new ConfigBuilder();
        lines.forEach(line -> configBuilder.addLine(line));
        configBlocks = configBuilder.build();
    }

    private Stream<String> fetchFileLines(ResourceLoader resourceLoader, String configEnvVarPath) throws IOException {
        return Files.lines(fetchPath(resourceLoader, configEnvVarPath));
    }

    private Path fetchPath(ResourceLoader resourceLoader, String configEnvVarFilename) throws IOException {

        Path resultPath = Paths.get(configEnvVarFilename);
        if (resultPath == null || Files.notExists(resultPath)) {
            resultPath = resourceLoader.getResource("classpath:/" + configEnvVarFilename).getFile().toPath();
        }
        return resultPath;
    }

    public ConfigBlock[] getConfigBlocks() {
        return configBlocks;
    }

}
