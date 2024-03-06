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
//import org.example.database.Database;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

/**
 * ATTENZIONE: l'Engine è molto spartano, in realtà demanda la logica alla
 * classe che implementa GameDescription e si occupa di gestire I/O sul
 * terminale.
 *
 * @author francesco
 */
public class Engine {

    private GameDescription game;
    //private final GameDescription game;
    private Parser parser;
    //private Database database;

    public Engine() {
        try {
            Set<String> stopwords = Utils.loadFileListInSet(new File("./resources/stopwords"));
            parser = new Parser(stopwords);
            //database = new Database();
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
                System.out.println("Carica partita non ancora implementato!");
                //loadGame();
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
        System.out.println("Creazione di una Nuova Partita...");
        game = new StarshipExodus();
        try {
            game.init();
            //inserire nuovo record nel database
            //database.insertNewGame(game);
            System.out.println("Nuova Partita creata con successo!");
        } catch (Exception ex) {
            System.err.println(ex);
        }
        playGame();
    }

    /* Gestisce l'esecuzione del gioco */
    private void playGame() {
        System.out.println("Inizio del Gioco...");
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
        engine.startMenu();
    }
}
