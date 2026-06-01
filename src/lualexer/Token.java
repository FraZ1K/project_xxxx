package lualexer;

public class Token {
    private final TokenType type;
    private final String lexeme;
    private final int line;
    private final int column;
    private final String fileName;

    public Token(TokenType type, String lexeme, int line, int column, String fileName) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
        this.fileName = fileName;
    }

    public TokenType getType() { return type; }
    public String getLexeme() { return lexeme; }
    public int getLine() { return line; }
    public int getColumn() { return column; }
    public String getFileName() { return fileName; }

    @Override
    public String toString() {
        return String.format("[%s] '%s' (%s:%d:%d)", type, lexeme, fileName, line, column);
    }
}