package org.ripple.core.start;

import javax.swing.*;
import java.awt.*;

public class PlaceholderTextField extends JTextField {
    private String placeholder;

    public PlaceholderTextField(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getText().isEmpty()) {
            Font originalFont = g.getFont();
            g.setFont(originalFont);
            g.setColor(Color.GRAY);
            g.drawString(placeholder, getInsets().left, (getHeight() + g.getFontMetrics().getHeight()) / 2);
            g.setFont(originalFont);
        }
    }
}
