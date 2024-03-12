package org.example.swing;

import org.example.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PrivateKey;

public class MenuSwing {
    private JButton newGameButton;
    private JButton loadGameButton;
    private JButton exitButton;

    Engine engine = new Engine();

    public MenuSwing() {

    }

    public void startMenu() {
        JFrame frame = new JFrame("Starship Exodus");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creazione dello sfondo (il pannello principale che ha lo sfondo)
        Background sfondo = new Background("./resources/bg.jpeg");


        newGameButton = new JButton("Nuova Partita");
        loadGameButton = new JButton("Carica Partita");
        exitButton = new JButton("Esci");

        sfondo.add(newGameButton);
        sfondo.add(loadGameButton);
        sfondo.add(exitButton);


        frame.add(sfondo);

        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame topLevelFrame = (JFrame) SwingUtilities.getWindowAncestor(newGameButton); // Ottieni la finestra principale
                topLevelFrame.dispose(); // Chiude la finestra principale
                engine.newGame();

            }
        });

        loadGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame topLevelFrame = (JFrame) SwingUtilities.getWindowAncestor(loadGameButton); // Ottieni la finestra principale
                topLevelFrame.dispose(); // Chiude la finestra principale
                engine.loadSavedGame();

            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });


        frame.setVisible(true);
    }


}
