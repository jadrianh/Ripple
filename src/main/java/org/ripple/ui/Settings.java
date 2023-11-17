package org.ripple.ui;

import org.ripple.util.CustomFontManager;
import org.ripple.util.DeleteDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Settings extends JFrame {
    public Settings() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Ajustes");
        setSize(580, 450);
        setLayout(new BorderLayout());
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/images/drawable-action/sliders.png")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

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
        titleLabel.setFont(CustomFontManager.getCustomFontMedium(26, false));
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

        optionsPanel.add(createButton(" Importar Contactos", "/images/drawable-action/download.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Process process = Runtime.getRuntime().exec("powershell.exe -ExecutionPolicy Bypass -File ./src/main/java/org/ripple/scripts/file_selector.ps1");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    StringBuilder output = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        String selectedFile = output.toString().trim();
                        // Hacer algo con el archivo seleccionado
                        System.out.println("Selected file: " + selectedFile);
                    } else {
                        // Manejar error de ejecución de script PowerShell
                        System.out.println("PowerShell script execution failed");
                    }
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }));
        optionsPanel.add(createSeparator());

        optionsPanel.add(createButton(" Exportar Contactos", "/images/drawable-action/upload.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Settings.this, "Acción del Botón 2");
            }
        }));
        optionsPanel.add(createSeparator());

        optionsPanel.add(createButton(" Cambiar contraseña", "/images/drawable-indication/lock.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Settings.this, "Acción del Botón 3");
            }
        }));
        optionsPanel.add(createSeparator());

        optionsPanel.add(createButton(" Acerca de Ripple", "/images/drawable-indication/info.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutUs info = new AboutUs();
                dispose();
            }
        }));
        optionsPanel.add(createSeparator());

        optionsPanel.add(createButton(" Eliminar Cuenta", "/images/drawable-action/trash-2.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear una instancia del cuadro de diálogo personalizado
                DeleteDialog deletionDialog = new DeleteDialog(Settings.this);
                deletionDialog.setVisible(true);

                // Aquí puedes verificar si se confirmó la eliminación después de cerrar el cuadro de diálogo
                if (deletionDialog.isConfirmed()) {
                    // El usuario confirmó la eliminación, proceder con la lógica de eliminación
                } else {
                    // El usuario canceló la eliminación, realizar alguna acción en consecuencia
                }
            }
        }));

        int arc = 40;
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Component[] buttons = optionsPanel.getComponents();
        for (Component button : buttons) {
            if (button instanceof JButton) {
                ((JButton) button).setFont(CustomFontManager.getCustomFont(20, false));
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

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(480, 1));
        separator.setForeground(Color.GRAY);
        return separator;
    }
}
