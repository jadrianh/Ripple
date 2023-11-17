package org.ripple.util;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class CustomScrollPane extends JScrollPane {

    public CustomScrollPane(Component view, int MaximumWidth, int MaximumHeight) {
        super(view);
        customizeScrollPane(MaximumWidth, MaximumHeight);
    }

    private void customizeScrollPane(int MaximumWidth, int MaximumHeight) {
        getViewport().setOpaque(false);  // Quitar fondo
        setBorder(null);  // Quitar bordes
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setMaximumSize(new Dimension(MaximumWidth, MaximumHeight));

        JScrollBar verticalScrollBar = getVerticalScrollBar();
        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                g.setColor(Color.decode("#FFFFFF")); // Hacer el fondo transparente
                g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
    }
}
