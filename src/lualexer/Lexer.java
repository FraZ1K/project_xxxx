package lualexer;

import java.util.*;

public class Lexer {
    private final String source;
    private final String fileName;
    private final ErrorHandler errorHandler;
    private int pos = 0, line = 1, col = 1;
    private char ch;
    private final List<Token> tokens = new ArrayList<>();
    private boolean hasError = false;

    private static final Set<String> KEYWORDS = Set.of(
            "and", "break", "do", "else", "elseif", "end", "false", "for", "function",
            "goto", "if", "in", "local", "nil", "not", "or", "repeat", "return",
            "then", "true", "until", "while"
    );

    public Lexer(String source, String fileName, ErrorHandler errorHandler) {
        this.source = source;
        this.fileName = fileName;
        this.errorHandler = errorHandler;
        nextChar();
    }

    private void nextChar() {
        if (pos < source.length()) {
            ch = source.charAt(pos++);
            if (ch == '\n') {
                line++;
                col = 1;
            } else if (ch == '\r') {
                // Пропускаем \r, строка увеличится на следующем \n
                col++;
            } else {
                col++;
            }
        } else {
            ch = '\0';
        }
    }

    private char peek() {
        return pos < source.length() ? source.charAt(pos) : '\0';
    }

    private void error(String msg) {
        errorHandler.addError(msg, line, col, fileName);
        hasError = true;
    }

    public List<Token> tokenize() {
        while (ch != '\0' && !hasError) {
            skipWhitespace();
            if (ch == '\0') break;

            if (ch == '-' && peek() == '-') {
                handleComment();
            }
            else if (ch == '"' || ch == '\'') {
                handleShortString();
            }
            else if (ch == '[' && (peek() == '[' || peek() == '=')) {
                handleLongString();
            }
            else if (Character.isDigit(ch)) {
                handleNumber();
            }
            else if (Character.isLetter(ch) || ch == '_') {
                handleIdentifierOrKeyword();
            }
            else {
                handleOperator();
            }
        }
        tokens.add(new Token(TokenType.EOF, "", line, col, fileName));
        return hasError ? Collections.emptyList() : tokens;
    }

    private void skipWhitespace() {
        while (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n') {
            nextChar();
        }
    }

    private void handleComment() {
        nextChar(); // пропускаем первый '-'
        nextChar(); // пропускаем второй '-'

        if (ch == '[' && (peek() == '[' || peek() == '=')) {
            // Длинный комментарий
            int level = getLongBracketLevel();
            if (level >= 0) {
                while (ch != '\0') {
                    if (ch == ']' && checkClosingLongBracket(level)) {
                        for (int i = 0; i < getClosingLongBracket(level).length() - 1 && ch != '\0'; i++) {
                            nextChar();
                        }
                        nextChar();
                        break;
                    }
                    nextChar();
                }
            }
        } else {
            // Короткий комментарий до конца строки
            while (ch != '\0' && ch != '\n' && ch != '\r') {
                nextChar();
            }
        }
    }

    private int getLongBracketLevel() {
        int level = 0;
        if (ch == '[') {
            nextChar();
            while (ch == '=') {
                level++;
                nextChar();
            }
            if (ch == '[') {
                nextChar();
                return level;
            }
        }
        return -1;
    }

    private String getClosingLongBracket(int level) {
        StringBuilder sb = new StringBuilder("]");
        for (int i = 0; i < level; i++) sb.append("=");
        sb.append("]");
        return sb.toString();
    }

    private boolean checkClosingLongBracket(int level) {
        if (ch != ']') return false;
        int found = 0;
        char c = peek();
        while (c == '=') {
            found++;
            c = pos + found < source.length() ? source.charAt(pos + found) : '\0';
        }
        return c == ']' && found == level;
    }

    private void handleShortString() {
        int startLine = line, startCol = col;
        char quote = ch;
        StringBuilder str = new StringBuilder().append(ch);
        nextChar();

        while (ch != '\0') {
            if (ch == '\\') {
                str.append(ch);
                nextChar();
                if (ch == '\0') {
                    error("Незакрытая строка");
                    return;
                }
                str.append(ch);
                nextChar();
            } else if (ch == quote) {
                str.append(ch);
                nextChar();
                tokens.add(new Token(TokenType.STRING, str.toString(), startLine, startCol, fileName));
                return;
            } else if (ch == '\n' || ch == '\r') {
                error("Незакрытая строка (встречен перевод строки)");
                return;
            } else {
                str.append(ch);
                nextChar();
            }
        }
        error("Незакрытая строка");
    }

    private void handleLongString() {
        int startLine = line, startCol = col;
        int level = getLongBracketLevel();
        if (level == -1) {
            error("Некорректный формат длинной строки");
            return;
        }

        StringBuilder str = new StringBuilder();
        String opening = "[" + "=".repeat(level) + "[";
        String closing = "]" + "=".repeat(level) + "]";
        str.append(opening);

        if (ch == '\n') nextChar();
        else if (ch == '\r' && peek() == '\n') {
            nextChar();
            nextChar();
        }

        while (ch != '\0') {
            str.append(ch);
            if (ch == ']' && checkClosingLongBracket(level)) {
                for (int i = 0; i < closing.length() - 1 && ch != '\0'; i++) {
                    nextChar();
                }
                str.append(closing);
                nextChar();
                break;
            }
            nextChar();
        }

        tokens.add(new Token(TokenType.STRING, str.toString(), startLine, startCol, fileName));
    }

