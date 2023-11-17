package org.ripple.util;

import javax.swing.*;
import java.awt.*;

public class PlaceholderTextField extends JTextField {
    private String placeholder;
    private Font placeholderFont;
    private Font userTextFont;

    public PlaceholderTextField(String placeholder) {
        this.placeholder = placeholder;
        this.placeholderFont = new Font("Verdana", Font.PLAIN, 16);
        this.userTextFont = new Font("Verdana", Font.PLAIN, 18);
        setFont(userTextFont);
    }

    public void setPlaceholderFont(Font placeholderFont) {
        this.placeholderFont = placeholderFont;
        repaint();
    }

    public void setUserTextFont(Font userTextFont) {
        this.userTextFont = userTextFont;
        setFont(userTextFont);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getText().isEmpty()) {
            Font originalFont = g.getFont();
            g.setFont(placeholderFont);
            g.setColor(Color.GRAY);
            g.drawString(placeholder, getInsets().left, (getHeight() + g.getFontMetrics().getHeight()) / 2);
            g.setFont(originalFont);
        }
    }
}
