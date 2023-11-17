package org.ripple.util;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {

    private final int arc;

    // Constructor de la clase RoundedPanel
    public RoundedPanel(int arc) {
        // Inicializa el panel con un radio específico para bordes redondeados
        this.arc = arc;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Dibuja un panel redondeado con bordes redondeados
        super.paintComponent(g);
        Dimension arcs = new Dimension(arc, arc);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(Color.WHITE);
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
    }
}
