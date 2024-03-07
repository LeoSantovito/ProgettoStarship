package org.example.swing;
import org.example.Engine;
import org.example.games.StarshipExodus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StarshipJswing extends JFrame {

    private JTextArea textArea;
    private JTextField textField;
    Engine engine = new Engine(new StarshipExodus(this), this);


    public StarshipJswing() {
        super("Starship Exodus");
        // Creazione dello sfondo
        ImageIcon backgroundImage = new ImageIcon("./src/main/java/org/example/resources/bg.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight()); // Imposta le dimensioni dell'etichetta sulle dimensioni dell'immagine
        setContentPane(backgroundLabel);

        // Impostazione delle dimensioni della finestra
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Impostazione del layout del pannello principale
        backgroundLabel.setLayout(new BorderLayout());

        // Creazione dell'area di testo per visualizzare il testo del gioco
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        backgroundLabel.add(scrollPane, BorderLayout.CENTER);

        // Creazione del pannello per l'input del comando
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        // Creazione del campo di testo per l'input del comando
        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Esecuzione del comando inserito
                String command = textField.getText();
                engine.processCommand(command); // Passa il comando all'Engine
                // Clear the input field
                textField.setText("");
            }
        });
        inputPanel.add(textField, BorderLayout.CENTER);

        // Creazione del pulsante per inviare il comando
        JButton sendButton = new JButton("Invia");
        sendButton.addActionListener(e -> {
            // Esecuzione del comando inserito
            String command = textField.getText();

            engine.processCommand(command); // Passa il comando all'Engine
            // Clear the input field
            textField.setText("");
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        backgroundLabel.add(inputPanel, BorderLayout.SOUTH);

    }

    // Metodo per visualizzare il testo nella text area
    public void displayText(String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textArea.append(text + "\n");
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        });
    }

    public void processOutput(String output) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                displayText(output);
            }
        });
    }
}