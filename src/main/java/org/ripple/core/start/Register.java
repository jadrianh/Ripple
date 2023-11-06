package org.ripple.core.start;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.sql.ResultSet;
import org.ripple.CConexion;

public class Register extends JFrame implements ActionListener {
    private JTextField idUsuarioField, usuarioField, nombreField, apellidoField;
    private JPasswordField contrasenaField;
    private JButton registrarseButton, volverButton;

    private Color backgroundColor = new Color(0, 170, 255);
    private Color buttonColor = new Color(0, 167, 248);
    private Color buttonTextColor = Color.WHITE;
    private Color labelTextColor = Color.BLACK;

    private Connection cn;

    public Register() {
        setTitle("Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);

        CConexion conexionManager = new CConexion();
        cn = conexionManager.obtenerConexion();

        if (cn == null) {
            JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión a la Base de Datos");
        } else {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(4, 2));
            panel.setBackground(backgroundColor);

            JLabel idUsuarioLabel = new JLabel("ID:");
            JLabel usuarioLabel = new JLabel("Usuario:");
            JLabel contrasenaLabel = new JLabel("Contraseña:");
            JLabel nombreLabel = new JLabel("Nombre:");
            JLabel apellidoLabel = new JLabel("Apellido:");

            idUsuarioLabel.setForeground(labelTextColor);
            usuarioLabel.setForeground(labelTextColor);
            contrasenaLabel.setForeground(labelTextColor);
            nombreLabel.setForeground(labelTextColor);
            apellidoLabel.setForeground(labelTextColor);

            idUsuarioField = new JTextField();
            usuarioField = new JTextField();
            contrasenaField = new JPasswordField();
            nombreField = new JTextField();
            apellidoField = new JTextField();

            registrarseButton = new JButton("Registrarse");
            registrarseButton.addActionListener(this);
            registrarseButton.setPreferredSize(new Dimension(100, 30));
            registrarseButton.setBackground(buttonColor);
            registrarseButton.setForeground(buttonTextColor);

            volverButton = new JButton("Volver");
            volverButton.addActionListener(this);
            volverButton.setPreferredSize(new Dimension(100, 30));
            volverButton.setBackground(buttonColor);
            volverButton.setForeground(buttonTextColor);

            panel.add(idUsuarioLabel);
            panel.add(idUsuarioField);
            panel.add(usuarioLabel);
            panel.add(usuarioField);
            panel.add(contrasenaLabel);
            panel.add(contrasenaField);
            panel.add(nombreLabel);
            panel.add(nombreField);
            panel.add(apellidoLabel);
            panel.add(apellidoField);
            panel.add(new JLabel()); // Leave an empty space for alignment
            panel.add(registrarseButton);
            panel.add(volverButton);

            add(panel);

            setVisible(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registrarseButton) {
            String idUsuario = idUsuarioField.getText();
            String usuario = usuarioField.getText();
            char[] contrasena = contrasenaField.getPassword();
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();

            if (idUsuario.isEmpty() || usuario.isEmpty() || contrasena.length == 0 || nombre.isEmpty() || apellido.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            } else {
                if (existeUsuario(idUsuario, usuario)) {
                    JOptionPane.showMessageDialog(this, "El nombre de usuario o ID ya existe.");
                } else {
                    if (insertarUsuario(idUsuario, usuario, new String(contrasena), nombre, apellido)) {
                        JOptionPane.showMessageDialog(this, "Registro exitoso");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al registrar el usuario.");
                    }
                }
            }
        } else if (e.getSource() == volverButton) {
            dispose();
            LogIn loginForm = new LogIn();
            loginForm.setVisible(true);
        }
    }

    private boolean existeUsuario(String idUsuario, String usuario) {
        try {
            String consulta = "SELECT * FROM UserProfile WHERE idUser = ? OR username = ?";
            PreparedStatement statement = cn.prepareStatement(consulta);
            statement.setString(1, idUsuario);
            statement.setString(2, usuario);

            ResultSet resultado = statement.executeQuery();

            return resultado.next();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private boolean insertarUsuario(String idUsuario, String usuario, String contrasena, String nombre, String apellido) {
        try {
            String consulta = "INSERT INTO UserProfile (idUser, username, password, name, lastname, registerDate) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = cn.prepareStatement(consulta);
            statement.setString(1, idUsuario);
            statement.setString(2, usuario);
            statement.setString(3, contrasena);
            statement.setString(4, nombre);
            statement.setString(5, apellido);
            statement.setTimestamp(6, new Timestamp(new Date().getTime()));

            int filasAfectadas = statement.executeUpdate();

            return filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Register();
        });
    }
}
