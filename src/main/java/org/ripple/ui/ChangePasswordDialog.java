package org.ripple.ui;

import org.ripple.connection.CConexion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangePasswordDialog extends JDialog {

    private JTextField usernameField;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;

    public ChangePasswordDialog(Frame parent) {
        super(parent, "Cambiar Contraseña", true);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Usuario:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Contraseña Antigua:"));
        oldPasswordField = new JPasswordField();
        add(oldPasswordField);

        add(new JLabel("Contraseña Nueva:"));
        newPasswordField = new JPasswordField();
        add(newPasswordField);

        JButton aceptarButton = new JButton("Aceptar");
        aceptarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] oldPasswordChars = oldPasswordField.getPassword();
                char[] newPasswordChars = newPasswordField.getPassword();

                // Convertir los arrays de caracteres a String
                String oldPassword = new String(oldPasswordChars);
                String newPassword = new String(newPasswordChars);

                // Crear una instancia de CConexion y obtener la conexión
                CConexion conexion = new CConexion();
                try (Connection connection = conexion.establecerConection()) {
                    // Consulta SQL para actualizar la contraseña
                    String sql = "UPDATE UserProfile SET password = ? WHERE username = ? AND password = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, newPassword);
                        statement.setString(2, username);
                        statement.setString(3, oldPassword);

                        // Ejecutar la actualización
                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(ChangePasswordDialog.this, "Contraseña actualizada con éxito");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(ChangePasswordDialog.this, "La contraseña antigua no es correcta");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ChangePasswordDialog.this, "Error al actualizar la contraseña");
                }
            }
        });
        add(aceptarButton);

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Si el usuario decide cancelar, simplemente cierra la ventana emergente.
                dispose();
            }
        });
        add(cancelarButton);

        setSize(300, 150);
        setLocationRelativeTo(parent);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChangePasswordDialog dialog = new ChangePasswordDialog(new JFrame());
            dialog.setVisible(true);
        });
    }
}
