package org.ripple.core;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.geom.Rectangle2D;

public class Profile extends JFrame {
    public Profile() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Perfil");
        setMinimumSize(new Dimension(520, 980));
        setLayout(new BorderLayout(0, 100));
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                GradientPaint gradient = new GradientPaint(
                        0, 0, Color.decode("#00AAFF"),
                        0, getHeight(), Color.decode("#95F4FF")
                );

                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        mainPanel.setLayout(new BorderLayout());

        // Agregar el nuevo panel blanco
        JPanel whitePanel = new JPanel();
        whitePanel.setBackground(Color.WHITE);

        Dimension size = new Dimension(mainPanel.getWidth() - 60, mainPanel.getHeight() - 60);
        whitePanel.setSize(size);
        whitePanel.setLocation(30, 30);

        mainPanel.add(whitePanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        setVisible(true);
    }
}

