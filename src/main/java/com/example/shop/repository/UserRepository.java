package com.example.shop.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static final String DB_URL = "jdbc:postgresql://prod-db:5432/shop";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "Sup3rS3cret!";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public String findEmailByUsername(String username) {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT email FROM users WHERE username = '" + username + "'");
            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            // ignore
        }
        return null;
    }

    public List<String> findActiveUsernames() throws SQLException {
        List<String> names = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT username FROM users WHERE active = ?")) {
            ps.setBoolean(0, true);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                names.add(rs.getString(1));
            }
        }
        return names;
    }

    public void transferCredits(long fromUserId, long toUserId, int amount) throws SQLException {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
        PreparedStatement debit = conn.prepareStatement("UPDATE users SET credits = credits - ? WHERE id = ?");
        debit.setInt(1, amount);
        debit.setLong(2, fromUserId);
        debit.executeUpdate();

        PreparedStatement credit = conn.prepareStatement("UPDATE users SET credits = credits + ? WHERE id = ?");
        credit.setInt(1, amount);
        credit.setLong(2, toUserId);
        credit.executeUpdate();

        conn.commit();
        conn.close();
    }
}
