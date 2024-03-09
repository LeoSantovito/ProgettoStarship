package org.example.swing;
import org.example.Engine;
import org.example.games.StarshipExodus;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StarshipJswing extends JFrame {

    private JTextArea textArea;
    private JTextField textField;
    Engine engine = new Engine(new StarshipExodus(this), this);

    public StarshipJswing() {
        super("Starship Exodus");

        JFrame frame = this;
        frame.setResizable(false);
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());


        // Creazione dello sfondo (il pannello principale che ha lo sfondo)
        Background sfondo = new Background("./src/main/java/org/example/resources/bgS.jpeg");
        frame.add(sfondo);


        // Creazione della JTextArea per visualizzare il testo del gioco e aggiungila a uno JScrollPane
        textArea = new JTextArea(20,60);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.BOLD, 12));
        textArea.setBackground(sfondo.getBackground().darker());
        sfondo.add(textArea);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0)); // Imposta solo lo spessore verticale
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 12)); // Imposta solo lo spessore orizzontale
        sfondo.add(scrollPane, BorderLayout.CENTER);

        // Creazione del JPanel per l'input dei comandi
        JPanel inputPanel = new JPanel();

        // Creazione del JTextField per l'immissionce dei comandi
        textField = new JTextField(20);
        // Aggiungi il JTextField al pannello di input
        inputPanel.add(textField, BorderLayout.EAST);

        ImageIcon sendIcon = new ImageIcon("./src/main/java/org/example/resources/send.png");
        Image sendImage = sendIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        ImageIcon sendResizedIcon = new ImageIcon(sendImage);
        JButton sendButton = new JButton("Invia", sendResizedIcon);

        // Carica l'immagine icona
        ImageIcon InvIcon = new ImageIcon("./src/main/java/org/example/resources/ufo.png");
        Image image = InvIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);//
        ImageIcon InvResizedIcon = new ImageIcon(image);
        JButton inventario = new JButton("Inventario", InvResizedIcon);
        

        // Creazione del JButton per inviare i comandi
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = textField.getText();
                engine.processCommand(command);
                textField.setText("");
            }
        });
        // ActionListener per il JButton
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = textField.getText();
                engine.processCommand(command);
                textField.setText("");
            }
        });
        inventario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.processCommand("inventario");
            }
        });

        inputPanel.add(inventario);
        inputPanel.add(textField);
        inputPanel.add(sendButton);

        // Aggiungi il pannello di input al pannello principale
        frame.add(inputPanel, BorderLayout.SOUTH);


    }

    // Metodo per visualizzare il testo nella JTextArea
    public void displayText(String text) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(text + "\n");
            textArea.setCaretPosition(textArea.getDocument().getLength());

        });
    }

    public void processOutput(String output) {
        SwingUtilities.invokeLater(() -> displayText(output));
    }
}