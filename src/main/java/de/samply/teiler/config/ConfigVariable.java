package de.samply.teiler.config;

import java.util.ArrayList;
import java.util.List;

import static de.samply.teiler.config.ConfigLineType.*;

public class ConfigVariable {

    private String variable;
    private String value;
    private List<String> variableComment = new ArrayList<>();

    public String getVariable() {
        return variable;
    }

    public String getValue() {
        return value;
    }

    public void setVariableAndValue(String line) {

        String variableAndValue = extractConfigLineType(line, VARIABLE);
        String[] variableAndValuesArray = variableAndValue.split("=");
        if (variableAndValuesArray.length >= 2){
            value = variableAndValuesArray[1];
        }
        if (variableAndValuesArray.length >= 1){
            variable = variableAndValuesArray[0];
        }

    }

    public List<String> getVariableComment() {
        return variableComment;
    }

    public void addVariableCommentLine(String line) {
        this.variableComment.add(extractConfigLineType(line, VARIABLE_COMMENT));
    }

    public void addVariableCommentEmptyLine(){
        this.variableComment.add("");
    }

}
