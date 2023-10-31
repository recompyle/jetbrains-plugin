package org.recompyle.services.breakpoint;

public class BreakPointPosition {
    private String filePath;
    private Integer line;
    private Integer column;

    public BreakPointPosition(String filePath, Integer line, Integer intValue2) {
        this.filePath = filePath;
        this.line = line;
        this.column = intValue2;
    }

    public String getStringValue() {
        return this.filePath;
    }

    public Integer getLine() {
        return this.line;
    }

    public Integer getColumn() {
        return this.column;
    }
}
