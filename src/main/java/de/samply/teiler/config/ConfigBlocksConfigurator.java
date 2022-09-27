package de.samply.teiler.config;

import de.samply.teiler.core.TeilerCoreConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

@Component
public class ConfigBlocksConfigurator {

    private ConfigBlock[] configBlocks;

    public ConfigBlocksConfigurator(@Value(TeilerCoreConst.CONFIG_ENV_VAR_FILENAME_SV) String configEnvVarFilename) {
        parseConfigEnvVarFile(configEnvVarFilename);
    }

    private void parseConfigEnvVarFile(String configEnvVarFilename){
        try(Stream<String> lines = fetchFileLines(configEnvVarFilename)){
            generateConfigBlocks(lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void generateConfigBlocks(Stream<String> lines){
        ConfigBuilder configBuilder = new ConfigBuilder();
        lines.forEach(line -> configBuilder.addLine(line));
        configBlocks = configBuilder.build();
    }

    private Stream<String> fetchFileLines (String configEnvVarFilename) throws IOException {
         return Files.lines(ResourceUtils.getFile("classpath:" + configEnvVarFilename).toPath());
    }

    public ConfigBlock[] getConfigBlocks() {
        return configBlocks;
    }

}
