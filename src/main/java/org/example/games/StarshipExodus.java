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
import org.example.parser.ParserOutput;
import org.example.type.*;
import org.example.database.*;

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

    @Override
    public void init() throws Exception {
        //Commands
        List<Command> commands = loadObjectsFromFile("./resources/commands.json", Command.class);
        getCommands().addAll(commands);
        List<Room> rooms = loadObjectsFromFile("./resources/rooms.json", Room.class);
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
        for (Room room : rooms) {
            for (AdvObject obj : room.getObjects()) {
                if (obj.isContainer()){
                    System.out.println("Container: " + obj.getName());
                }
            }
        }
        getRooms().addAll(rooms);
        setCurrentRoom(findRoomById(rooms, 0));
        //Rooms
       /* Room hall = new Room(0, "Corridoio",
                "Sei appena tornato a casa e non sai cosa fare.\nTi ricordi che non hai ancora aperto quel fantastico regalo di tua zia Lina.\n"
                        + " Sarà il caso di cercarlo e di giocarci!");
        hall.setLook(
                "Sei nel corridoio, a nord vedi il bagno, a sud il soggiorno e ad ovest la tua cameretta, forse il gioco sarà lì?");
        Room livingRoom = new Room(1, "Soggiorno",
                "Ti trovi nel soggiorno.\nCi sono quei mobili marrone scuro che hai sempre odiato e delle orribili sedie.");
        livingRoom.setLook("Non c'è nulla di interessante qui.");
        Room kitchen = new Room(2, "Cucina",
                "Ti trovi nella solita cucina.\nMobili bianchi, maniglie azzurre, quello strano lampadario che adoravi tanto quando eri piccolo.\n"
                        +
                        "C'è un tavolo con un bel portafrutta e una finestra.");
        kitchen.setLook(
                "La solita cucina, ma noti una chiave vicino al portafrutta.");
        Room bathroom = new Room(3, "Bagno",
                "Sei nel bagno.\nQuanto tempo passato qui dentro...meglio non pensarci...");
        bathroom.setLook(
                "Vedo delle batterie sul mobile alla destra del lavandino.");
        Room yourRoom = new Room(4, "La tua cameratta",
                "Finalmente la tua cameretta!\nQuesto luogo ti è così famigliare...ma non ricordi dove hai messo il nuovo regalo di zia Lina.");
        yourRoom.setLook(
                "C'è un armadio bianco, di solito ci conservi i tuoi giochi.");
        //maps
        kitchen.setEast(livingRoom);
        livingRoom.setNorth(hall);
        livingRoom.setWest(kitchen);
        hall.setSouth(livingRoom);
        hall.setWest(yourRoom);
        hall.setNorth(bathroom);
        bathroom.setSouth(hall);
        yourRoom.setEast(hall);
        getRooms().add(kitchen);
        getRooms().add(livingRoom);
        getRooms().add(hall);
        getRooms().add(bathroom);
        getRooms().add(yourRoom);
        //obejcts
        AdvObject battery = new AdvObject(1, "batteria",
                "Un pacco di batterie, chissà se sono cariche.");
        battery.setAlias(new String[] {"batterie", "pile", "pila"});
        bathroom.getObjects().add(battery);
        AdvObjectContainer wardrobe =
                new AdvObjectContainer(2, "armadio", "Un semplice armadio.");
        wardrobe.setAlias(new String[] {"guardaroba", "vestiario"});
        wardrobe.setOpenable(true);
        wardrobe.setPickupable(false);
        wardrobe.setOpen(false);
        yourRoom.getObjects().add(wardrobe);
        AdvObject toy = new AdvObject(3, "giocattolo",
                "Il gioco che ti ha regalato zia Lina.");
        toy.setAlias(new String[] {"gioco", "robot"});
        toy.setPushable(true);
        toy.setPush(false);
        wardrobe.add(toy);
        AdvObject kkey = new AdvObject(4, "chiave",
                "Usa semplice chiave come tante altre.");
        toy.setAlias(new String[] {"key"});
        toy.setPushable(false);
        toy.setPush(false);
        kitchen.getObjects().add(kkey);
        //set starting room
        setCurrentRoom(hall); */
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
            if (p.getCommand().getType() == CommandType.NORTH) {
                if (getCurrentRoom().getNorth() != null) {
                    setCurrentRoom(getCurrentRoom().getNorth());
                    move = true;
                } else {
                    noroom = true;
                }
            } else if (p.getCommand().getType() == CommandType.SOUTH) {
                if (getCurrentRoom().getSouth() != null) {
                    setCurrentRoom(getCurrentRoom().getSouth());
                    move = true;
                } else {
                    noroom = true;
                }
            } else if (p.getCommand().getType() == CommandType.EAST) {
                if (getCurrentRoom().getEast() != null) {
                    setCurrentRoom(getCurrentRoom().getEast());
                    move = true;
                } else {
                    noroom = true;
                }
            } else if (p.getCommand().getType() == CommandType.WEST) {
                if (getCurrentRoom().getWest() != null) {
                    setCurrentRoom(getCurrentRoom().getWest());
                    move = true;
                } else {
                    noroom = true;
                }
            } else if (p.getCommand().getType() == CommandType.INVENTORY) {
                out.println("Nel tuo inventario ci sono:");
                for (AdvObject o : getInventory()) {
                    out.println(o.getName() + ": " + o.getDescription());
                }
            } else if (p.getCommand().getType() == CommandType.LOOK_AT) {
                if (getCurrentRoom().getLook() != null) {
                    out.println(getCurrentRoom().getLook());
                } else {
                    out.println("Non c'è niente di interessante qui.");
                }
            } else if (p.getCommand().getType() == CommandType.PICK_UP) {
                if (p.getObject() != null) {
                    if (p.getObject().isPickupable()) {
                        getInventory().add(p.getObject());
                        getCurrentRoom().getObjects().remove(p.getObject());
                        out.println("Hai raccolto: " +
                                p.getObject().getDescription());
                    } else {
                        out.println("Non puoi raccogliere questo oggetto.");
                    }
                } else {
                    out.println("Non c'è niente da raccogliere qui.");
                }
            } else if (p.getCommand().getType() == CommandType.OPEN) {
                /*ATTENZIONE: quando un oggetto contenitore viene aperto, tutti gli oggetti contenuti
                 * vengongo inseriti nella stanza o nell'inventario a seconda di dove si trova l'oggetto contenitore.
                 * Potrebbe non esssere la soluzione ottimale.
                 */
                if (p.getObject() == null && p.getInvObject() == null) {
                    out.println("Non c'è niente da aprire qui.");
                } else {
                    if (p.getObject() != null) {
                        if (p.getObject().isOpenable() &&
                                p.getObject().isOpen() == false) {
                            if (p.getObject().isContainer()) {
                                out.println("Hai aperto: " +
                                        p.getObject().getName());
                                AdvObject c = p.getObject();
                                if (!c.getObjectsList().isEmpty()) {
                                    out.print(c.getName() + " contiene:");
                                    Iterator<AdvObject> it =
                                            c.getObjectsList().iterator();
                                    while (it.hasNext()) {
                                        AdvObject next = it.next();
                                        getCurrentRoom().getObjects().add(next);
                                        out.print(" " + next.getName());
                                        it.remove();
                                    }
                                    out.println();
                                }
                            } else {
                                out.println("Hai aperto: " +
                                        p.getObject().getName());
                                p.getObject().setOpen(true);
                            }
                        } else {
                            out.println("Non puoi aprire questo oggetto.");
                        }
                    }
                    if (p.getInvObject() != null) {
                        if (p.getInvObject().isOpenable() &&
                                p.getInvObject().isOpen() == false) {
                            if (p.getInvObject().isContainer()) {
                                AdvObject c = p.getInvObject();
                                if (!c.getObjectsList().isEmpty()) {
                                    out.print(c.getName() + " contiene:");
                                    Iterator<AdvObject> it =
                                            c.getObjectsList().iterator();
                                    while (it.hasNext()) {
                                        AdvObject next = it.next();
                                        getInventory().add(next);
                                        out.print(" " + next.getName());
                                        it.remove();
                                    }
                                    out.println();
                                }
                            } else {
                                p.getInvObject().setOpen(true);
                            }
                            out.println("Hai aperto nel tuo inventario: " +
                                    p.getInvObject().getName());
                        } else {
                            out.println("Non puoi aprire questo oggetto.");
                        }
                    }
                }
            } else if (p.getCommand().getType() == CommandType.PUSH) {
                //ricerca oggetti pushabili
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
            if (noroom) {
                out.println(
                        "Da quella parte non si può andare c'è un muro!\nNon hai ancora acquisito i poteri per oltrepassare i muri...");
            } else if (move) {
                out.println(getCurrentRoom().getName());
                out.println("================================================");
                out.println(getCurrentRoom().getDescription());
            }
        }
    }

    private void end(PrintStream out) {
        out.println(
                "Premi il pulsante del giocattolo e in seguito ad una forte esplosione la tua casa prende fuoco...\ntu e tuoi famigliari cercate invano di salvarvi e venite avvolti dalle fiamme...\nè stata una morte CALOROSA...addio!");
        System.exit(0);
    }

    private static <T> List<T> loadObjectsFromFile(String filePath, Class<T> objectType) throws IOException {
        Gson gson = new Gson();
        Type objectTypeList = TypeToken.getParameterized(List.class, objectType).getType();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            T[] objectArray = gson.fromJson(br, (Type) Array.newInstance(objectType, 0).getClass());
            return List.of(objectArray);
        }
    }
}
