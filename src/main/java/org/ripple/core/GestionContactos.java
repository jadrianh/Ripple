package org.ripple.core;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.ripple.core.main.Home;

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

        // Crear un JPanel para los encabezados de las columnas
        JPanel headerPanel = new JPanel(new GridLayout(1, 3)); // 1 fila y 3 columnas para los encabezados
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
        JList<String> contactList = new JList<>(contactListModel); // Utiliza el modelo global
        contactList.setBackground(Color.decode("#00AAFF")); // Cambiar el color de fondo de la lista al mismo del JFrame
        contactList.setSelectionBackground(Color.BLUE); // Cambiar el color de fondo de la selección
        panel.add(new JScrollPane(contactList), BorderLayout.CENTER);

        // Crear un panel para los botones de agregar, eliminar, editar y regresar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Espacio entre botones
        buttonPanel.setBackground(Color.WHITE);

        // Crear un botón para agregar contactos
        JButton addButton = new JButton("Agregar");
        addButton.setBackground(Color.WHITE); // Cambiar el color de fondo del botón a blanco
        addButton.setForeground(Color.BLACK); // Cambiar el color del texto a negro
        addButton.setPreferredSize(new Dimension(100, 30)); // Tamaño 100x30
        buttonPanel.add(addButton);

        // Crear un botón para eliminar contactos
        JButton deleteButton = new JButton("Eliminar");
        deleteButton.setBackground(Color.WHITE); // Cambiar el color de fondo del botón a blanco
        deleteButton.setForeground(Color.BLACK); // Cambiar el color del texto a negro
        deleteButton.setPreferredSize(new Dimension(100, 30)); // Tamaño 100x30
        buttonPanel.add(deleteButton);

        // Crear un botón para editar contactos
        JButton editButton = new JButton("Editar");
        editButton.setBackground(Color.WHITE); // Cambiar el color de fondo del botón a blanco
        editButton.setForeground(Color.BLACK); // Cambiar el color del texto a negro
        editButton.setPreferredSize(new Dimension(100, 30)); // Tamaño 100x30
        buttonPanel.add(editButton);

        // Crear un botón para regresar a la ventana Home
        JButton backButton = new JButton("Regresar");
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setPreferredSize(new Dimension(100, 30)); // Tamaño 100x30
        buttonPanel.add(backButton);

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
                    // Obtener el contacto seleccionado
                    String selectedContact = contactListModel.getElementAt(selectedIndex);

                    // Abrir la ventana de edición para modificar el contacto
                    EditarContacto editarContacto = new EditarContacto(selectedContact, contactListModel, selectedIndex);
                    editarContacto.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecciona un contacto para editar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
      //es aqui???
        // Agregar un ActionListener al botón para regresar a la ventana Home
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Cierra la ventana actual
                // Aquí debes crear una instancia de la ventana Home y mostrarla
                Home home = new Home();
                home.setVisible(true);
            }
        });

        // Agregar el panel de botones al panel principal
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Hacer visible el marco
        frame.add(panel);
        frame.setVisible(true);
    }
}
