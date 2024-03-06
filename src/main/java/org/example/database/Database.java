package org.example.database;

/*Java class that implements the H2 database that saves the object GameDescription, alongside with id, name, and currentRoom.

import org.example.GameDescription;
import org.example.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:h2:./resources/db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public void saveGame(GameDescription game) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO games (name, currentRoom, gameDescription) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, game.getName());
                ps.setString(2, game.getCurrentRoom().getName());
                ps.setObject(3, game);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 */