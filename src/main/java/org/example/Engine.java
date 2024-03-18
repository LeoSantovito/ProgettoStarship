/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example;

import org.example.games.StarshipExodus;
import org.example.parser.Parser;
import org.example.parser.ParserOutput;
import org.example.swing.Background;
import org.example.swing.MenuSwing;
import org.example.type.CommandType;
import org.example.database.Database;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

/**
 * L'Engine si occupa di inizializzare il gioco, gestire il menu iniziale, caricare, salvare le partite ed eseguirle.
 *
 * @author francesco
 */

public class Engine {

    private GameDescription game;
    private Parser parser;
    private Database database;
    private GameTimer timer;


    public Engine() {
        try {
            Set<String> stopwords = Utils.loadFileListInSet(new File("./resources/stopwords"));
            parser = new Parser(stopwords);
            database = new Database();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    /* Inizializa una nuova partita. */
    public void newGame() {

        /* Richiede il nome del giocatore da usare per il salvataggio. */
        System.out.print("Inserisci il tuo nome: ");
        Scanner scanner = new Scanner(System.in);
        String playerName = scanner.nextLine();

        /* Crea una nuova partita, imposta l'id a -1 e il timer a 0. */
        game = new StarshipExodus();
        game.setGameId(-1);
        game.setTimeElapsed(0);

        try {
            System.out.println();
            System.out.println("Creazione di una Nuova Partita...");

            /* Inizializza il gioco e lo salva nel database. Viene anche aggiornata la gameDescription con il nuovo id. */
            game.init();
            database.insertGame(game, playerName);

            System.out.println("Nuova Partita creata con successo!");
            System.out.println();
            System.out.println("ID della partita: " + game.getGameId());
            System.out.println();
            System.out.println("Inizio del Gioco...");
            System.out.println();

            playGame(game);
        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            /* Chiude la connessione al database, fa l'interrupt del timer e chiude il programma. */
            database.closeDatabase();
            timer.interrupt();
            System.out.println("Partita terminata.");
            System.exit(0);
        }
    }

    /* Carica i dati di una partita salvata. */
    public void loadSavedGame() {
        database.cleanEmptyGames(); // Rimuove nuove partite non salvate.
        database.printAllGames();
        System.out.print("Inserisci l'id del salvataggio da caricare: ");
        Scanner scanner = new Scanner(System.in);

        /* Verifica che l'id sia un intero. */
        while (!scanner.hasNextInt()) {
            System.out.println("Quello non è un numero intero!");
            System.out.println();
            System.out.print("Inserisci l'id del salvataggio da caricare: ");
            scanner.next();
        }
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println();
        System.out.println("Caricamento di una Partita Salvata...");

        /* Fa una query sulla tabella games nel DB a partire dall'id. */
        try {
            if (database.selectGame(id) == null) {
                System.out.println("Salvataggio non trovato. Torno al menu principale.");
                System.out.println();

                MenuSwing menuSwing = new MenuSwing(this);
                menuSwing.startMenu();
            } else {
                try {
                    GameDescription game = null;
                    game = database.loadGame(id);

                    System.out.println("Partita Caricata con successo!");
                    System.out.println();
                    System.out.println("ID della partita: " + game.getGameId());
                    System.out.println();

                    playGame(game);
                } finally {
                    /* Chiude la connessione al database, fa l'interrupt del timer e chiude il programma. */
                    database.closeDatabase();
                    timer.interrupt();
                    System.out.println("Partita terminata.");
                    System.exit(0);
                }
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /* Gestisce l'esecuzione del gioco. */
    public void playGame(GameDescription game) {
        /* Inizializza il timer prendendo il valore iniziale dalla gameDescription del DB. */
        timer = new GameTimer(game.getTimeElapsed());
        timer.start();

        /* Stampa messaggi di benvenuto. */
        int totalSeconds = game.getTimeElapsed();
        int id = game.getGameId();
        if (totalSeconds != 0) {
            System.out.println("Abbiamo sentito la tua mancanza, " + database.getPlayerName(id) + "!");
            printGameTime(totalSeconds);
        } else {
            System.out.println("Benvenuto a bordo, " + database.getPlayerName(id) + "!");
        }

        /* Stampa la stanza iniziale. */
        System.out.println("Sei nella stanza: " + game.getCurrentRoom().getName() + ".");
        System.out.println();
        System.out.println(game.getCurrentRoom().getDescription());
        System.out.println();

        /* Gestione dei comandi. */
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            ParserOutput p = parser.parse(command, game.getCommands(), game.getCurrentRoom().getAllObjects(), game.getInventory());

            if (p == null || p.getCommand() == null) {
                System.out.println("Non capisco quello che mi vuoi dire.");
            } else {
                switch (p.getCommand().getType()) {
                    case SAVE:
                        saveGame(game);
                        break;
                    case TIME:
                        /* Stampa il tempo di gioco. */
                        totalSeconds = timer.getSecondsElapsed();
                        printGameTime(totalSeconds);
                        System.out.println();
                        break;
                    case END:
                        /* Chiede al giocatore se vuole salvare la partita prima di uscire dal gioco. */
                        System.out.println("Vuoi salvare la partita prima di uscire? (s/n)");
                        String answer = scanner.nextLine().toLowerCase();

                        while (!answer.equals("s") && !answer.equals("n")) {
                            System.out.println("Risposta non valida. Inserisci 's' per salvare o 'n' per uscire senza salvare.");
                            answer = scanner.nextLine();
                        }
                        if (answer.equals("s")) {
                            saveGame(game);
                        }

                        System.out.println("Addio!");
                        return;
                    case SHOW_MAP: // Mostra la mappa.
                        showMap();
                        break;
                    default:
                        game.nextMove(p, System.out);
                        System.out.println();
                        break;
                }
            }
        }
    }

    /* Salva i dati di una partita, aggiornando il record corrispondente nel database. */
    private void saveGame(GameDescription game) {
        System.out.println("Salvataggio in corso della partita con ID: " + game.getGameId() + "...");

        /* Recupera i secondsElapsed dal timer e li aggiorna nella gameDescription. */
        int secondsElapsed = timer.getSecondsElapsed();
        game.setTimeElapsed(secondsElapsed);

        /* Aggiorna la partita nel database. */
        database.updateGame(game);
        System.out.println("Salvataggio completato!");
        System.out.println();
    }

    /* Stampa il tempo di gioco. */
    private void printGameTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        String printHours = hours == 1 ? "ora" : "ore";
        String printMinutes = minutes == 1 ? "minuto" : "minuti";
        String printSeconds = seconds == 1 ? "secondo" : "secondi";

        if (hours == 0 && minutes == 0) {
            System.out.println("Hai giocato per " + seconds + " " + printSeconds + " e non hai ancora finito il gioco! Che fallimento!");
        } else if (hours == 0) {
            System.out.println("Hai giocato per " + minutes + " " + printMinutes + " e " + seconds + " " + printSeconds + " e non hai ancora finito il gioco! Che fallimento!");
        } else {
            System.out.println("Hai giocato per " + hours + " " + printHours + ", " + minutes + " " + printMinutes + " e " + seconds + " " + printSeconds + " e non hai ancora finito il gioco! Che fallimento!");
        }
    }

    public void showMap() {
        try {
            JDialog frame = new JDialog(new JFrame(), "Mappa", true);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(500, 300);

            Background img = new Background("./resources/Mappa.png");
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


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Engine engine = new Engine();
        MenuSwing menuSwing = new MenuSwing(engine);
        menuSwing.startMenu();
    }
}
