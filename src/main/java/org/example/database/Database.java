package org.example.database;

/* In questo file vengono definiti i metodi per la gestione del database utilizzato per salvare le partite
 * per poterle caricare in un secondo momento.
 */

/* Classe che crea un record nel database quando viene creata una partita. Genera una chiave univoca, chiede il nome
al giocatore, e salva id, GameDescription, CurrentRoom, Data e ora di creazione, e il nome del giocatore.
 */


import org.example.GameDescription;
import org.example.type.Room;
import org.example.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String DB_URL = "jdbc:h2:./resources/savedgames";
    private static final String DB_USER = "user";
    private static final String DB_PASS = "password";

    /* Query per creare la tabella games. La colonna gamedescription è di tipo BLOB (Binary Large Object)
    per poter salvare oggetti serializzati. */
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS games ("
            + "id INT AUTO_INCREMENT PRIMARY KEY,"
            + "gamedescription BLOB NOT NULL,"
            + "currentroom INT NOT NULL,"
            + "creationdate TIMESTAMP NOT NULL,"
            + "playername VARCHAR(255) NOT NULL"
            + ")";

    /* Query per inserire un record di un nuovo salvataggio nella tabella games. */
    private static final String INSERT_GAME = "INSERT INTO games (gamedescription, currentroom, creationdate, playername) VALUES (?, ?, ?, ?)";

    /* Query per selezionare un record dalla tabella games a partire da un id specifico. */
    private static final String SELECT_GAME = "SELECT * FROM games WHERE id = ?";

    /* Query per selezionare tutti i record dalla tabella games. */
    private static final String SELECT_ALL_GAMES = "SELECT * FROM games";

    /* Query per eliminare un record dalla tabella games a partire da un id specifico. */
    private static final String DELETE_GAME = "DELETE FROM games WHERE id = ?";

    /* Query che aggiorna gamedescription e currentroom in un record per salvare lo stato del gioco. */
    private static final String SAVE_GAME = "UPDATE games SET gamedescription = ?, currentroom = ? WHERE id = ?";

    /* Query per eliminare la tabella games con tutti i salvataggi. */
    private static final String DROP_TABLE = "DROP TABLE games";

    private Connection conn;

    public Database() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            Statement stmt = conn.createStatement();
            stmt.execute(CREATE_TABLE);
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void closeDatabase() {
        try {
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void insertGame(GameDescription game, Room currentRoom, String playerName) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(INSERT_GAME);

            /* Serializza l'oggetto GameDescription in un array di byte per poterlo salvare nel database. */
            try {
                byte[] bytesGame = Utils.serializeObject(game);
                pstmt.setBytes(1, bytesGame);
            } catch (Exception ex) {
                System.err.println(ex);
            }

            pstmt.setInt(2, currentRoom.getId());
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(4, playerName);

            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public GameDescription loadGame(Integer id){
        GameDescription game = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement(SELECT_GAME);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                try {
                    return (GameDescription) Utils.deserializeObject(rs.getBytes("gamedescription"));
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }

    public List<GameRecord> selectAllGames() {
        List<GameRecord> games = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL_GAMES);
            while (rs.next()) {
                GameRecord gr = new GameRecord();
                gr.setId(rs.getInt("id"));
                gr.setCurrentRoom(rs.getInt("currentroom"));
                gr.setCreationDate(rs.getTimestamp("creationdate"));
                gr.setPlayerName(rs.getString("playername"));
                games.add(gr);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return games;
    }

    public GameRecord selectGame(Integer id) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(SELECT_GAME);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                GameRecord gr = new GameRecord();
                gr.setGameDescription((GameDescription) rs.getObject("gamedescription"));
                gr.setId(rs.getInt("id"));
                gr.setCurrentRoom(rs.getInt("currentroom"));
                gr.setCreationDate(rs.getTimestamp("creationdate"));
                gr.setPlayerName(rs.getString("playername"));
                return gr;
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }

    public void printAllGames() {
        List<GameRecord> games = selectAllGames();
        if (games.isEmpty()) {
            System.out.println("Nessun salvataggio trovato.");
        } else {
            System.out.println();
            System.out.println("Lista dei Salvataggi:");
            System.out.println("-----------------------");

            for (GameRecord gr : games) {
                System.out.println("ID: " + gr.getId());
                System.out.println("Stanza Corrente: " + gr.getCurrentRoom());
                System.out.println("Data di Creazione: " + gr.getCreationDate());
                System.out.println("Nome del Giocatore: " + gr.getPlayerName());
                System.out.println("-----------------------");
            }
        }
    }

    public void deleteGame(Integer id) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(DELETE_GAME);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void saveGame(Integer id, GameDescription game, Room currentRoom) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(SAVE_GAME);

            try {
                byte[] bytesGame = Utils.serializeObject(game);
                pstmt.setBytes(1, bytesGame);
            } catch (Exception ex) {
                System.err.println(ex);
            }
            pstmt.setInt(2, currentRoom.getId());
            pstmt.setInt(3, id);

            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void dropTable() {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(DROP_TABLE);
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

}