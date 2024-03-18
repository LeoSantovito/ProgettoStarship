package org.example.games;

import org.example.type.AdvObject;
import org.example.type.Room;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

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
            case 6:
                //pistola restringente in stanza ponte inferiore
                if(currentRoom.getId()==5){
                    out.println("Hai usato: " + object.getName());
                    out.println();
                    out.println("WHOOSH! La pistola ha sparato un raggio laser e ha colpito in pieno l'alieno!");
                    out.println("L'alieno si sta rimpicciolendo sempre di più, fino a scomparire del tutto.");
                    out.println();
                    out.println("Ci è mancato poco! Sembrava che l'alieno mi stesse per attaccare!");
                    out.println("La pistola è scarica ormai... la lascio qua, ma almeno posso andare avanti");

                    inventory.remove(object);
                    currentRoom.getWest().setAccessible(true);
                } else {
                    out.println("Non mi conviene usare la pistola qui... meglio tenerla per quando ne avrò bisogno.");
                }
                break;
            case 7:
                //visore ultravioletto in stanza laboratorio
                if(currentRoom.getId()==1 && !currentRoom.getEast().getAccessible()){
                    out.println("Hai usato: " + object.getName());
                    out.println();
                    out.println("Hai acceso il visore ultravioletto.");
                    out.println("Sul tastierino numerico della porta d'accesso alla sala delle armi ci sono delle impronte digitali.");
                    out.println("Sembra che vengano evidenziati i numeri 1, 3 e 5.");
                    out.println("Devo capire la combinazione corretta... proverò a tentativi.");
                    out.println();

                    /* Chiede al giocatore di inserire la combinazione corretta, ovvero 513.
                    Se la combinazione è corretta, la porta si apre e la stanza diventa accessibile.
                    Se la combinazione è errata, chiede nuovamente finché non inserisce la combinazione corretta.
                    DA FARE
                     */

                    out.println("La porta si è aperta!");
                    currentRoom.getEast().setAccessible(true);
                }
                else if (currentRoom.getId()==1 && currentRoom.getEast().getAccessible()) {
                    out.println("Ho già aperto la porta, non c'è bisogno di usare il visore qui.");
                }
                else {
                    out.println("Non posso usare questo oggetto qua.");
                }
                break;
            default:
                out.println("Non puoi usare questo oggetto.");
                break;
        }
    }
}
