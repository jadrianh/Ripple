/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ripple.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestionContactos {
    private static DefaultListModel<String> contactListModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            contactListModel = new DefaultListModel<>(); // Inicializa el modelo
            crearInterfaz();
        });
    }

    private static void crearInterfaz() {
        // Crear un nuevo marco (JFrame)
        JFrame frame = new JFrame("Gestión de Contactos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 740); // Cambiar altura y ancho a un cuadrado
        frame.getContentPane().setBackground(Color.decode("#00AAFF")); // Cambiar el color de fondo del contenido del JFrame

        // Crear un JPanel para contener los componentes
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE); // Cambiar el color de fondo del panel a blanco

        // Crear un JLabel para mostrar "Gestión de contactos"
        JLabel label = new JLabel("Gestión de contactos");
        label.setHorizontalAlignment(JLabel.CENTER); // Centrar el texto en el JLabel
        label.setFont(new Font("Arial", Font.BOLD, 24)); // Cambiar la fuente y el tamaño del texto
        label.setForeground(Color.BLACK); // Cambiar el color del texto a negro
        panel.add(label, BorderLayout.NORTH);

        // Crear un JList para mostrar la lista de contactos
        JList<String> contactList = new JList<>(contactListModel); // Utiliza el modelo global
        contactList.setBackground(Color.decode("#00AAFF")); // Cambiar el color de fondo de la lista al mismo del JFrame
        contactList.setSelectionBackground(Color.BLUE); // Cambiar el color de fondo de la selección
        panel.add(new JScrollPane(contactList), BorderLayout.CENTER);

        // Crear un botón para agregar contactos
        JButton addButton = new JButton("Agregar Contacto");
        addButton.setBackground(Color.WHITE); // Cambiar el color de fondo del botón a blanco
        addButton.setForeground(Color.BLACK); // Cambiar el color del texto a negro
        panel.add(addButton, BorderLayout.SOUTH);

        // Crear un botón para eliminar contactos
        JButton deleteButton = new JButton("Eliminar Contacto");
        deleteButton.setBackground(Color.WHITE); // Cambiar el color de fondo del botón a blanco
        deleteButton.setForeground(Color.BLACK); // Cambiar el color del texto a negro
        panel.add(deleteButton, BorderLayout.WEST);

        // Agregar un ActionListener al botón para abrir la ventana de "Agregar Contacto"
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AgregarContacto agregarContacto = new AgregarContacto(contactListModel); // Pasa el modelo al constructor
                agregarContacto.setVisible(true);
            }
        });

        // Agregar un ActionListener al botón para eliminar contactos
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = contactList.getSelectedIndex();
                if (selectedIndex != -1) {
                    contactListModel.remove(selectedIndex);
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecciona un contacto para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Agregar el panel al marco
        frame.add(panel);

        // Hacer visible el marco
        frame.setVisible(true);
    }
}
