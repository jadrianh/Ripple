package org.ripple.util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PicturePanel extends JPanel {

    private Image profileImage;

    public PicturePanel(Image profileImage, int width, int height) {
        this.profileImage = profileImage;
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dibuja un fondo redondeado
        RoundRectangle2D rect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 50, 50);
        g2d.setClip(rect);
        g2d.setColor(Color.WHITE);
        g2d.fill(rect);

        // Dibuja la imagen
        if (profileImage != null) {
            g2d.drawImage(profileImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
