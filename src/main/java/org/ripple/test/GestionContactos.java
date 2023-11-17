package org.ripple.test;

import org.ripple.ui.Home;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        frame.setSize(520, 740);
        frame.getContentPane().setBackground(Color.decode("#00AAFF"));

        // Crear un JPanel para contener los componentes
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Crear un JPanel para los encabezados de las columnas
        JPanel headerPanel = new JPanel(new GridLayout(1, 3));
        headerPanel.setBackground(Color.WHITE);

        // Agregar etiquetas de encabezado para cada columna
        JLabel nombreLabel = new JLabel("Nombre");
        JLabel apellidoLabel = new JLabel("Apellido");
        JLabel telefonoLabel = new JLabel("Teléfono");

        // Añadir los encabezados al panel de encabezados
        headerPanel.add(nombreLabel);
        headerPanel.add(apellidoLabel);
        headerPanel.add(telefonoLabel);

        // Agregar el panel de encabezados al panel principal
        panel.add(headerPanel, BorderLayout.NORTH);

        // Crear un JList para mostrar la lista de contactos
        JList<String> contactList = new JList<>(contactListModel);
        contactList.setBackground(Color.decode("#00AAFF"));
        contactList.setSelectionBackground(Color.BLUE);
        panel.add(new JScrollPane(contactList), BorderLayout.CENTER);

        // Crear un panel para los botones de agregar, eliminar, editar, guardar y regresar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        // Crear un botón para agregar contactos
        JButton addButton = new JButton("Agregar");
        addButton.setBackground(Color.WHITE);
        addButton.setForeground(Color.BLACK);
        addButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(addButton);

        // Crear un botón para eliminar contactos
        JButton deleteButton = new JButton("Eliminar");
        deleteButton.setBackground(Color.WHITE);
        deleteButton.setForeground(Color.BLACK);
        deleteButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(deleteButton);

        // Crear un botón para editar contactos
        JButton editButton = new JButton("Editar");
        editButton.setBackground(Color.WHITE);
        editButton.setForeground(Color.BLACK);
        editButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(editButton);

        // Crear un botón para guardar contactos en la base de datos
        JButton saveButton = new JButton("Guardar en Base de Datos");
        saveButton.setBackground(Color.WHITE);
        saveButton.setForeground(Color.BLACK);
        saveButton.setPreferredSize(new Dimension(150, 30));
        buttonPanel.add(saveButton);

        // Crear un botón para regresar a la ventana Home
        JButton backButton = new JButton("Regresar");
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(backButton);

        // Agregar un ActionListener al botón para abrir la ventana de "Agregar Contacto"
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AgregarContacto agregarContacto = new AgregarContacto(contactListModel);
                agregarContacto.setVisible(true);
            }
        });

        // Agregar un ActionListener al botón para eliminar contactos
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = contactList.getSelectedIndex();
                if (selectedIndex != -1) {
                    int confirm = JOptionPane.showConfirmDialog(frame, "¿Estás seguro de que deseas eliminar este contacto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        contactListModel.remove(selectedIndex);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecciona un contacto para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Agregar un ActionListener al botón para editar contactos
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = contactList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedContact = contactListModel.getElementAt(selectedIndex);
                    EditarContacto editarContacto = new EditarContacto(selectedContact, contactListModel, selectedIndex);
                    editarContacto.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecciona un contacto para editar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Agregar un ActionListener al botón para guardar contactos en la base de datos
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = contactList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedContact = contactListModel.getElementAt(selectedIndex);
                    String[] contactData = selectedContact.split(" ");

                    // Conectar a la base de datos (Asegúrate de que ya tengas la conexión configurada)
                    Connection conn = null;
                    try {
                        conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/rippledb", "root", "Ragnar2105");

                        // Insertar el contacto en la tabla "Contacts"
                        String insertContactQuery = "INSERT INTO Contacts (firstName, lastName) VALUES (?, ?)";
                        PreparedStatement contactStatement = conn.prepareStatement(insertContactQuery);
                        contactStatement.setString(1, contactData[0]);
                        contactStatement.setString(2, contactData[1]);
                        contactStatement.executeUpdate();

                        // Obtener el ID del contacto insertado
                        ResultSet generatedKeys = contactStatement.getGeneratedKeys();
                        int contactID = -1;
                        if (generatedKeys.next()) {
                            contactID = generatedKeys.getInt(1);
                        }

                        // Insertar el número de teléfono en la tabla "Phone" y relacionarlo con el contacto en "Phone_Contacts"
                        String insertPhoneQuery = "INSERT INTO Phone (phoneNumber) VALUES (?)";
                        PreparedStatement phoneStatement = conn.prepareStatement(insertPhoneQuery);
                        phoneStatement.setString(1, contactData[2]);
                        phoneStatement.executeUpdate();

                        // Obtener el ID del número de teléfono insertado
                        ResultSet generatedPhoneKeys = phoneStatement.getGeneratedKeys();
                        int phoneID = -1;
                        if (generatedPhoneKeys.next()) {
                            phoneID = generatedPhoneKeys.getInt(1);
                        }

                        // Relacionar el número de teléfono con el contacto en "Phone_Contacts"
                        String insertPhoneContactQuery = "INSERT INTO Phone_Contacts (idPhone, idContacts) VALUES (?, ?)";
                        PreparedStatement phoneContactStatement = conn.prepareStatement(insertPhoneContactQuery);
                        phoneContactStatement.setInt(1, phoneID);
                        phoneContactStatement.setInt(2, contactID);
                        phoneContactStatement.executeUpdate();

                        JOptionPane.showMessageDialog(frame, "Contacto y número de teléfono guardados en la base de datos.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error al guardar el contacto en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        if (conn != null) {
                            try {
                                conn.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecciona un contacto para guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Agregar un ActionListener al botón para regresar a la ventana Home
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Home home = new Home();
                home.setVisible(true);
            }
        });

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }
}
