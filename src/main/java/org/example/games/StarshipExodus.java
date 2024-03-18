/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.games;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Engine;
import org.example.GameDescription;
import org.example.Utils;
import org.example.parser.ParserOutput;
import org.example.swing.Background;
import org.example.type.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.example.type.Room.findRoomById;

/**
 * ATTENZIONE: La descrizione del gioco è fatta in modo che qualsiasi gioco
 * debba estendere la classe GameDescription. L'Engine è fatto in modo che possa
 * eseguire qualsiasi gioco che estende GameDescription, in questo modo si
 * possono creare più gioci utilizzando lo stesso Engine.
 *
 * Diverse migliorie possono essere applicate: - la descrizione del gioco
 * potrebbe essere caricate da file o da DBMS in modo da non modificare il
 * codice sorgente - l'utilizzo di file e DBMS non è semplice poiché all'interno
 * del file o del DBMS dovrebbe anche essere codificata la logica del gioco
 * (nextMove) oltre alla descrizione di stanze, oggetti, ecc...
 *
 * @author pierpaolo
 */
public class StarshipExodus extends GameDescription {
    private static final int STARTING_ROOM_ID = 1;

    private static final int MAP_ID = 4;

    private CommandsExecution execute = new CommandsExecution();

    @Override
    public void init() throws Exception {
        //Commands
        List<Command> commands = Utils.loadObjectsFromFile("./resources/commands.json", Command.class);
        getCommands().addAll(commands);
        List<Room> rooms = Utils.loadObjectsFromFile("./resources/rooms.json", Room.class);
        setMap(rooms);
        getRooms().addAll(rooms);

        setCurrentRoom(findRoomById(rooms, STARTING_ROOM_ID));
        getCurrentRoom().setVisited(true);
    }

