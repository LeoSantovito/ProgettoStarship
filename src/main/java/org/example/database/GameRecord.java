package org.example.database;

import org.example.GameDescription;

import java.sql.Timestamp;

public class GameRecord {

    private int id;
    private String name;
    private GameDescription gameDescription;
    private int currentRoom;
    private Timestamp creationDate;
    private String playerName;

    // Costruttore vuoto
    public GameRecord() {
    }

    // Metodi getter e setter per i campi della classe

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameDescription getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(GameDescription gameDescription) {
        this.gameDescription = gameDescription;
    }

    public int getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(int currentRoom) {
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
}