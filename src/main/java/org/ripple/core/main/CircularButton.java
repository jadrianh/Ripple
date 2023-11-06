package org.ripple.core.main;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class CircularButton extends JButton{
    private static final int BUTTON_SIZE = 50;

    public CircularButton(Icon icon) {
        setIcon(icon);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusable(false);
        setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1));
        super.paintComponent(g2);
        g2.dispose();
    }
}
