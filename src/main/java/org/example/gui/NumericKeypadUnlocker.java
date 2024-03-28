package org.example.gui;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/*
 * Questa classe rappresenta un pannello con un tastierino numerico.
 * L'utente deve inserire una sequenza di 3 cifre per sbloccare la porta.
 */

public class NumericKeypadUnlocker extends JPanel {
    private final JTextField inputField;
    private final StringBuilder inputBuffer = new StringBuilder();

    public static boolean padUnlocked = false;

    public NumericKeypadUnlocker() {
        setLayout(new BorderLayout());

        inputField = new JTextField();
        inputField.setEditable(false);
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setFont(new Font("Arial", Font.PLAIN, 24));
        inputField.setBackground(new Color(173, 216, 230)); // Celeste chiaro
        add(inputField, BorderLayout.NORTH);

        JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 10, 10)) {
            // Override del metodo paintComponent per disegnare l'immagine di sfondo
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("./resources/images/bgTastierino.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        JButton[] buttons = new JButton[10];
        for (int i = 1; i <= 9; i++) {
            final int num = i;
            buttons[i - 1] = new JButton(String.valueOf(i));
            buttons[i - 1].setContentAreaFilled(false);
            buttons[i - 1].setOpaque(true);
            buttons[i - 1].setBorderPainted(false);
            buttons[i - 1].setBackground(Color.LIGHT_GRAY);
            buttons[i - 1].setFont(new Font("Arial", Font.PLAIN, 20));
            buttons[i - 1].addActionListener(e -> {
                inputBuffer.append(num);
                updateInputField();
                playClickSound(); // Riproduci il suono del clic
                checkUnlockSequence();
            });
            buttons[i - 1].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    buttons[num - 1].setBackground(Color.GRAY);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    buttons[num - 1].setBackground(Color.LIGHT_GRAY);
                }
            });
            keypadPanel.add(buttons[i - 1]);
        }

        // Aggiungi il bottone "Cancella" rosso
        JButton clearButton = createButton("Cancella");
        clearButton.setForeground(Color.BLACK);
        clearButton.setBackground(Color.RED);
        clearButton.addActionListener(e -> clearInput());
        keypadPanel.add(clearButton);

        // Aggiungi il tasto "0" al centro nella riga inferiore
        JButton zeroButton = new JButton("0");
        zeroButton.setContentAreaFilled(false);
        zeroButton.setOpaque(true);
        zeroButton.setBorderPainted(false);
        zeroButton.setBackground(Color.LIGHT_GRAY);
        zeroButton.setFont(new Font("Arial", Font.PLAIN, 20));
        zeroButton.addActionListener(e -> {
            inputBuffer.append(0);
            updateInputField();
            playClickSound(); // Riproduci il suono del clic
            checkUnlockSequence();
        });
        zeroButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                zeroButton.setBackground(Color.GRAY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                zeroButton.setBackground(Color.LIGHT_GRAY);
            }
        });
        keypadPanel.add(zeroButton);

        JButton okButton = createButton("OK");
        okButton.setForeground(Color.BLACK); // Testo nero
        okButton.setBackground(Color.GREEN); // Sfondo verde
        okButton.addActionListener(e -> {
            if (inputBuffer.length() == 3) { // Controlla se sono state inserite 3 cifre
                JOptionPane.showMessageDialog(NumericKeypadUnlocker.this, "Input confermato: " +
                        inputBuffer, "OK", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(NumericKeypadUnlocker.this, "Inserire esattamente 3 cifre", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
        keypadPanel.add(okButton);

        add(keypadPanel, BorderLayout.CENTER);
    }

    private void updateInputField() {
        inputField.setText(inputBuffer.toString());
    }

    private void checkUnlockSequence() {
        String unlockSequence = "531";
        if (inputBuffer.length() >= unlockSequence.length()) {
            if (inputBuffer.substring(inputBuffer.length() - unlockSequence.length()).equals(
                    unlockSequence)) {
                JOptionPane.showMessageDialog(this, "Porta sbloccata!", "Sbloccato", JOptionPane.INFORMATION_MESSAGE);
                setPadUnlocked(true);
                SwingUtilities.getWindowAncestor(NumericKeypadUnlocker.this).dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Combinazione errata!", "Errore", JOptionPane.ERROR_MESSAGE);
                inputBuffer.setLength(0); // Reset input buffer
                updateInputField(); // Aggiorna il campo di input
            }
        }
    }

    private void playClickSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/sounds/suono.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        return button;
    }

    private void clearInput() {
        if (inputBuffer.length() > 0) {
            inputBuffer.deleteCharAt(inputBuffer.length() - 1);
            updateInputField();
        }
    }

    public static void setPadUnlocked(boolean padUnlocked) {
        NumericKeypadUnlocker.padUnlocked = padUnlocked;
    }

    public static boolean isPadUnlocked() {
        return padUnlocked;
    }
}