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
            } else {
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
                                    out.println("Hai raccolto: " + object.getDescription());
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
}
