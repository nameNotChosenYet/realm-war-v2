package org.example.utils;

import org.example.model.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LogManager {
    private static final Path LOG_FILE = Path.of("log.json");
    private static final List<String> LOGS = new ArrayList<>();

    static {
        try {
            Files.deleteIfExists(LOG_FILE);
        } catch (IOException ignored) {}
    }

    public static void logAttack(String playerName) {
        LOGS.add(playerName + " attacked!");
        writeLogs();
    }

    public static void writeFinalScores(Player p1, Player p2) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"logs\": [\n");
        for (int i = 0; i < LOGS.size(); i++) {
            sb.append("    \"").append(LOGS.get(i).replace("\"", "\\\"")).append("\"");
            if (i < LOGS.size() - 1) sb.append(',');
            sb.append("\n");
        }
        sb.append("  ],\n  \"scores\": {\n");
        sb.append("    \"").append(p1.getName()).append("\": ").append(p1.getScore()).append(',').append("\n");
        sb.append("    \"").append(p2.getName()).append("\": ").append(p2.getScore()).append("\n");
        sb.append("  }\n}");
        try {
            Files.writeString(LOG_FILE, sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeLogs() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            for (int i = 0; i < LOGS.size(); i++) {
                sb.append("  \"").append(LOGS.get(i).replace("\"", "\\\"")).append("\"");
                if (i < LOGS.size() - 1) sb.append(',');
                sb.append("\n");
            }
            sb.append("]");
            Files.writeString(LOG_FILE, sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}