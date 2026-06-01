package lualexer;

import java.util.*;
import java.util.stream.Collectors;

public class Statistics {
    private final Map<TokenType, Integer> counts = new HashMap<>();
    private final Map<TokenType, List<String>> examples = new HashMap<>();
    private final Map<String, List<String>> identifiers = new HashMap<>();
    private int total = 0;

    public void processToken(Token token) {
        total++;
        counts.merge(token.getType(), 1, Integer::sum);

        List<String> ex = examples.computeIfAbsent(token.getType(), k -> new ArrayList<>());
        if (ex.size() < 3 && !ex.contains(token.getLexeme())) ex.add(token.getLexeme());

        if (token.getType() == TokenType.IDENTIFIER) {
            identifiers.computeIfAbsent(token.getLexeme(), k -> new ArrayList<>())
                    .add(token.getFileName() + ":" + token.getLine());
        }
    }

    public void print() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 РЕЗУЛЬТАТЫ ЛЕКСИЧЕСКОГО АНАЛИЗА LUA");
        System.out.println("=".repeat(70));
        System.out.println("\n1️⃣ ОБЩЕЕ ЧИСЛО ЛЕКСЕМ: " + total);

        System.out.println("\n2️⃣ ЧАСТОТА ЛЕКСЕМ:");
        System.out.println("   " + "-".repeat(60));
        System.out.printf("   %-20s | %8s | %8s | %s\n", "ТИП", "АБСОЛЮТ", "ОТНОС%", "ПРИМЕРЫ");
        System.out.println("   " + "-".repeat(60));

        counts.entrySet().stream()
                .sorted(Map.Entry.<TokenType, Integer>comparingByValue().reversed())
                .forEach(e -> {
                    double rel = (e.getValue() * 100.0) / total;
                    String ex = examples.getOrDefault(e.getKey(), List.of()).stream()
                            .limit(2).collect(Collectors.joining(", "));
                    System.out.printf("   %-20s | %8d | %7.2f%% | %s\n",
                            e.getKey(), e.getValue(), rel, ex.isEmpty() ? "-" : ex);
                });

        if (!identifiers.isEmpty()) {
            System.out.println("\n3️⃣ ИДЕНТИФИКАТОРЫ (по алфавиту):");
            identifiers.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .forEach(e -> {
                        System.out.println("\n   📌 " + e.getKey() + ":");
                        e.getValue().forEach(pos -> System.out.println("      - " + pos));
                    });
        }
        System.out.println("\n" + "=".repeat(70));
    }
}