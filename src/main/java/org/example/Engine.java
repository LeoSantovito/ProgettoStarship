/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example;

import org.example.games.StarshipExodus;
import org.example.parser.Parser;
import org.example.parser.ParserOutput;
import org.example.swing.MenuSwing;
import org.example.type.CommandType;
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

    public Engine() {
        try {
            Set<String> stopwords = Utils.loadFileListInSet(new File("./resources/stopwords"));
            parser = new Parser(stopwords);
            database = new Database();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    /**
     * Mostra il menu iniziale e gestisce la scelta dell'utente. Verrà sostituito da un'interfaccia grafica SWING.
     */
    public void startMenu() {
    /*    System.out.println("========== Starship Exodus ==========");
        System.out.println("1. Nuova Partita");
        System.out.println("2. Carica Partita");
        System.out.println("3. Esci");
        System.out.print("Scelta: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch(choice) {
            case 1:
                newGame();
                break;
            case 2:
                database.printAllGames();
                loadSavedGame();
                break;
            case 3:
                System.out.println("Addio!");
                System.exit(0);
                break;
            default:
                System.out.println("Scelta non valida. Riprova.");
                startMenu();
        }*/


    }

    /* Inizializa una nuova partita. */
    public void newGame() {

        /* Richiede il nome del giocatore da usare per il salvataggio. */
        System.out.print("Inserisci il tuo nome: ");
        Scanner scanner = new Scanner(System.in);
        String playerName = scanner.nextLine();

        /* Crea una nuova partita e imposta l'id a -1. */
        game = new StarshipExodus();
        game.setGameId(-1);

        try {
            System.out.println();
            System.out.println("Creazione di una Nuova Partita...");

            /* Inizializza il gioco e lo salva nel database. Viene anche aggiornata la gameDescription con il nuovo id. */
            game.init();
            database.insertGame(game, game.getCurrentRoom(), playerName);

            System.out.println("Nuova Partita creata con successo!");
            System.out.println();
            System.out.println("ID della partita: " + game.getGameId());
            System.out.println();
            System.out.println("Inizio del Gioco...");
            System.out.println();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        playGame(game);
    }

    /* Carica i dati di una partita salvata. */
    public void loadSavedGame() {
        System.out.print("Inserisci l'id del salvataggio da caricare: ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println();
        System.out.println("Caricamento di una Partita Salvata...");

        try {
            if (database.selectGame(id) != null) {
                GameDescription game = null;
                game = database.loadGame(id);
                System.out.println("Partita Caricata con successo!");
                System.out.println();
                System.out.println("ID della partita: " + game.getGameId());
                System.out.println();
                playGame(game);
            } else {
                System.out.println("Salvataggio non trovato. Torno al menu principale.");
                System.out.println();
               // startMenu();
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /* Gestisce l'esecuzione del gioco. */
    private void playGame(GameDescription game) {
        System.out.println("Sei nella stanza: " + game.getCurrentRoom().getName() + ".");
        System.out.println();
        System.out.println(game.getCurrentRoom().getDescription());
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            ParserOutput p = parser.parse(command, game.getCommands(), game.getCurrentRoom().getObjects(), game.getInventory());
            if (p == null || p.getCommand() == null) {
                System.out.println("Non capisco quello che mi vuoi dire.");
            } else if (p.getCommand() != null && p.getCommand().getType() == CommandType.SAVE){
                saveGame(game);
            }
            else if (p.getCommand() != null && p.getCommand().getType() == CommandType.END) {
                System.out.println("Addio!");
                break;
            } else {
                game.nextMove(p, System.out);
                System.out.println();
            }
        }
    }

    /* Salva i dati di una partita, aggiornando il record corrispondente nel database. */
    private void saveGame(GameDescription game) {
        System.out.println("Salvataggio in corso della partita con ID: " + game.getGameId() + "...");
        int gameId = game.getGameId();
        database.updateGame(gameId, game, game.getCurrentRoom());
        System.out.println("Salvataggio completato!");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Engine engine = new Engine();

        try {
           MenuSwing menuSwing = new MenuSwing();
            menuSwing.startMenu();
        } finally {
            /* Pulisce le partite non salvate (con id della gameDescription a -1) e chiude la connessione al database. */
            engine.database.cleanEmptyGames();
            engine.database.closeDatabase();
        }
    }
}
