package org.example.database;
import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/RealmWar";
    private static final String USER = "postgres";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void saveGameResult(String winner, String loser, int winnerScore, int loserScore) {
        String sql = "INSERT INTO game_history (winner_name, loser_name, winner_score, loser_score) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, winner);
            stmt.setString(2, loser);
            stmt.setInt(3, winnerScore);
            stmt.setInt(4, loserScore);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getGameHistory() {
        try {
            String sql = "SELECT id, winner_name, loser_name, winner_score, loser_score FROM game_history ORDER BY id DESC";
            Connection conn = getConnection();
            return conn.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

