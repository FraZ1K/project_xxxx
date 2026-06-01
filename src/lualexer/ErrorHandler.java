package lualexer;

import java.util.*;

public class ErrorHandler {
    private final List<String> errors = new ArrayList<>();
    private String source = "";

    public void setSource(String source, String fileName) {
        this.source = source;
    }

    public void addError(String message, int line, int column, String fileName) {
        String error = message + " в " + fileName + ":" + line + ":" + column;
        errors.add(error);
        System.out.println("  ❌ ОШИБКА: " + error);

        if (!source.isEmpty()) {
            String[] lines = source.split("\n");
            if (line <= lines.length && line > 0) {
                System.out.println("     " + line + " | " + lines[line - 1]);
                System.out.println("       " + " ".repeat(Math.max(0, column - 1)) + "^");
            }
        }
    }

    public boolean hasErrors() { return !errors.isEmpty(); }
    public void printErrors() {
        if (!errors.isEmpty()) {
            System.out.println("\n📊 Всего ошибок: " + errors.size());
        }
    }
    public void clear() { errors.clear(); source = ""; }
}