package org.ripple.test;
import org.ripple.ui.LogIn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Registro extends JFrame implements ActionListener {
    private JTextField usuarioField, nombreField, apellidoField, numeroField, fechaNacimientoField;
    private JButton registrarseButton, volverButton;

    private Color backgroundColor = new Color(0, 170, 255); // Color de fondo
    private Color buttonColor = new Color(0, 167, 248); // Color de botón
    private Color buttonTextColor = Color.WHITE; // Color de texto del botón
    private Color labelTextColor = Color.BLACK; // Color de texto de etiquetas
    private Color separatorColor = Color.BLACK; // Color del separador

    public Registro() {
        setTitle("Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));
        panel.setBackground(backgroundColor); // Color de fondo

        JLabel usuarioLabel = new JLabel("Usuario:");
        JLabel nombreLabel = new JLabel("Nombre:");
        JLabel apellidoLabel = new JLabel("Apellido:");
        JLabel numeroLabel = new JLabel("Número:");
        JLabel fechaNacimientoLabel = new JLabel("Fecha de Nacimiento:");
        usuarioLabel.setForeground(labelTextColor); // Color de texto de etiquetas
        nombreLabel.setForeground(labelTextColor);
        apellidoLabel.setForeground(labelTextColor);
        numeroLabel.setForeground(labelTextColor);
        fechaNacimientoLabel.setForeground(labelTextColor);

        usuarioField = new JTextField();
        nombreField = new JTextField();
        apellidoField = new JTextField();
        numeroField = new JTextField();
        fechaNacimientoField = new JTextField();

        registrarseButton = new JButton("Registrarse");
        registrarseButton.addActionListener(this);
        registrarseButton.setPreferredSize(new Dimension(100, 30)); // Establecer el tamaño
        registrarseButton.setBackground(buttonColor); // Color de botón
        registrarseButton.setForeground(buttonTextColor); // Color de texto del botón

        volverButton = new JButton("Volver");
        volverButton.addActionListener(this);
        volverButton.setPreferredSize(new Dimension(100, 30)); // Establecer el tamaño
        volverButton.setBackground(buttonColor); // Color de botón
        volverButton.setForeground(buttonTextColor); // Color de texto del botón

        panel.add(usuarioLabel);
        panel.add(usuarioField);
        panel.add(nombreLabel);
        panel.add(nombreField);
        panel.add(apellidoLabel);
        panel.add(apellidoField);
        panel.add(numeroLabel);
        panel.add(numeroField);
        panel.add(fechaNacimientoLabel);
        panel.add(fechaNacimientoField);
        panel.add(registrarseButton);
        panel.add(volverButton);

        add(panel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registrarseButton) {
            // Aquí puedes agregar el código para registrar al usuario
            // Puedes acceder a los valores de los campos de texto con usuarioField.getText(), nombreField.getText(), etc.
            // Realiza la lógica de registro según tus necesidades.
            // Por ahora, mostraremos un mensaje de registro exitoso.
            JOptionPane.showMessageDialog(this, "Registro exitoso");
        } else if (e.getSource() == volverButton) {
            // Cierra la ventana de registro
            dispose();

            // Crea una instancia de la ventana de inicio de sesión (LogIn)
            LogIn loginForm = new LogIn();
            // Muestra la ventana de inicio de sesión
            loginForm.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Registro());
    }
}
