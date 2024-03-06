package org.example.database;

/* In questo file vengono definiti i metodi per la gestione del database utilizzato per salvare le partite
    * per poterle caricare in un secondo momento. Si utilizza H2 come database in-memory a fini di debug
    * (andrà cambiato con uno persistente successivamente).
*/

/* Classe che crea un record nel database quando viene creata una partita. Genera una chiave univoca, chiede il nome
al giocatore, e salva id, GameDescription, CurrentRoom, Data e ora di creazione, e il nome del giocatore.
 */


import org.example.GameDescription;
import org.example.type.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String DB_URL = "jdbc:h2:mem:starshipexodus";
    private static final String DB_USER = "user";
    private static final String DB_PASS = "password";

    /* Query per creare la tabella games. La colonna gamedescription è di tipo BLOB (Binary Large Object)
    per poter salvare oggetti serializzati. */
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS games ("
            + "id INT AUTO_INCREMENT PRIMARY KEY,"
            + "name VARCHAR(255) NOT NULL,"
            + "gamedescription BLOB NOT NULL,"
            + "currentroom INT NOT NULL,"
            + "creationdate TIMESTAMP NOT NULL,"
            + "playername VARCHAR(255) NOT NULL"
            + ")";

    /* Query per inserire un record di un nuovo salvataggio nella tabella games. */
    private static final String INSERT_GAME = "INSERT INTO games (name, gamedescription, currentroom, creationdate, playername) VALUES (?, ?, ?, ?, ?, ?)";

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

    public void insertGame(String name, GameDescription game, Room currentRoom, String playerName) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(INSERT_GAME);
            pstmt.setString(1, name);
            pstmt.setObject(2, game);
            pstmt.setInt(3, currentRoom.getId());
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(5, playerName);
            pstmt.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public GameDescription selectGame(Integer id) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(SELECT_GAME);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return (GameDescription) rs.getObject("gamedescription");
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
                gr.setName(rs.getString("name"));
                gr.setGameDescription((GameDescription) rs.getObject("gamedescription"));
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

    public void deleteGame(Integer id) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(DELETE_GAME);
            pstmt.setInt(1, id);
            pstmt.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void saveGame(Integer id, GameDescription game, Room currentRoom) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(SAVE_GAME);
            pstmt.setObject(1, game);
            pstmt.setInt(2, currentRoom.getId());
            pstmt.setInt(3, id);
            pstmt.execute();
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