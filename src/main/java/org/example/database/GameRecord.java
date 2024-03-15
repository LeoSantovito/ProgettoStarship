package org.example.database;

import org.example.GameDescription;

import java.sql.Timestamp;

/* La classe GameRecord viene utilizzata per rapprensentare le informazioni principali
 * di una partita salvata. In questo modo, si aggregano le informazioni della partita in un'unica struttura dati.
 */

public class GameRecord {

    private int id;
    private String currentRoom;
    private Timestamp creationDate;
    private String playerName;
    private int timeElapsed;

    public GameRecord() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(int timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

}