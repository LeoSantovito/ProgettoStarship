package org.example.games;

import org.example.GameDescription;
import org.example.GameTimer;
import org.example.Utils;

import org.example.gui.AlienBossGame;

import org.example.api.WeatherApi;
import org.example.database.Database;

import org.example.gui.Background;
import org.example.gui.NumericKeypadUnlocker;
import org.example.type.AdvObject;
import org.example.type.Inventory;
import org.example.type.Room;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Iterator;
import java.util.Scanner;

/*
    * La classe CommandsExecution contiene i metodi per l'esecuzione dei comandi del gioco.
    * Questi metodi vengono chiamati dalla classe StarshipExodus per eseguire le azioni
    * richieste dal giocatore.
 */

public class CommandsExecution implements Serializable {
    private boolean alienKilled = false;
    private static final int CRISTAL_ID = 2;
    private static final int MAP_ID = 4;
    private static final int NOTES_ID = 5;
    private static final int GUN_ID = 6;
    private static final int UV_ID = 7;
    private static final int TRANSMITTER_ID = 8;
    private static final int KEY_ID = 9;
    private static final int HANGAR_ID = 6;
    private static final int LOWER_DECK_ID = 5;
    private static final int LAB_ID = 1;

    private static final int PRISON_ID = 7;

    public void openItem(AdvObject object, PrintStream out) {
            if (object.isOpenable()) {
                if (object.isContainer()) {
                    if (!object.isOpen()) {
                        out.println("Hai aperto: " +
                                object.getName());
                        out.println(object.getDescription());
                        object.setOpen(true);
                        if (!object.getObjectsList().isEmpty()) {
                            out.print("L'oggetto " + object.getName() + " contiene:");
                            Iterator<AdvObject> it =
                                    object.getObjectsList().iterator();
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
                        if (!object.getObjectsList().isEmpty()) {
                            out.print("L'oggetto " + object.getName() + " contiene:");
                            Iterator<AdvObject> it =
                                    object.getObjectsList().iterator();
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

    public void pickItem(AdvObject object, PrintStream out, Inventory inventory, Room currentRoom) {
        // gesione degli oggetti da prendere presenti nella stanza

        /*
        Se l'attributo containerId dell'oggetto è -1,
        significa che l'oggetto è direttamente nella stanza e non all'interno di un contenitore.
         */
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
            /*
            Se l'attributo containerId dell'oggetto è diverso da -1,
            significa che l'oggetto è all'interno di un contenitore.
             */
        } else if (object.getContainerId() != -1) {
            if (object.isPickupable()) {
                for (AdvObject o : currentRoom.getObjects()) {
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

    public void useItem(AdvObject object, PrintStream out, GameTimer timer, GameDescription game, Database database) {
        //switch case che in base all'id dell'oggetto permette di personalizzare il comportamento del gioco
        switch (object.getId()) {
            case CRISTAL_ID -> {
                //uso del cristallo nell'hangar
                if (game.getCurrentRoom().getId() == HANGAR_ID) {
                    out.println("Hai usato: " + object.getName());
                    out.println();

                    /* Rimuove il gioco corrente da DB perché terminato, e stampa la fine del gioco. */
                    database.deleteGame(game.getGameId());
                    end(out, timer.getSecondsElapsed());
                } else {
                    out.println("Non si può usare il cristallo qui.");
                }
            }
            case MAP_ID -> {
                //mostra la mappa del gioco
                showMap();
                out.println("Per essere una navicella spaziale, è piuttosto grande...");
            }
            case NOTES_ID -> {
                //mostra gli appunti di john
                showNotes();
                out.println("Avranno ucciso John perché sapeva troppo? Chissà cosa aveva scoperto...");
            }
            case GUN_ID -> {
                //pistola restringente in stanza ponte inferiore
                if (game.getCurrentRoom().getId() == LOWER_DECK_ID) {
                    out.println("Hai usato: " + object.getName());
                    out.println();
                    Utils.printFromFile("./resources/dialogs/use_object_6.txt");
                    alienKilled = true;

                    game.getInventory().remove(object);
                    game.getCurrentRoom().getWest().setAccessible(true);
                } else {
                    out.println("Non mi conviene usare la pistola qui... meglio tenerla per quando ne avrò bisogno.");
                }
            }
            case UV_ID -> {
                //visore ultravioletto in stanza laboratorio
                if (game.getCurrentRoom().getId() == LAB_ID && !game.getCurrentRoom().getEast().getAccessible()) {
                    out.println("Hai usato: " + object.getName());
                    out.println();
                    Utils.printFromFile("./resources/dialogs/use_object_7.txt");
                    out.println();

                    JDialog dialog = new JDialog(new JFrame(), "Keypad", true);
                    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    dialog.setAlwaysOnTop(true);
                    dialog.getContentPane().add(new NumericKeypadUnlocker());
                    dialog.setSize(350, 500);

                    dialog.setLocationRelativeTo(null); // Posiziona il frame al centro dello schermo
                    dialog.setVisible(true); // Rendi visibile il frame

                    boolean padUnlocked = NumericKeypadUnlocker.isPadUnlocked();
                    if (padUnlocked) {
                        out.println("La porta si è aperta! Ora posso andare alla sala delle armi.");
                        game.getCurrentRoom().getEast().setAccessible(true);
                    } else {
                        out.println("La porta non si è aperta... forse devo riprovare.");
                    }
                } else if (game.getCurrentRoom().getId() == 1 && game.getCurrentRoom().getEast().getAccessible()) {
                    out.println("Hai già aperto la porta, non c'è bisogno di usare il visore qui.");
                } else {
                    out.println("Non serve usare il visore qui.");
                }
            }
            case TRANSMITTER_ID -> {
                //trasmettitore di messaggi intergalattico
                if (object.isUsed()) {
                    out.println("Ho trasmesso già un messaggio, non posso piangermi addosso per sempre...");

                } else {
                    out.println("Questa tecnologia potrebbe servirmi per comunicare con la Terra!");
                    out.println("Si adatta automaticamente alla lingua di chi la usa, non c'è bisogno di impostarla.");
                    out.println("Leggo cosa sta scritto sull'interfaccia...");

                    out.println();

                    WeatherApi weatherApi = new WeatherApi();
                    try {
                        //Esegue la trasmissione in ciclo finché non ritorna true
                        out.println("<< Inserire la località alla quale indirizzare il messaggio: >>");
                        String location = new Scanner(System.in).nextLine();
                        if(weatherApi.getWeatherData(location)){
                            Utils.printFromFile("./resources/dialogs/use_object_8.txt");
                            object.setUsed(true);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            case KEY_ID -> {
                //chiave delle celle
                if(game.getCurrentRoom().getId() == PRISON_ID) {
                    out.println("Hai usato: " + object.getName());
                    out.println();
                    Utils.printFromFile("./resources/dialogs/use_object_9.txt");

                    game.getCurrentRoom().getWest().setAccessible(true);
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
            // Imposta la finestra al centro dello schermo
            frame.setSize(500, 400);
            frame.setLocationRelativeTo(null);

            Background img = new Background("./resources/images/map.png");
            frame.add(img);


            // Imposta la finestra come non bloccante
            frame.setResizable(false);
            frame.setAlwaysOnTop(true);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore nell'apertura della mappa.");
        }
    }

    public void showNotes() {
        try {
            JDialog frame = new JDialog(new JFrame(), "Note", true);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            frame.setSize(500, 750);
            frame.setLocationRelativeTo(null);
            Background img = new Background("./resources/images/notes.png");
            frame.add(img);

            // Imposta la finestra come non bloccante
            frame.setResizable(false);
            frame.setAlwaysOnTop(true);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore nell'apertura della mappa.");
        }
    }

    public JDialog showHelp() {
        JDialog helpDialog = new JDialog(new JFrame(), "Help", true);
        helpDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        helpDialog.setSize(800,500);
        helpDialog.setLocationRelativeTo(null);
        helpDialog.setResizable(false);

        Background img = new Background("./resources/images/bgHelp.jpg");
        helpDialog.add(img);

        JTextArea textArea = new JTextArea();
        textArea.setOpaque(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        textArea.setLineWrap(true); // Imposta il wrap delle righe
        textArea.setSize(800, 500);
        textArea.setEditable(false);
        img.add(textArea);

        try {
            File file = new File("resources/dialogs/help.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append("\t").append(line).append("\t\n");
            }
            br.close();
            textArea.setText(content.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(helpDialog, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        helpDialog.pack();
        return helpDialog;
    }

    /* Stampa il testo della fine del gioco. */
    public void end(PrintStream out, int totalGameTime) {
        Utils.printFromFile("./resources/dialogs/game_end_1.txt");
        out.println();
        Utils.waitForEnter();
        Utils.printFromFile("./resources/dialogs/game_end_2.txt");
        out.println();
        Utils.waitForEnter();
        Utils.printFromFilePlaceholder("./resources/dialogs/game_end_3.txt", Utils.printGameTime(totalGameTime));

        System.exit(0);
    }

    public void attackBoss(boolean bossKilled, Room room, PrintStream out) {
        /*
        * Se il boss è già stato ucciso e il giocatore si trova nella stanza del boss, non è possibile combattere di nuovo.
        * Se il giocatore si trova nella stanza del boss e il boss non è stato ucciso, viene aperta una finestra di gioco
        * per combattere contro il boss.
        * Se il giocatore si trova in una stanza diversa dalla stanza del boss, viene stampato un messaggio di errore.
         */
        if (bossKilled && room.getId() == 8) {
            out.println("Hai già ucciso il boss, non c'è bisogno di combattere di nuovo!\nSfogati con qualcos'altro se proprio ne hai bisogno...");
        } else if (room.getId() == 8 && !bossKilled) {
            out.println("Preparati a combattere!");
            JDialog dialog = new JDialog(new JFrame(), "Starship Exodus", true);
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialog.setAlwaysOnTop(true);
            dialog.getContentPane().add(new AlienBossGame());
            dialog.pack(); // Adatta la dimensione del frame al pannello
            dialog.setLocationRelativeTo(null); // Posiziona il frame al centro dello schermo
            dialog.setVisible(true); // Rendi visibile il frame
        } else if (room.getId() == 5) {
            if (!alienKilled) {
                out.println("Con cosa dovrei attaccare questo alieno?\nNon credo che il wrestling visto da bambino possa aiutarmi in questa situazione...");
            } else {
                out.println("Hai già ucciso l'alieno, non c'è bisogno di combattere di nuovo!");
            }
        }
        else if (room.getId() != 8 && room.getId() != 5) {
            out.println("Non c'è nessuno da attaccare qui!");
        }
    }
}
