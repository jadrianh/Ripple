package org.ripple.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings extends JFrame {
    public Settings() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Ajustes");
        setSize(580, 450);
        setLayout(new BorderLayout());
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/images/Core/sliders.png")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el panel principal que contiene los otros dos paneles
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

        // Crear el panel superior para el título "Opciones" (transparente)
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Opciones");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        // Crear el panel de opciones con fondo blanco y bordes redondeados
        JPanel optionsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.WHITE);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = 40;
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            }
        };

        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        optionsPanel.add(createButton(" Importar Contactos", "/images/Core/download.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Settings.this, "Acción del Botón 1");
            }
        }));
        optionsPanel.add(createSeparator());

        optionsPanel.add(createButton(" Exportar Contactos", "/images/Core/upload.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Settings.this, "Acción del Botón 2");
            }
        }));
        optionsPanel.add(createSeparator());

        optionsPanel.add(createButton(" Modo Claro", "/images/Core/sun.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Settings.this, "Acción del Botón 4");
            }
        }));
        optionsPanel.add(createSeparator());

        optionsPanel.add(createButton(" Ordenar Por: Nombre", "/images/Core/list.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Settings.this, "Acción del Botón 3");
            }
        }));
        optionsPanel.add(createSeparator());

        optionsPanel.add(createButton(" Acerca de Ripple", "/images/Core/info.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Settings.this, "Acción del Botón 5");
            }
        }));
        optionsPanel.add(createSeparator());

        optionsPanel.add(createButton(" Eliminar Cuenta", "/images/Core/trash.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Settings.this, "Acción del Botón 6");
            }
        }));

        int arc = 40;
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font buttonFont = new Font("Verdana", Font.PLAIN, 16);
        Component[] buttons = optionsPanel.getComponents();
        for (Component button : buttons) {
            if (button instanceof JButton) {
                ((JButton) button).setFont(buttonFont);
                ((JButton) button).setFocusable(false);
            }
        }

        optionsPanel.setOpaque(false);

        int margin = 30;
        mainPanel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private JButton createButton(String buttonText, String imagePath, ActionListener actionListener) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagePath));
        Image image = imageIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(image);

        button.setIcon(scaledImageIcon);
        button.setText(buttonText);

        button.addActionListener(actionListener);

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        return button;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(480, 1));
        separator.setForeground(Color.GRAY);
        return separator;
    }
}
