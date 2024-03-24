/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.games;

import org.example.GameDescription;
import org.example.GameTimer;
import org.example.Utils;
import org.example.database.Database;
import org.example.parser.ParserOutput;
import org.example.gui.AlienBossGame;
import org.example.type.*;

import java.io.PrintStream;
import java.util.List;

import static org.example.type.Room.findRoomById;
/*
    * La classe StarshipExodus rappresenta il gioco vero e proprio. Estende la classe GameDescription e implementa i metodi
    * astratti init() e nextMove(). Inoltre, contiene un attributo booleano bossKilled che indica se il boss è stato sconfitto.
    * La classe contiene anche un oggetto CommandsExecution che si occupa di eseguire i comandi del gioco.
    * La classe implementa il metodo init() per inizializzare il gioco e il metodo nextMove() per gestire il prossimo movimento del giocatore.
    * Il metodo nextMove() gestisce i comandi del giocatore e aggiorna la posizione del giocatore sulla mappa.
    * La classe contiene anche un metodo setMap(List<Room> rooms) che imposta le stanze della mappa.
 */
public class StarshipExodus extends GameDescription {
    private static final int STARTING_ROOM_ID = 1;
    private static final int MAP_ID = 4;

    private CommandsExecution execute = new CommandsExecution();
    private boolean bossKilled = false;


    @Override
    public void init() throws Exception {
        /*
        Il metodo init() è stato implementato in modo da caricare i comandi e le stanze del gioco da file JSON.
        Inoltre, inizializza la posizione iniziale del giocatore sulla mappa e imposta la stanza corrente come visitata.
        Nel file JSON delle stanze, vengono impostate le stanze con le relative caratteristiche, compresi gli oggetti all'interno,
        i confini tra le stanze vengono rappresentati con interi nel file JSON e associati a oggetti Room con il metodo setMap(List<Room> rooms).
        In questo modo se si vuole cambiare le stanze del giocoe  gli oggetti con tutte le relative caratteristiche,
        basta modificare il file JSON rooms.
         */
        //Commands
        List<Command> commands = Utils.loadObjectsFromFile("./resources/files/commands.json", Command.class);
        getCommands().addAll(commands);
        List<Room> rooms = Utils.loadObjectsFromFile("./resources/files/rooms.json", Room.class);
        setMap(rooms);
        getRooms().addAll(rooms);

        setCurrentRoom(findRoomById(rooms, STARTING_ROOM_ID));
        getCurrentRoom().setVisited(true);
    }

    /*
    Il metodo nextMove() è stato implementato in modo da gestire i comandi del giocatore e aggiornare la posizione del giocatore sulla mappa.
    Il metodo controlla se il comando è valido e se la direzione in cui il giocatore vuole andare è accessibile.
    Se il giocatore prova ad andare in una direzione non accessibile, il metodo restituisce un messaggio di errore.
    Se il giocatore prova ad andare in una direzione accessibile, il metodo aggiorna la posizione del giocatore e restituisce la descrizione della stanza.
    Il metodo gestisce anche i comandi relativi all'inventario, all'osservazione della stanza, al raccoglimento di oggetti,
     all'apertura di oggetti, all'uso di oggetti, alla visualizzazione della mappa e all'attacco del boss.
    Il metodo restituisce un messaggio di errore se il comando non è valido.
     */
    @Override
    public void nextMove(ParserOutput p, PrintStream out, GameTimer timer, Database database) {
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
                case USE -> {
                    //controlla se l'oggetto è nell'inventario, se si esegue execute.useItem
                    if (p.getInvObject() != null) {
                        execute.useItem(p.getInvObject(), out, timer, this, database);
                    } else if (p.getObject() != null && !p.getObject().isPickupable()) {
                        execute.useItem(p.getObject(), out, timer, this, database);
                    } else {
                        out.println("Non ci sono oggetti da usare.");
                    }
                }
                case SHOW_MAP -> {
                    // Mostra la mappa se l'oggetto mappa è nell'inventario
                    if (Utils.findObjectById(getInventory(), MAP_ID) != null) {
                        execute.showMap();
                        out.println("Per essere una navicella spaziale, è piuttosto grande...");
                    } else {
                        out.println("Non hai la mappa nell'inventario!");
                    }
                }
                case ATTACK -> {
                    execute.attackBoss(bossKilled, getCurrentRoom(), out);
                    bossKilled = AlienBossGame.isGameWon();
                    if (bossKilled && getCurrentRoom().getId() == 8) {
                        AdvObject keyItem = Utils.findObjectById(getCurrentRoom().getObjects(), 9);
                        if (keyItem != null) {
                            keyItem.setPickupable(true);
                            out.println("Complimenti! Ora puoi raccogliere la chiave.");
                        }
                    }
                }
                case HELP -> {
                    execute.showHelp().setVisible(true);

                }
                default -> out.println("Comando non valido.");
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

    /*
    Il metodo setMap(List<Room> rooms) è stato implementato in modo da associare i confini rapprensentati da interi nel file JSON con
    gli attributi di tipo Room della classe Room. In questo modo, se si vuole cambiare la mappa del gioco, basta modificare il file JSON rooms.
     */
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
