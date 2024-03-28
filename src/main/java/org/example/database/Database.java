package org.example.database;

/* Questa classe si occupa di gestire la connessione al database H2 e di eseguire le query per
 * inserire, selezionare, eliminare e aggiornare i record della tabella games. */

import org.example.GameDescription;
import org.example.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String DB_URL = "jdbc:h2:./resources/files/savedgames";
    private static final String DB_USER = "user";
    private static final String DB_PASS = "password";

    /* Query per creare la tabella games. La colonna gamedescription è di tipo BLOB (Binary Large Object)
    per poter salvare oggetti serializzati. */
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS games ("
            + "id INT AUTO_INCREMENT PRIMARY KEY,"
            + "gamedescription BLOB NOT NULL,"
            + "creationdate TIMESTAMP NOT NULL,"
            + "playername VARCHAR(255) NOT NULL"
            + ")";

    /* Query per inserire un record di un nuovo salvataggio nella tabella games. */
    private static final String INSERT_GAME = "INSERT INTO games (gamedescription, creationdate, playername) VALUES (?, ?, ?)";

    /* Query per selezionare un record dalla tabella games a partire da un id specifico. */
    private static final String SELECT_GAME = "SELECT * FROM games WHERE id = ?";

    /* Query per selezionare tutti i record dalla tabella games. */
    private static final String SELECT_ALL_GAMES = "SELECT * FROM games";

    /* Query per eliminare un record dalla tabella games a partire da un id specifico. */
    private static final String DELETE_GAME = "DELETE FROM games WHERE id = ?";

    /* Query che aggiorna gamedescription in un record per salvare lo stato del gioco. */
    private static final String SAVE_GAME = "UPDATE games SET gamedescription = ? WHERE id = ?";

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

    /* Inserisce un nuovo record corrispondente a una partita nella tabella games. */
    public void insertGame(GameDescription game, String playerName) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(INSERT_GAME);

            /* Serializza l'oggetto GameDescription in un array di byte per poterlo salvare nel database. */
            try {
                byte[] bytesGame = Utils.serializeObject(game);
                pstmt.setBytes(1, bytesGame);
            } catch (Exception ex) {
                System.err.println(ex);
            }

            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(3, playerName);

            pstmt.executeUpdate();

            /* Si aggiorna dopo aver salvato il GameDescription nella tabella games.
             * Le partite non salvate rimarranno pertanto con gameId = -1.
             */
            updateGameId(game);

            pstmt.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    /* Recupera l'ID generato da SQL per il record del gioco e imposta gameId nell'oggetto GameDescription. */
    private void updateGameId(GameDescription game){
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("CALL IDENTITY()");
            if (rs.next()) {
                game.setGameId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    /* Carica una GameDescription dalla tabella games a partire da un id specifico. */
    public GameDescription loadGame(int id){
        byte[] serializedGame = null;
        GameDescription loadedGame = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement(SELECT_GAME);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                try {
                    serializedGame = rs.getBytes(2);
                    loadedGame = (GameDescription) Utils.deserializeObject(serializedGame);
                    return loadedGame;
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }

    /* Estrae le informazioni principali di tutti i record dalla tabella games. */
    public List<GameRecord> selectAllGames() {
        List<GameRecord> games = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL_GAMES);
            while (rs.next()) {
                GameRecord gr = new GameRecord();
                gr.setId(rs.getInt("id"));
                gr.setCreationDate(rs.getTimestamp("creationdate"));
                gr.setPlayerName(rs.getString("playername"));

                try {
                    /* Informazioni ottenute dalla GameDescription. */
                    byte[] loadedGame = rs.getBytes(2);
                    gr.setCurrentRoom(((GameDescription) Utils.deserializeObject(loadedGame)).getCurrentRoom().getName());
                    gr.setTimeElapsed(((GameDescription) Utils.deserializeObject(loadedGame)).getTimeElapsed());
                } catch (Exception ex) {
                    System.err.println(ex);
                }

                games.add(gr);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return games;
    }

    /* Estrae le informazioni principali di un singolo record dalla tabella games a partire da un id specifico. */
    public GameRecord selectGame(int id) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(SELECT_GAME);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                GameRecord gr = new GameRecord();

                try {
                    /* Informazioni ottenute dalla GameDescription. */
                    byte[] loadedGame = rs.getBytes(2);
                    gr.setCurrentRoom(((GameDescription) Utils.deserializeObject(loadedGame)).getCurrentRoom().getName());
                    gr.setTimeElapsed(((GameDescription) Utils.deserializeObject(loadedGame)).getTimeElapsed());
                } catch (Exception ex) {
                    System.err.println(ex);
                }

                gr.setId(rs.getInt("id"));
                gr.setCreationDate(rs.getTimestamp("creationdate"));
                gr.setPlayerName(rs.getString("playername"));
                return gr;
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }

    /* Stampa tutte le informazioni dei salvataggi nella tabella games. */
    public boolean printAllGames() {
        List<GameRecord> games = selectAllGames();
        if (games.isEmpty()) {
            return false;
        } else {
            System.out.println();
            System.out.println("Lista dei Salvataggi:");
            System.out.println("-----------------------");

            for (GameRecord gr : games) {
                System.out.println("ID: " + gr.getId());
                System.out.println("Stanza Corrente: " + gr.getCurrentRoom());
                System.out.println("Data di Creazione: " + gr.getCreationDate());
                System.out.println("Nome del Giocatore: " + gr.getPlayerName());
                /* Ottiene e stampa il tempo trascorso in ore, minuti e secondi. */
                int totalSeconds = gr.getTimeElapsed();
                int hours = totalSeconds / 3600;
                int minutes = (totalSeconds % 3600) / 60;
                int seconds = totalSeconds % 60;
                System.out.println("Tempo di Gioco: " + hours + "h " + minutes + "m " + seconds + "s");
                System.out.println("-----------------------");
            }
            return true;
        }
    }

    public String getPlayerName(int id){
        try {
            PreparedStatement pstmt = conn.prepareStatement(SELECT_GAME);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("playername");
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }

    /* Pulisce le partitite vuote dalla tabella games nel DB. */
    public void cleanEmptyGames() {

        /* Cicla su tutti i record il quale valore gameId in GameDescription è -1 e li elimina. */
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL_GAMES);
            while (rs.next()) {
                try {
                    byte[] serializedGame = rs.getBytes(2);
                    GameDescription loadedGame = (GameDescription) Utils.deserializeObject(serializedGame);
                    if (loadedGame.getGameId() == -1) {
                        deleteGame(rs.getInt(1));
                    }
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    /* Elimina un record dalla tabella games a partire da un id specifico. */
    public void deleteGame(int id) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(DELETE_GAME);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void updateGame(GameDescription game) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(SAVE_GAME);

            try {
                byte[] bytesGame = Utils.serializeObject(game);
                pstmt.setBytes(1, bytesGame);
            } catch (Exception ex) {
                System.err.println(ex);
            }
            pstmt.setInt(2, game.getGameId());
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