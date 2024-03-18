package org.example.games;

import org.example.GameDescription;
import org.example.Utils;
import org.example.swing.Background;
import org.example.type.AdvObject;
import org.example.type.Room;

import javax.swing.*;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class CommandsExecution implements Serializable {
    public void openItem(AdvObject object, PrintStream out) {
            if (object.isOpenable()) {
                if (object.isContainer()) {
                    if (!object.isOpen()) {
                        out.println("Hai aperto: " +
                                object.getName());
                        out.println(object.getDescription());
                        object.setOpen(true);
                        AdvObject c = object;
                        if (!c.getObjectsList().isEmpty()) {
                            out.print("L'oggetto " + c.getName() + " contiene:");
                            Iterator<AdvObject> it =
                                    c.getObjectsList().iterator();
                            while (it.hasNext()) {
                                AdvObject next = it.next();
                                out.print(" " + next.getName());
                                if(it.hasNext()) {
                                    out.print(",");
                                }
                                else {
                                    out.print(".");
                                }
                            }
                            out.println();
                        }
                        else {
                            out.println("Non c'è niente al suo interno.");
                        }
                    } else {
                        out.println("Hai già aperto questo oggetto.");
                        AdvObject c = object;
                        if (!c.getObjectsList().isEmpty()) {
                            out.print(c.getName() + " contiene:");
                            Iterator<AdvObject> it =
                                    c.getObjectsList().iterator();
                            while (it.hasNext()) {
                                AdvObject next = it.next();
                                out.print(" " + next.getName());
                                if(it.hasNext()) {
                                    out.print(",");
                                }
                                else {
                                    out.print(".");
                                }
                            }
                            out.println();
                        }
                        else {
                            out.println("Non c'è niente al suo interno.");
                        }
                    }
                }
            } else {
                out.println("Non puoi aprire questo oggetto.");
            }
    }

    public void pickItem(AdvObject object, PrintStream out, List<AdvObject> inventory, Room currentRoom) {
        // gesione degli oggetti da prendere presenti nella stanza
        if (object.getContainerId() == -1) {
            if (object.isPickupable()) {
                inventory.add(object);
                currentRoom.getObjects().remove(object);
                out.println("Hai raccolto: " + object.getName() + ".");
                out.println(object.getDescription());
            }
            else {
                out.println("Non puoi raccogliere questo oggetto.");
            }
            // gestione degli oggetti da prendere presenti nei container
        } else if (object.getContainerId() != -1) {
            if (object.isPickupable()) {

                Iterator<AdvObject> roomObjectIterator = currentRoom.getObjects().iterator();
                while (roomObjectIterator.hasNext()) {
                    AdvObject o = roomObjectIterator.next();
                    // controllo che l'id dell'oggetto contenitore sia uguale all'id del contenitore dell'oggetto da raccogliere
                    if (o.getId() == object.getContainerId()) {
                        if (o.isOpen()) {
                            Iterator<AdvObject> containedObjectIterator = o.getObjectsList().iterator();
                            while (containedObjectIterator.hasNext()) {
                                AdvObject obj = containedObjectIterator.next();
                                if (object.getId() == obj.getId()) {
                                    object.setContainerId(-1);
                                    inventory.add(object);
                                    containedObjectIterator.remove();
                                    out.println("Hai raccolto: " + object.getName() + ".");
                                    out.println(object.getDescription());
                                }
                            }
                        } else {
                            System.out.println("Non c'è niente da raccogliere qui");
                        }
                    }
                }

            } else {
                out.println("Non puoi raccogliere questo oggetto.");
            }
        }
    }

    public void useItem(AdvObject object, PrintStream out, List<AdvObject> inventory, Room currentRoom) {
        //switch case che in base all'id dell'oggetto permette di personalizzare il comportamento del gioco
        switch (object.getId()) {
            case 2 -> {
                //uso del cristallo nell'hangar
                if (currentRoom.getId() == 6) {
                    out.println("Hai usato: " + object.getName());
                    out.println();

                    /* Finisce il gioco */
                    end(out);

                } else {
                    out.println("Non posso usare il cristallo qui.");
                }
            }
            case 4 -> {
                //mostra la mappa del gioco
                showMap();
            }
            case 6 -> {
                //pistola restringente in stanza ponte inferiore
                if (currentRoom.getId() == 5) {
                    out.println("Hai usato: " + object.getName());
                    out.println();
                    Utils.printFromFile("./resources/dialogs/use_object_6.txt");

                    inventory.remove(object);
                    currentRoom.getWest().setAccessible(true);
                } else {
                    out.println("Non mi conviene usare la pistola qui... meglio tenerla per quando ne avrò bisogno.");
                }
            }
            case 7 -> {
                //visore ultravioletto in stanza laboratorio
                if (currentRoom.getId() == 1 && !currentRoom.getEast().getAccessible()) {
                    out.println("Hai usato: " + object.getName());
                    out.println();
                    Utils.printFromFile("./resources/dialogs/use_object_7.txt");
                    out.println();

                    while (true) {
                        out.println("Inserisci la combinazione corretta:");
                        String input = new Scanner(System.in).nextLine();
                        if (input.equals("531")) {
                            break;
                        } else {
                            out.println("La combinazione non è corretta, devo riprovare.");
                            out.println();
                        }
                    }

                    out.println("La porta si è aperta! Ora posso andare alla sala delle armi.");
                    currentRoom.getEast().setAccessible(true);
                } else if (currentRoom.getId() == 1 && currentRoom.getEast().getAccessible()) {
                    out.println("Ho già aperto la porta, non c'è bisogno di usare il visore qui.");
                } else {
                    out.println("Non puoi usare questo oggetto qui.");
                }
            }
            case 8 -> {
                //trasmettitore di messaggi intergalattico
                out.println("DEBUG");
            }
            case 9 -> {
                //chiave delle celle
                if(currentRoom.getId() == 7) {
                    out.println("Hai usato: " + object.getName());
                    out.println();
                    Utils.printFromFile("./resources/dialogs/use_object_9.txt");

                    currentRoom.getWest().setAccessible(true);
                } else {
                    out.println("Non posso usare la chiave qui.");
                }
            }
            default -> out.println("Non puoi usare questo oggetto.");
        }
    }

    public void showMap() {
        try {
            JDialog frame = new JDialog(new JFrame(), "Mappa", true);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 500);

            Background img = new Background("./resources/images/Mappa.jpg");
            frame.add(img);
            // Creazione di un nuovo thread per la finestra della mappa
            // Imposta la finestra come non bloccante
            frame.setAlwaysOnTop(true);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore nell'apertura della mappa.");
        }
    }

    //fine del gioco
    public void end(PrintStream out) {
        out.println("Fine del gioco. DEBUG.");
        //da implementare lettura da file come la parte iniziale del gioco
        System.exit(0);
    }
}
