package lualexer;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n🔍 ЛЕКСИЧЕСКИЙ АНАЛИЗАТОР LUA");
        System.out.println("=".repeat(50));

        if (args.length == 0) {
            System.out.println("❌ Использование: java Main <файл.lua>");
            return;
        }

        List<File> files = new ArrayList<>();
        for (String arg : args) {
            File f = new File(arg);
            if (f.exists() && f.getName().endsWith(".lua")) {
                files.add(f);
            } else {
                System.out.println("⚠ Файл не найден: " + arg);
            }
        }

        if (files.isEmpty()) { System.out.println("❌ Нет файлов"); return; }

        Statistics stats = new Statistics();
        ErrorHandler err = new ErrorHandler();
        boolean hasError = false;

        for (File f : files) {
            System.out.println("\n📄 --- " + f.getName() + " ---");
            try {
                String src = Files.readString(f.toPath());
                err.setSource(src, f.getName());
                Lexer lex = new Lexer(src, f.getName(), err);
                List<Token> tokens = lex.tokenize();

                if (tokens.isEmpty()) {
                    System.out.println("   ⚠ Пропущен");
                    hasError = true;
                } else {
                    System.out.println("   ✅ Найдено лексем: " + tokens.size());
                    for (Token t : tokens) {
                        if (t.getType() != TokenType.EOF) {
                            stats.processToken(t);
                            System.out.println("   " + t);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("   ❌ Ошибка: " + e.getMessage());
                hasError = true;
            }
        }

        if (!hasError && !err.hasErrors()) {
            stats.print();
            System.out.println("\n✅ УСПЕХ!");
        } else {
            err.printErrors();
            System.out.println("\n❌ ОШИБКИ");
        }
    }
}