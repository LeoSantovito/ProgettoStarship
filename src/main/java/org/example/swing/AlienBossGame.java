package org.example.swing;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class AlienBossGame extends JPanel {
    private int bossHealth = 100; // Salute del boss alieno
    private int score = 0; // Punteggio del giocatore
    private boolean bossHit = false; // Indica se il boss è stato colpito nell'ultimo click
    private Random random = new Random();
    private int bossX, bossY; // Coordinate x e y del boss
    private int bossRadius = 20; // Raggio del boss
    private Image backgroundImage;
    private Image bossImage;
    private boolean killed = false;

    public AlienBossGame() {
        setPreferredSize(new Dimension(400, 300)); // Imposta le dimensioni del pannello del gioco
        backgroundImage = new ImageIcon("./resources/images/spazio.jpg").getImage();
        bossImage = new ImageIcon("./resources/images/boss.jpg").getImage();

        // Crea un timer per cambiare l'area del boss ogni 0.5 secondi
        Timer areaTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bossX = random.nextInt(getWidth() - 2 * bossRadius) + bossRadius; // Nuova coordinata x casuale per il boss
                bossY = random.nextInt(getHeight() - 2 * bossRadius) + bossRadius; // Nuova coordinata y casuale per il boss
                repaint(); // Ridisegna il pannello del gioco con la nuova area del boss
            }
        });
        areaTimer.start(); // Avvia il timer per cambiare l'area del boss

        // Aggiunge un mouse listener per rilevare i click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                // Calcola la distanza dal centro del cerchio
                double distance = Math.sqrt(Math.pow(x - (bossX + bossRadius), 2) + Math.pow(y - (bossY + bossRadius), 2));
                if (distance <= bossRadius) {
                    bossHit = true; // Indica che il boss è stato colpito
                    bossHealth -= 10; // Rimuove 10 punti salute al boss
                    score += 10; // Aumenta il punteggio del giocatore
                    repaint(); // Ridisegna il pannello del gioco dopo ogni click
                }
            }
        });

        // Crea un timer per far avanzare il gioco
        Timer gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bossHit = false; // Resetta lo stato del boss dopo ogni avanzamento del gioco
                repaint(); // Ridisegna il pannello del gioco dopo ogni avanzamento
                if (bossHealth <= 0 && !killed) {
                    killed = true;
                    endGame(); // Termina il gioco se il boss è stato sconfitto
                }
            }
        });
        gameTimer.start(); // Avvia il timer del gioco
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g); // Disegna il gioco
    }

    private void drawGame(Graphics g) {
        // Disegna lo sfondo
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);

        // Disegna il boss alieno come un cerchio
        g.drawImage(bossImage, bossX, bossY, 2*bossRadius, 2*bossRadius, null);

        // Disegna la salute del boss alieno
        g.setColor(Color.WHITE);
        g.drawString("Salute del boss alieno: " + bossHealth, 10, 20);

        // Disegna il punteggio del giocatore
        g.drawString("Punteggio: " + score, getWidth() - 100, 20);

        // Mostra un messaggio se il boss è stato colpito nell'ultimo click
        if (bossHit) {
            g.setColor(Color.RED);
            g.drawString("Hai colpito il boss!", getWidth() / 2 - 50, getHeight() - 20);
        }
    }

    private void endGame() {
        JOptionPane.showMessageDialog(this, "Hai sconfitto il boss!\nPunteggio finale: " + score,
                "Fine del gioco", JOptionPane.INFORMATION_MESSAGE);
    }
}
