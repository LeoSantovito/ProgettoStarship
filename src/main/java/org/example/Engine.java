/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example;

import org.example.games.StarshipExodus;
import org.example.parser.Parser;
import org.example.parser.ParserOutput;
import org.example.type.CommandType;
import org.example.database.Database;
import org.example.database.GameRecord;

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
        System.out.println("========== Starship Exodus ==========");
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
        }
    }

    /* Inizializa una nuova partita */
    private void newGame() {

        /*Richiede il nome del giocatore da usare per il salvataggio. */
        System.out.print("Inserisci il tuo nome: ");
        Scanner scanner = new Scanner(System.in);
        String playerName = scanner.nextLine();

        /* Crea una nuova partita e la salva nel database. */
        game = new StarshipExodus();

        try {
            System.out.println("Creazione di una Nuova Partita...");

            game.init();
            database.insertGame(game, game.getCurrentRoom(), playerName);

            System.out.println("Nuova Partita creata con successo!");
            System.out.println();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        playGame(game);
    }

    public static void saveGame() {
        System.out.println("Test Test Test");
    }

    /* Carica i dati di una partita salvata (DA SISTEMARE PER IL CAST DI GAMEDESCRIPTION IN SELECTGAME, RIVEDERE LOGICA DEL METODO. */
    private void loadSavedGame() {
        System.out.print("Inserisci l'id del salvataggio da caricare: ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Caricamento di una Partita Salvata...");

        try {
            if (database.selectGame(id) != null) {
                GameDescription game = null;
                game = database.loadGame(id);
                System.out.println("Partita Caricata con successo!");
                System.out.println();
                playGame(game);
            } else {
                System.out.println("Salvataggio non trovato. Torno al menu principale.");
                System.out.println();
                startMenu();
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }


    /* Gestisce l'esecuzione del gioco */
    private void playGame(GameDescription game) {
        System.out.println("Inizio del Gioco...");
        System.out.println();

        System.out.println(game.getCurrentRoom().getName());
        System.out.println();
        System.out.println(game.getCurrentRoom().getDescription());
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            ParserOutput p = parser.parse(command, game.getCommands(), game.getCurrentRoom().getObjects(), game.getInventory());
            if (p == null || p.getCommand() == null) {
                System.out.println("Non capisco quello che mi vuoi dire.");
            } else if (p.getCommand() != null && p.getCommand().getType() == CommandType.END) {
                System.out.println("Addio!");
                break;
            } else {
                game.nextMove(p, System.out);
                System.out.println();
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Engine engine = new Engine();
        try {
            engine.startMenu();
        } finally {
            /* Pulisce i record vuoti (da implementare quando metteremo il timer, con timer = 0) e chiude la connessione al database alla fine dell'esecuzione. */
            engine.database.closeDatabase();
        }
    }
}
