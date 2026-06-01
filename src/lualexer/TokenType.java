package lualexer;

public enum TokenType {
    // Ключевые слова Lua
    AND, BREAK, DO, ELSE, ELSEIF, END, FALSE, FOR, FUNCTION,
    GOTO, IF, IN, LOCAL, NIL, NOT, OR, REPEAT, RETURN,
    THEN, TRUE, UNTIL, WHILE,

    // Идентификаторы и литералы
    IDENTIFIER, NUMBER, STRING,

    // Арифметические операторы
    PLUS, MINUS, MULTIPLY, DIVIDE, MOD, POWER, LENGTH,

    // Побитовые операторы
    AMP, TILDE, PIPE, LEFT_SHIFT, RIGHT_SHIFT, FLOOR_DIVIDE,

    // Операторы сравнения
    EQUAL, NOT_EQUAL, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL, ASSIGN,

    // Разделители
    LPAREN, RPAREN, LBRACE, RBRACE, LBRACKET, RBRACKET,
    SEMICOLON, COLON, DOUBLE_COLON, COMMA, DOT, DOT_DOT, ELLIPSIS,

    // Специальные
    COMMENT, EOF, ERROR
}