    private void handleNumber() {
        int startLine = line, startCol = col;
        StringBuilder num = new StringBuilder();
        boolean isHex = false;

        if (ch == '0' && (peek() == 'x' || peek() == 'X')) {
            isHex = true;
            num.append(ch); nextChar();
            num.append(ch); nextChar();
        }

        while (Character.isDigit(ch) || (isHex && isHexDigit(ch))) {
            num.append(ch); nextChar();
        }

        if (ch == '.' && (isHex || Character.isDigit(peek()))) {
            num.append(ch); nextChar();
            while (Character.isDigit(ch) || (isHex && isHexDigit(ch))) {
                num.append(ch); nextChar();
            }
        }

        if (!isHex && (ch == 'e' || ch == 'E')) {
            num.append(ch); nextChar();
            if (ch == '+' || ch == '-') { num.append(ch); nextChar(); }
            while (Character.isDigit(ch)) { num.append(ch); nextChar(); }
        }

        if (isHex && (ch == 'p' || ch == 'P')) {
            num.append(ch); nextChar();
            if (ch == '+' || ch == '-') { num.append(ch); nextChar(); }
            while (Character.isDigit(ch)) { num.append(ch); nextChar(); }
        }

        tokens.add(new Token(TokenType.NUMBER, num.toString(), startLine, startCol, fileName));
    }

    private boolean isHexDigit(char c) {
        return Character.isDigit(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    private void handleIdentifierOrKeyword() {
        int startLine = line, startCol = col;
        StringBuilder ident = new StringBuilder();

        while (Character.isLetterOrDigit(ch) || ch == '_') {
            ident.append(ch); nextChar();
        }

        String lexeme = ident.toString();
        TokenType type = KEYWORDS.contains(lexeme) ?
                TokenType.valueOf(lexeme.toUpperCase()) : TokenType.IDENTIFIER;
        tokens.add(new Token(type, lexeme, startLine, startCol, fileName));
    }

    private void handleOperator() {
        int startLine = line, startCol = col;

        switch (ch) {
            case '+': addToken(TokenType.PLUS, "+"); nextChar(); break;
            case '-': addToken(TokenType.MINUS, "-"); nextChar(); break;
            case '*': addToken(TokenType.MULTIPLY, "*"); nextChar(); break;
            case '%': addToken(TokenType.MOD, "%"); nextChar(); break;
            case '^': addToken(TokenType.POWER, "^"); nextChar(); break;
            case '#': addToken(TokenType.LENGTH, "#"); nextChar(); break;
            case '&': addToken(TokenType.AMP, "&"); nextChar(); break;
            case '|': addToken(TokenType.PIPE, "|"); nextChar(); break;
            case '~':
                if (peek() == '=') { nextChar(); addToken(TokenType.NOT_EQUAL, "~="); nextChar(); }
                else { addToken(TokenType.TILDE, "~"); nextChar(); }
                break;
            case '(': addToken(TokenType.LPAREN, "("); nextChar(); break;
            case ')': addToken(TokenType.RPAREN, ")"); nextChar(); break;
            case '{': addToken(TokenType.LBRACE, "{"); nextChar(); break;
            case '}': addToken(TokenType.RBRACE, "}"); nextChar(); break;
            case '[': addToken(TokenType.LBRACKET, "["); nextChar(); break;
            case ']': addToken(TokenType.RBRACKET, "]"); nextChar(); break;
            case ';': addToken(TokenType.SEMICOLON, ";"); nextChar(); break;
            case ',': addToken(TokenType.COMMA, ","); nextChar(); break;
            case ':':
                if (peek() == ':') { nextChar(); addToken(TokenType.DOUBLE_COLON, "::"); nextChar(); }
                else { addToken(TokenType.COLON, ":"); nextChar(); }
                break;
            case '/':
                if (peek() == '/') { nextChar(); addToken(TokenType.FLOOR_DIVIDE, "//"); nextChar(); }
                else { addToken(TokenType.DIVIDE, "/"); nextChar(); }
                break;
            case '<':
                if (peek() == '<') { nextChar(); addToken(TokenType.LEFT_SHIFT, "<<"); nextChar(); }
                else if (peek() == '=') { nextChar(); addToken(TokenType.LESS_EQUAL, "<="); nextChar(); }
                else { addToken(TokenType.LESS, "<"); nextChar(); }
                break;
            case '>':
                if (peek() == '>') { nextChar(); addToken(TokenType.RIGHT_SHIFT, ">>"); nextChar(); }
                else if (peek() == '=') { nextChar(); addToken(TokenType.GREATER_EQUAL, ">="); nextChar(); }
                else { addToken(TokenType.GREATER, ">"); nextChar(); }
                break;
            case '=':
                if (peek() == '=') { nextChar(); addToken(TokenType.EQUAL, "=="); nextChar(); }
                else { addToken(TokenType.ASSIGN, "="); nextChar(); }
                break;
            case '.':
                if (peek() == '.' && pos + 1 < source.length() && source.charAt(pos + 1) == '.') {
                    nextChar(); nextChar(); addToken(TokenType.ELLIPSIS, "..."); nextChar();
                } else if (peek() == '.') {
                    nextChar(); addToken(TokenType.DOT_DOT, ".."); nextChar();
                } else {
                    addToken(TokenType.DOT, "."); nextChar();
                }
                break;
            default:
                error("Недопустимый символ: '" + ch + "'");
                nextChar();
        }
    }

    private void addToken(TokenType type, String lexeme) {
        tokens.add(new Token(type, lexeme, line, col - lexeme.length(), fileName));
    }
}