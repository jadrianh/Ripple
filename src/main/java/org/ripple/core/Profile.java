package org.ripple.core;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Profile extends JFrame {
    public Profile() {
        initializeUI();
    }

    private void initializeUI() {
        //--------------Configuracion principal--------------//
        setTitle("Perfil");
        setMinimumSize(new Dimension(520, 980));
        setLayout(new BorderLayout(0, 100));
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //-------------Configuracion resolucion-------------//
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        DisplayMode mode = gd.getDisplayMode();

        int screenWidth = mode.getWidth();
        int screenHeight = mode.getHeight();
        int smallerDimension = Math.min(screenWidth, screenHeight);

        int newWidth = smallerDimension * 520 / 980;
        int newHeight = smallerDimension;

        setSize(newWidth, newHeight);
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
                g2d.fill(new Rectangle(0, 0, getWidth(), getHeight()));
            }
        };

        mainPanel.setLayout(new BorderLayout());

        // Agregar el nuevo panel blanco con margen
        JPanel profilePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int arcWidth = 60; // Ajusta el radio de las esquinas redondeadas
                int arcHeight = 60; // Ajusta el radio de las esquinas redondeadas
                RoundRectangle2D roundRect = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.WHITE);
                g2d.fill(roundRect);
            }
        };

        profilePanel.setOpaque(false);

        int margin = 30;
        mainPanel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));

        // Crear el panel para los botones en la parte superior
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        // Crear etiquetas (JLabels) con imágenes y establecer su tamaño
        JLabel backButton = new JLabel(new ImageIcon(getClass().getResource("/images/Core/chevron-left.png")));
        backButton.setPreferredSize(new Dimension(50, 50));

        JLabel trashButton = new JLabel(new ImageIcon(getClass().getResource("/images/Core/trash.png")));
        trashButton.setPreferredSize(new Dimension(50, 50));

        JLabel editButton = new JLabel(new ImageIcon(getClass().getResource("/images/Core/edit.png")));
        editButton.setPreferredSize(new Dimension(50, 50));

        // Configurar la disposición del panel de botones
        JPanel leftButtonPanel = new JPanel();
        leftButtonPanel.add(backButton);

        JPanel rightButtonPanel = new JPanel();
        rightButtonPanel.add(trashButton);
        rightButtonPanel.add(editButton);

        buttonPanel.add(leftButtonPanel, BorderLayout.WEST);
        buttonPanel.add(rightButtonPanel, BorderLayout.EAST);

        // Agregar el panel de botones en la parte superior del profilePanel
        profilePanel.setLayout(new BorderLayout());
        profilePanel.add(buttonPanel, BorderLayout.NORTH);

        mainPanel.add(profilePanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        setVisible(true);
    }
}
