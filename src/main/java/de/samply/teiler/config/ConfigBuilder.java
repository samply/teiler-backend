package de.samply.teiler.config;

import java.util.ArrayList;
import java.util.List;

import static de.samply.teiler.config.ConfigLineType.*;

public class ConfigBuilder {

    private List<ConfigBlock> blocks = new ArrayList<>();
    private ConfigBlock currentConfigBlock;
    private ConfigVariable currentConfigVariable;

    private ConfigLineType lastConfigLineType = BLOCK_COMMENT;

    public ConfigBuilder() {
        addNewBlock();
    }

    private void addNewBlock(){
        currentConfigBlock = new ConfigBlock();
        currentConfigVariable = null;
        blocks.add(currentConfigBlock);
    }

    private void addNewVariable(){
        currentConfigVariable = new ConfigVariable();
        currentConfigBlock.addVariable(currentConfigVariable);
    }

    public ConfigBuilder addLine(String line){

        ConfigLineType configLineType = ConfigLineType.getConfigLineType(line);
        switch (configLineType) {
            case BLOCK_COMMENT -> {
                if (lastConfigLineType != BLOCK_COMMENT){
                    addNewBlock();
                }
                currentConfigBlock.addBlockCommentLine(line);
            }
            case TITLE -> {
                addNewBlock();
                currentConfigBlock.setTitle(line);
            }
            case TITLE_COMMENT -> {
                currentConfigBlock.addTitleCommentLine(line);
            }
            case VARIABLE_COMMENT -> {
                if (currentConfigVariable == null){
                    addNewVariable();
                }
                currentConfigVariable.addVariableCommentLine(line);
            }
            case VARIABLE -> {
                if (currentConfigVariable == null || currentConfigVariable.getVariable() != null){
                    addNewVariable();
                }
                currentConfigVariable.setVariableAndValue(line);
                currentConfigVariable = null;
            }
            case EMPTY_LINE -> {
                addEmptyLine();
            }
        }
        if (configLineType != EMPTY_LINE){
            lastConfigLineType = configLineType;
        }

        return this;
    }

    private void addEmptyLine(){
        switch (lastConfigLineType) {

            case BLOCK_COMMENT -> {
                currentConfigBlock.addBlockCommentEmptyLine();
            }
            case TITLE -> {
                currentConfigBlock.addTitleCommentEmtpyLine();
            }
            case TITLE_COMMENT -> {
                currentConfigBlock.addTitleCommentEmtpyLine();
            }
            case VARIABLE_COMMENT -> {
                currentConfigVariable.addVariableCommentEmptyLine();
            }

        }
    }

    public ConfigBlock[] build() {
        return blocks.toArray(ConfigBlock[]::new);
    }

}
