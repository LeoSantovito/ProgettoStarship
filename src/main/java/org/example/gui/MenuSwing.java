package org.example.gui;

import org.example.Engine;
import org.example.games.CommandsExecution;

import javax.swing.*;
import java.awt.*;

public class MenuSwing {
    private JButton newGameButton;
    private JButton loadGameButton;
    private final Engine engine;
    private final CommandsExecution  execute = new CommandsExecution();

    public MenuSwing(Engine engine) {
        this.engine = engine;
    }

    public void startMenu() {
        JFrame frame = new JFrame("Starship Exodus");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        // Creazione dello sfondo (il pannello principale che ha lo sfondo)
        Background sfondo = new Background("./resources/images/bg1.jpg");
        //logo del gioco
        Background logo = new Background("./resources/images/gameLogo2.png");
        logo.setBounds(-7, 50, 400, 125);
        logo.setOpaque(false);
        sfondo.add(logo);
        //Icona del bottone che inizia una nuova partita
        ImageIcon newGameIcon = new ImageIcon("./resources/images/newGame.png");
        Image imageNew = newGameIcon.getImage();
        Image resizedNew = imageNew.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        newGameIcon = new ImageIcon(resizedNew);

        //icona del bottone che carica una partita
        ImageIcon loadGameIcon = new ImageIcon("./resources/images/loadGame.png");
        Image imageLoad = loadGameIcon.getImage();
        Image resizedLoad = imageLoad.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        loadGameIcon = new ImageIcon(resizedLoad);

        //icona del bottone che esci dal gioco
        ImageIcon exitIcon = new ImageIcon("./resources/images/exitGame.png");
        Image imageExit = exitIcon.getImage();
        Image resizedExit = imageExit.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        exitIcon = new ImageIcon(resizedExit);

        //icona del bottone che mostra i comandi del gioco
        ImageIcon helpIcon = new ImageIcon("./resources/images/help.png");
        Image imageHelp = helpIcon.getImage();
        Image resizedHelp = imageHelp.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        helpIcon = new ImageIcon(resizedHelp);

        // Creazione dei bottoni per nuova partita, caricam partita ed esci dal gioco
        newGameButton = new JButton("Nuova Partita");
        newGameButton.setIcon(newGameIcon);

        loadGameButton = new JButton("Carica Partita");
        loadGameButton.setIcon(loadGameIcon);

        JButton exitButton = new JButton("Esci");
        exitButton.setIcon(exitIcon);


        JButton helpButton = new JButton("Help");
        helpButton.setIcon(helpIcon);

        // Set the border with shadow for the buttons
        newGameButton.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        loadGameButton.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        exitButton.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));


        sfondo.setLayout(null);

        //posizionamento dei bottoni e dimensioni
        newGameButton.setBounds(120, 220, 150, 50);
        loadGameButton.setBounds(120, 280, 150, 50);
        helpButton.setBounds(120, 340, 150, 50);
        exitButton.setBounds(120, 400, 150, 50);



        sfondo.add(newGameButton);
        sfondo.add(loadGameButton);
        sfondo.add(exitButton);
        sfondo.add(helpButton);

        frame.add(sfondo); // Add sfondo to the frame
        frame.setResizable(false);
        frame.setVisible(true);

        newGameButton.addActionListener(e -> {
            JFrame topLevelFrame = (JFrame) SwingUtilities.getWindowAncestor(newGameButton); // Ottieni la finestra principale
            topLevelFrame.dispose(); // Chiude la finestra principale
            engine.newGame();

        });

        loadGameButton.addActionListener(e -> {
            JFrame topLevelFrame = (JFrame) SwingUtilities.getWindowAncestor(loadGameButton); // Ottieni la finestra principale
            topLevelFrame.dispose(); // Chiude la finestra principale
            engine.loadSavedGame();
        });

        exitButton.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });

        helpButton.addActionListener(e -> execute.showHelp().setVisible(true));
    }



    }

