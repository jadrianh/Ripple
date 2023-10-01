package org.ripple.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditarContacto extends JFrame {
    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField telefonoField;
    private JButton actualizarButton;
    private DefaultListModel<String> contactListModel;
    private int selectedIndex;

    public EditarContacto(String selectedContact, DefaultListModel<String> contactListModel, int selectedIndex) {
        this.contactListModel = contactListModel;
        this.selectedIndex = selectedIndex;
        setTitle("Editar Contacto");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreField = new JTextField();
        JLabel apellidoLabel = new JLabel("Apellido:");
        apellidoField = new JTextField();
        JLabel telefonoLabel = new JLabel("Teléfono:");
        telefonoField = new JTextField();

        actualizarButton = new JButton("Actualizar");
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener los nuevos valores del contacto
                String nuevoNombre = nombreField.getText();
                String nuevoApellido = apellidoField.getText();
                String nuevoTelefono = telefonoField.getText();

                // Modificar el contacto en el modelo
                String nuevoContacto = nuevoNombre + " " + nuevoApellido + " - " + nuevoTelefono;
                contactListModel.set(selectedIndex, nuevoContacto);

                // Cerrar la ventana de edición
                dispose();
            }
        });

        panel.add(nombreLabel);
        panel.add(nombreField);
        panel.add(apellidoLabel);
        panel.add(apellidoField);
        panel.add(telefonoLabel);
        panel.add(telefonoField);
        panel.add(new JLabel()); // Espacio en blanco
        panel.add(actualizarButton);

        add(panel);
    }
}
