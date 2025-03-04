package de.samply.teiler.config;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public enum ConfigLineType {
    BLOCK_COMMENT("####"),
    TITLE("###"),
    TITLE_COMMENT("##"),
    VARIABLE_COMMENT("#"),
    VARIABLE(""),
    EMPTY_LINE("");

    private final String head;

    ConfigLineType(String head) {
        this.head = head;
    }

    public static ConfigLineType getConfigLineType(String line) {

        ConfigLineType configLineTypeResult = null;

        for (ConfigLineType configLineType : values()){
            if (configLineType != VARIABLE && configLineType != EMPTY_LINE && line.startsWith(configLineType.head)){
                configLineTypeResult = configLineType;
                break;
            }
        }
        if (configLineTypeResult == null){
            configLineTypeResult = (line.length() == 0) ? EMPTY_LINE : VARIABLE;
        }

        return configLineTypeResult;

    }

    public static String extractConfigLineType(String line, ConfigLineType configLineType) {
        return line.substring(configLineType.head.length());
    }


}
