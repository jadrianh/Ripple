package org.ripple.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteDialog extends JDialog {

    private JTextField confirmationField;
    private boolean confirmed = false;

    public DeleteDialog(JFrame parent) {
        super(parent, "Confirmación de Eliminación", true);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);

        JLabel messageLabel = new JLabel("Elimina tu cuenta");
        messageLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        panel.add(messageLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);

        JLabel descriptionLabel = new JLabel("<html><body style='width: 280px;'>Estás a punto de eliminar permanentemente tu cuenta en Ripple. Esta acción resultará en la pérdida irreversible de todos tus datos, perfiles y configuraciones. \n" +
                "Antes de continuar, te recomendamos encarecidamente exportar tus datos.</body></html>");
        descriptionLabel.setVerticalAlignment(JLabel.TOP);
        descriptionLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
        panel.add(descriptionLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);

        JLabel warningLabel = new JLabel("Deberás escribir ACEPTO en el recuadro:");
        warningLabel.setForeground(Color.decode("#ED2939"));
        warningLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
        panel.add(warningLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);

        confirmationField = new JTextField();
        confirmationField.setPreferredSize(new Dimension(150, 25));
        confirmationField.setMaximumSize(new Dimension(150, 25));
        panel.add(confirmationField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 0, 0);

        JButton confirmButton = new JButton("Eliminar");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = confirmationField.getText().trim();
                if ("ACEPTO".equals(userInput)) {
                    confirmed = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Palabra de confirmación incorrecta.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    confirmationField.setText("");
                }
            }
        });
        confirmButton.setBackground(Color.decode("#ED2939"));
        confirmButton.setFocusable(false);
        confirmButton.setBorderPainted(false);
        confirmButton.setForeground(Color.WHITE);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        cancelButton.setBackground(Color.decode("#91A3B0"));
        cancelButton.setFocusable(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(buttonPanel, gbc);

        add(panel);
        pack();
        setSize(460, 320);  // Tamaño ajustado a 580x450
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}