    @Override
    public void nextMove(ParserOutput p, PrintStream out) {
        if (p.getCommand() == null) {
            out.println(
                    "Non ho capito cosa devo fare! Prova con un altro comando.");
        } else {
            //move
            boolean noroom = false;
            boolean move = false;
            switch (p.getCommand().getType()) {
                case NORTH -> {
                    if (getCurrentRoom().getNorth() != null) {
                        if (getCurrentRoom().getNorth().getAccessible()) {
                            setCurrentRoom(getCurrentRoom().getNorth());
                            move = true;
                        } else {
                            System.out.println(getCurrentRoom().getNorth().getInaccessibleMessage());
                        }
                    } else {
                        noroom = true;
                    }
                }
                case SOUTH -> {
                    if (getCurrentRoom().getSouth() != null) {
                        if (getCurrentRoom().getSouth().getAccessible()) {
                            setCurrentRoom(getCurrentRoom().getSouth());
                            move = true;
                        } else {
                            System.out.println(getCurrentRoom().getSouth().getInaccessibleMessage());
                        }
                    } else {
                        noroom = true;
                    }
                }
                case EAST -> {
                    if (getCurrentRoom().getEast() != null) {
                        if (getCurrentRoom().getEast().getAccessible()) {
                            setCurrentRoom(getCurrentRoom().getEast());
                            move = true;
                        } else {
                            System.out.println(getCurrentRoom().getEast().getInaccessibleMessage());
                        }
                    } else {
                        noroom = true;
                    }
                }
                case WEST -> {
                    if (getCurrentRoom().getWest() != null) {
                        if (getCurrentRoom().getWest().getAccessible()) {
                            setCurrentRoom(getCurrentRoom().getWest());
                            move = true;
                        } else {
                            System.out.println(getCurrentRoom().getWest().getInaccessibleMessage());
                        }
                    } else {
                        noroom = true;
                    }
                }
                case INVENTORY -> {
                    if (getInventory().isEmpty()) {
                        out.println("Non hai oggetti nell'inventario.");
                    } else {
                        out.println("Ecco gli oggetti che hai nell'inventario:");
                        for (AdvObject o : getInventory()) {
                            out.println("- " + o.getName() + ": " + o.getDescription());
                        }
                    }
                }
                case LOOK_AT -> {
                    if (getCurrentRoom().getLook() != null) {
                        out.println(getCurrentRoom().getLook());
                    } else {
                        out.println("Non c'è niente di interessante qui.");
                    }
                }
                case PICK_UP -> {
                    if (p.getObject() != null) {
                        execute.pickItem(p.getObject(), out, getInventory(), getCurrentRoom());
                    } else {
                        out.println("Non c'è niente da raccogliere qui.");
                    }
                }
                case OPEN -> {
                    if (p.getObject() != null) {
                        execute.openItem(p.getObject(), out);
                    } else {
                        out.println("Non c'è niente da aprire qui.");
                    }
                }
                case PUSH -> {
                    // ricerca oggetti pushabili
                    if (p.getObject() != null && p.getObject().isPushable()) {
                        out.println("Hai premuto: " + p.getObject().getName());
                        if (p.getObject().getId() == 3) {
                            end(out);
                        }
                    } else if (p.getInvObject() != null &&
                            p.getInvObject().isPushable()) {
                        out.println("Hai premuto: " + p.getInvObject().getName());
                        if (p.getInvObject().getId() == 3) {
                            end(out);
                        }
                    } else {
                        out.println("Non ci sono oggetti che puoi premere qui.");
                    }
                }
                case USE -> {
                    //controlla se l'oggetto è nell'inventario, se si esegue execute.useItem
                    if (p.getInvObject() != null) {
                        execute.useItem(p.getInvObject(), out, getInventory(), getCurrentRoom());
                    } else if (p.getObject() != null && !p.getObject().isPickupable()) {
                        execute.useItem(p.getObject(), out, getInventory(), getCurrentRoom());
                    } else {
                        out.println("Non ci sono oggetti da usare.");
                    }
                }
                case SHOW_MAP -> {
                    // Mostra la mappa se l'oggetto mappa è nell'inventario
                    if (Utils.findObjectById(getInventory(), MAP_ID)!= null) {
                        execute.showMap();
                    } else {
                        out.println("Non hai la mappa nell'inventario!");
                    }
                }
            }
            if (noroom) {
                out.println(
                        "Da quella parte non si può andare c'è un muro!\nNon hai ancora acquisito i poteri per oltrepassare i muri...");
            } else if (move) {
                out.println();
                out.println("Sei nella stanza: " + getCurrentRoom().getName());
                out.println();
                if (!getCurrentRoom().isVisited()){
                    getCurrentRoom().setVisited(true);
                    out.println(getCurrentRoom().getIntro());
                    out.println();
                }
                out.println(getCurrentRoom().getDescription());
            }
        }
    }

    private void end(PrintStream out) {
        out.println(
                "Premi il pulsante del giocattolo e in seguito ad una forte esplosione la tua casa prende fuoco...\ntu e tuoi famigliari cercate invano di salvarvi e venite avvolti dalle fiamme...\nè stata una morte CALOROSA...addio!");
        System.exit(0);
    }
    private void setMap(List<Room> rooms) {
        for (Room currentRoom : rooms){
            int northId = currentRoom.getNorthId();
            if(northId != -1) {
                Room north = findRoomById(rooms, northId);
                currentRoom.setNorth(north);
            }
            int southId = currentRoom.getSouthId();
            if(southId != -1) {
                Room south = findRoomById(rooms, southId);
                currentRoom.setSouth(south);
            }
            int eastId = currentRoom.getEastId();
            if(eastId != -1) {
                Room east = findRoomById(rooms, eastId);
                currentRoom.setEast(east);
            }
            int westId = currentRoom.getWestId();
            if(westId != -1) {
                Room west = findRoomById(rooms, westId);
                currentRoom.setWest(west);
            }
        }
    }
}
