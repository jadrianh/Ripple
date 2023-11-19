package org.ripple.util;

import java.awt.*;
import javax.swing.border.AbstractBorder;

public class RoundedCornerBorder extends AbstractBorder {
    private final int radius;
    private final int borderThickness;
    private final Color borderColor;

    public RoundedCornerBorder(int radius, int borderThickness, Color borderColor) {
        this.radius = radius;
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(borderThickness));
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2d.dispose();
    }
}
