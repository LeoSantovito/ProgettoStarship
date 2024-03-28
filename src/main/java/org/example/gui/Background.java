package org.example.gui;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/*
 * Questa classe rappresenta un pannello con un'immagine di sfondo.
 * Viene usata per impostare un'immagine di sfondo personalizzata per un pannello.
 */

public class Background extends JPanel {
    private Image backgroundImage;

    public Background(String imagePath) {
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage,0, 0, getWidth(), getHeight(), this);
        }
    }
}

