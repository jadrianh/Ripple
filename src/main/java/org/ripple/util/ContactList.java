package org.ripple.util;

import org.ripple.connection.CConexion;
import org.ripple.entities.Contact;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactList extends JPanel {

    private List<Contact> contacts;
    private int visibleContacts;
    private int scrollPosition;

    // Constructor de la clase ContactList
    public ContactList() {
        // Inicializa la lista de contactos y otros atributos
        this.contacts = new ArrayList<>();
        this.scrollPosition = 0;
        this.visibleContacts = 4;
        setOpaque(false);

        // Cambia de FlowLayout a BoxLayout con orientación vertical
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        loadContactsFromDatabase();
        updateContactPanels();
        setScrollPosition(0);
    }

    private void loadContactsFromDatabase() {
        Connection dbConnection = null;

        try {
            CConexion conexion = new CConexion();
            try {
                conexion.establecerConection();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al conectar a la Base de Datos");
            }

            if (dbConnection != null) {
                String selectQuery = "SELECT c.idContacts, c.firstName, c.lastName, p.phoneNumber "
                        + "FROM Contacts c "
                        + "JOIN Phone_Contacts pc ON c.idContacts = pc.idContacts "
                        + "JOIN Phone p ON pc.idPhone = p.idPhone";
                try (PreparedStatement preparedStatement = dbConnection.prepareStatement(selectQuery); ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        String firstName = resultSet.getString("firstName");
                        String lastName = resultSet.getString("lastName");
                        String phoneNumber = resultSet.getString("phoneNumber");

                        Contact contact = new Contact(firstName, lastName, phoneNumber);
                        contacts.add(contact);
                    }
                    System.out.println("Number of contacts loaded: " + contacts.size());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
        updateContactPanels();
    }

    private void updateContactPanels() {
        removeAll();

        for (int i = scrollPosition; i < Math.min(scrollPosition + visibleContacts, contacts.size()); i++) {
            Contact contact = contacts.get(i);
            ContactPanel contactPanel = new ContactPanel(contact);
            add(contactPanel);

            if (i < visibleContacts - 1) {
                add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        revalidate();
        repaint();
    }

    public int getContactCount() {
        return contacts.size();
    }
}