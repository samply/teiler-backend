package de.samply.teiler.config;

import java.util.ArrayList;
import java.util.List;

import static de.samply.teiler.config.ConfigLineType.*;

public class ConfigBlock {

    private List<String> blockComment = new ArrayList<>();
    private String title;
    private List<String> titleComment = new ArrayList<>();
    private List<ConfigVariable> variables = new ArrayList<>();

    public List<String> getBlockComment() {
        return blockComment;
    }

    public void addBlockCommentLine(String line) {
        this.blockComment.add(extractConfigLineType(line, BLOCK_COMMENT));
    }

    public void addBlockCommentEmptyLine() {
        this.blockComment.add("");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String line) {
        this.title = extractConfigLineType(line, TITLE);
    }

    public List<String> getTitleComment() {
        return titleComment;
    }

    public void addTitleCommentLine(String line) {
        this.titleComment.add(extractConfigLineType(line, TITLE_COMMENT));
    }

    public void addTitleCommentEmtpyLine() {
        this.titleComment.add("");
    }

    public List<ConfigVariable> getVariables() {
        return variables;
    }

    public void addVariable(ConfigVariable variable) {
        this.variables.add(variable);
    }

}
