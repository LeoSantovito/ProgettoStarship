/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example;

import org.example.games.StarshipExodus;
import org.example.swing.StarshipJswing;
import org.example.parser.Parser;
import org.example.parser.ParserOutput;
import org.example.type.CommandType;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Set;

/**
 * ATTENZIONE: l'Engine è molto spartano, in realtà demanda la logica alla
 * classe che implementa GameDescription e si occupa di gestire I/O sul
 * terminale.
 *
 * @author pierpaolo
 */
public class Engine {

    private final GameDescription game;
    private final StarshipJswing frame;
    public StarshipJswing getFrame() {
        return frame;
    }


    private Parser parser;

    public Engine(GameDescription game, StarshipJswing frame) {
        this.game = game;
        this.frame = frame;
        try {
            this.game.init();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        try {
            Set<String> stopwords = Utils.loadFileListInSet(new File("./resources/stopwords"));
            parser = new Parser(stopwords);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void execute() {
        frame.processOutput("================================");
        frame.processOutput("* Starship Exodus *");
        frame.processOutput("================================");
        frame.processOutput(game.getCurrentRoom().getName());
        frame.processOutput("");
        frame.processOutput(game.getCurrentRoom().getDescription());
        frame.processOutput("");
    }

    public void processCommand(String command) {
        ParserOutput p = parser.parse(command, game.getCommands(), game.getCurrentRoom().getObjects(), game.getInventory());
        frame.processOutput(">>> " + command + "\n");
        if (p == null || p.getCommand() == null) {
            frame.processOutput("Non capisco quello che mi vuoi dire.");
        } else if (p.getCommand().getType() == CommandType.END) {
            frame.processOutput("Addio!");
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            game.nextMove(p, printStream);
            String output = outputStream.toString();
            frame.processOutput(output);
        }

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StarshipJswing starshipJswing = new StarshipJswing();
                StarshipExodus starshipExodus = new StarshipExodus(starshipJswing);
                Engine engine = new Engine(starshipExodus, starshipJswing);
                engine.execute();
                starshipJswing.setVisible(true);
            }
        });
    }



}