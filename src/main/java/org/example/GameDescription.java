package org.example;

import org.example.database.Database;
import org.example.parser.ParserOutput;
import org.example.type.Command;
import org.example.type.Inventory;
import org.example.type.Room;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * Questa classe astratta rappresenta lo stato del gioco.
 * Contiene le informazioni sul gioco e i metodi per inizializzarlo e modificarlo.
 */

public abstract class GameDescription implements Serializable {

    private int gameId;

    private int timeElapsed;

    private final List<Room> rooms = new ArrayList<>();

    private final List<Command> commands = new ArrayList<>();

    private final Inventory inventory = new Inventory();

    private Room currentRoom;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(int timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public abstract void init() throws Exception;

    public abstract void nextMove(ParserOutput p, PrintStream out, GameTimer timer, Database database);
}

