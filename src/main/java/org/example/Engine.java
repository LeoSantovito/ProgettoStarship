/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example;

import org.example.games.StarshipExodus;
import org.example.parser.Parser;
import org.example.parser.ParserOutput;
import org.example.gui.MenuSwing;
import org.example.database.Database;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

/**
 *
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
            Set<String> stopwords = Utils.loadFileListInSet(new File("./resources/files/stopwords"));
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

            printGameIntro(playerName);
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
        if (!database.printAllGames()) {
            System.out.println("Non ci sono partite salvate. Creo una nuova partita.");
            System.out.println();
            newGame();
        } else {
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

            /* Fa una query sulla tabella games nel DB a partire dall'id. */
            try {
                while (database.selectGame(id) == null) {
                    System.out.println("Salvataggio non trovato. Inserisci un id valido.");
                    System.out.println();
                    System.out.print("Inserisci l'id del salvataggio da caricare: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Quello non è un numero intero!");
                        System.out.println();
                        System.out.print("Inserisci l'id del salvataggio da caricare: ");
                        scanner.next();
                    }
                    id = scanner.nextInt();
                }
                try {
                    System.out.println("Caricamento di una Partita Salvata...");
                    GameDescription game = database.loadGame(id);

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
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

    /* Gestisce l'esecuzione del gioco. */
    public void playGame(GameDescription game) {
        /* Inizializza il timer prendendo il valore iniziale dalla gameDescription del DB. */
        timer = new GameTimer(game.getTimeElapsed());
        timer.start();

        /* Stampa messaggi di bentornato. */
        int totalSeconds = game.getTimeElapsed();
        int id = game.getGameId();
        if (totalSeconds != 0) {
            System.out.println("Abbiamo sentito la tua mancanza, " + database.getPlayerName(id) + "!");
            System.out.println("Hai già giocato " + Utils.printGameTime(totalSeconds) + "! È un po' tanto per questo gioco...");
        }

        System.out.println();
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
                    case SAVE -> {
                        saveGame(game);
                    }
                    case TIME -> {
                        /* Stampa il tempo di gioco. */
                        totalSeconds = timer.getSecondsElapsed();
                        System.out.print("Hai giocato per " + Utils.printGameTime(totalSeconds) + " e non hai ancora finito il gioco! Che fallimento!");
                        System.out.println();
                    }
                    case END -> {
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
                    }
                    default -> {
                        game.nextMove(p, System.out, timer, database);
                        System.out.println();
                    }
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

    private void printGameIntro(String playerName) {
        Utils.printFromFilePlaceholder("resources/dialogs/game_intro_1.txt", playerName);
        System.out.println();
        Utils.waitForEnter();
        Utils.printFromFile("resources/dialogs/game_intro_2.txt");
        System.out.println();
        Utils.waitForEnter();
        Utils.printFromFile("resources/dialogs/game_intro_3.txt");
        System.out.println();
        Utils.waitForEnter();
        Utils.printFromFile("resources/dialogs/game_intro_4.txt");
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
