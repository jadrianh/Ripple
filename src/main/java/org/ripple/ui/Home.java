package org.ripple.ui;

import desplazable.Desface;
import org.ripple.test.GestionContactos;
import org.ripple.util.CircularButton;
import org.ripple.util.SideMenu;

import desplazable.Desface;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import org.ripple.connection.CConexion;

class Contact {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private BufferedImage profileImage;

    private static final List<String> IMAGE_PATHS = List.of(
            "/images/drawable-pictures/profile00.png",
            "/images/drawable-pictures/profile01.png",
            "/images/drawable-pictures/profile02.png",
            "/images/drawable-pictures/profile03.png",
            "/images/drawable-pictures/profile04.png",
            "/images/drawable-pictures/profile05.png",
            "/images/drawable-pictures/profile06.png",
            "/images/drawable-pictures/profile07.png",
            "/images/drawable-pictures/profile08.png",
            "/images/drawable-pictures/profile09.png",
            "/images/drawable-pictures/profile10.png",
            "/images/drawable-pictures/profile11.png",
            "/images/drawable-pictures/profile12.png"
    );

    public Contact(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        assignRandomProfileImage();
    }

    private void assignRandomProfileImage() {
        Random random = new Random();
        int randomIndex = random.nextInt(IMAGE_PATHS.size());
        String imagePath = IMAGE_PATHS.get(randomIndex);

        // Imprime la ruta antes de intentar cargar la imagen
        System.out.println("Image path: " + imagePath);

        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                this.profileImage = ImageIO.read(imageUrl);
            } else {
                System.err.println("Error: Image not found - " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading image: " + e.getMessage());
        }
    }

    public BufferedImage getProfileImage() {
        return profileImage;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

class ContactPanel extends JPanel {

    private final Contact contact;
    private static final int PANEL_HEIGHT = 100; // Establece la altura fija del panel

    // Constructor de la clase ContactPanel
    public ContactPanel(Contact contact) {
        // Inicializa el panel con un contacto y configura acciones de clic
        this.contact = contact;
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            // Agrega acciones de clic si es necesario
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Dibuja la representación visual del contacto en cada panel
        super.paintComponent(g);

        int width = getWidth();
        int height = PANEL_HEIGHT; // Utiliza la altura fija
        int arc = 40;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, width, height, arc, arc);

        BufferedImage profileImage = contact.getProfileImage();
        int imageSize = height - 40;
        int x = 20;
        int y = (height - imageSize) / 2; // Centra la imagen verticalmente

        try {
            g2d.setClip(new Ellipse2D.Double(x, y, imageSize, imageSize));
            g2d.drawImage(profileImage, x, y, imageSize, imageSize, null);
            g2d.setClip(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Formato de elemento: Nombre y Apellido
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        String name = contact.getFirstName() + " " + contact.getLastName();
        int textX = imageSize + 40;
        int textY = height / 4 + g2d.getFontMetrics().getHeight() / 2;
        g2d.drawString(name, textX, textY);

        // Formato de elemento: Linea divisora
        int lineY = height / 2 - 9;
        int lineStartX = textX;
        int lineEndX = width - 300;
        g2d.setColor(Color.GRAY);
        g2d.drawLine(lineStartX, lineY, lineEndX, lineY);

        // Formato de elemento: Numero de telefono
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        String phoneNumber = contact.getPhoneNumber();
        textY = (height + lineY) / 3 + g2d.getFontMetrics().getHeight() / 2;
        g2d.drawString(phoneNumber, textX, textY);
    }
}

class ContactList extends JPanel {

    private List<Contact> contacts;
    private int visibleContacts;
    private int scrollPosition;

    // Constructor de la clase ContactList
    public ContactList() {
        // Inicializa la lista de contactos y otros atributos
        this.contacts = new ArrayList<>();
        this.scrollPosition = 0;
        this.visibleContacts = 200;
        setOpaque(false);

        // Cambia de FlowLayout a BoxLayout con orientación vertical
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        loadContactsFromDatabase(); // Cargar contactos desde la base de datos
        updateContactPanels();
        setScrollPosition(0);  // Asegúrate de llamar a setScrollPosition después de cargar los contactos
    }

    private void loadContactsFromDatabase() {
        Connection dbConnection = null;

        try {
            CConexion conexion = new CConexion();
            dbConnection = conexion.establecerConection();

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

//////////// Creacion de la clase RoundedPanel ////////////
class RoundedPanel extends JPanel {

    private final int arc;

    // Constructor de la clase RoundedPanel
    public RoundedPanel(int arc) {
        // Inicializa el panel con un radio específico para bordes redondeados
        this.arc = arc;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Dibuja un panel redondeado con bordes redondeados
        super.paintComponent(g);
        Dimension arcs = new Dimension(arc, arc);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(Color.WHITE);
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
    }
}

public class Home extends JFrame {

    private Desface desplace;
    private boolean menuVisible = false;
    private SideMenu sideMenu;
    private ContactList contactList;

    public Home() {
        initializeUI();
        desplace = new Desface();
    }

    private void initializeUI() {
        //--------------Configuracion principal--------------//
        setTitle("Ripple");
        setMinimumSize(new Dimension(520, 980));
        setLayout(new BorderLayout(0, 100));
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/images/drawable-icons/icon.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //-------------Configuracion resolucion-------------//
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        DisplayMode mode = gd.getDisplayMode();

        int screenWidth = mode.getWidth();
        int screenHeight = mode.getHeight();
        int smallerDimension = Math.min(screenWidth, screenHeight);

        int newWidth = smallerDimension * 520 / 980;
        int newHeight = smallerDimension;

        setSize(newWidth, newHeight);
        setLocationRelativeTo(null);

        //---------------Configuracion mainPanel---------------//
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
        Dimension size = mainPanel.getSize();

        // Configuración del botón "Búsqueda" (seacrhButton)
        JButton searchButton = new JButton(new ImageIcon(getClass().getResource("/images/drawable-action/search.png")));
        searchButton.setPreferredSize(new Dimension(40, 40));
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);
        searchButton.setFocusable(false);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Configuración del panel de búsqueda redondeado
        RoundedPanel searchPanel = new RoundedPanel(42);
        searchPanel.setPreferredSize(new Dimension(330, 30));

        // Configuración del campo de búsqueda
        JTextField searchBar = new JTextField();
        searchBar.setOpaque(false);
        searchBar.setBorder(BorderFactory.createEmptyBorder());
        searchBar.setPreferredSize(new Dimension(300, 30));
        searchBar.setForeground(Color.decode("#C0C0C0"));
        searchBar.setCaretColor(Color.decode("#C0C0C0"));

        Font font = new Font("Verdana", Font.PLAIN, 18);
        searchBar.setFont(font);
        searchPanel.add(searchBar, BorderLayout.CENTER);

        // Manejo de eventos del botón de búsqueda
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String searchText = searchBar.getText().toLowerCase();

                    // Realizar la búsqueda en la base de datos
                    List<Contact> resultadosBusqueda = searchContactsInDatabase(searchText);

                    // Limpiar la lista actual de contactos en la interfaz
                    contactList.removeAll();

                    // Agregar paneles de contacto para cada resultado de búsqueda
                    for (Contact resultado : resultadosBusqueda) {
                        ContactPanel contactPanel = new ContactPanel(resultado);
                        contactList.add(contactPanel);
                        contactList.add(Box.createRigidArea(new Dimension(0, 10))); // Espaciado entre paneles
                    }

                    // Volver a validar y repintar la lista de contactos
                    contactList.revalidate();
                    contactList.repaint();

                    // Mostrar o manejar los resultados de búsqueda según sea necesario
                    if (!resultadosBusqueda.isEmpty()) {
                        for (Contact resultado : resultadosBusqueda) {
                            System.out.println("Encontrado: " + resultado.getFirstName() + " " + resultado.getLastName());
                        }
                    } else {
                        JOptionPane.showMessageDialog(Home.this, "No se encontraron contactos para: " + searchText);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            private List<Contact> searchContactsInDatabase(String searchText) {
                List<Contact> resultadosBusqueda = new ArrayList<>();
                Connection dbConnection = null;

                try {
                    CConexion conexion = new CConexion();
                    dbConnection = conexion.establecerConection();

                    if (dbConnection != null) {
                        String selectQuery = "SELECT c.idContacts, c.firstName, c.lastName, p.phoneNumber "
                                + "FROM Contacts c "
                                + "JOIN Phone_Contacts pc ON c.idContacts = pc.idContacts "
                                + "JOIN Phone p ON pc.idPhone = p.idPhone "
                                + "WHERE LOWER(c.firstName) LIKE ? OR LOWER(c.lastName) LIKE ?";
                        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(selectQuery)) {
                            preparedStatement.setString(1, "%" + searchText + "%");
                            preparedStatement.setString(2, "%" + searchText + "%");

                            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                                while (resultSet.next()) {
                                    String firstName = resultSet.getString("firstName");
                                    String lastName = resultSet.getString("lastName");
                                    String phoneNumber = resultSet.getString("phoneNumber");

                                    Contact contact = new Contact(firstName, lastName, phoneNumber);
                                    resultadosBusqueda.add(contact);
                                }
                            }
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

                return resultadosBusqueda;
            }
        });

        // Configuración del botón "Agregar" (addButton)
        Icon addButtonIcon = new ImageIcon(getClass().getResource("/images/drawable-action/plus.png"));
        CircularButton addButton = new CircularButton(addButtonIcon);
        int addButtonX = getWidth() - 80 - 30;
        int addButtonY = getHeight() - 80 - 50;
        addButton.setBounds(addButtonX, addButtonY, 80, 80);
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Manejo de eventos del botón agregar
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dispose();
                    AddContact addContact = new AddContact();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        getLayeredPane().add(addButton, JLayeredPane.PALETTE_LAYER);

        // Manejo del addButton con el redimensionamiento de la ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                try {
                    int addButtonX = getWidth() - 80 - 30;
                    int addButtonY = getHeight() - 80 - 50;
                    addButton.setBounds(addButtonX, addButtonY, 80, 80);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Configuración de la lista de contactos (contactList)
        contactList = new ContactList();
        contactList.setBounds(20, 100, 520, 400);
        getContentPane().add(contactList);

        // Desplegar el panel de noContacts
        JLabel noContactsLabel = new JLabel("Todavía no tienes contactos");
        noContactsLabel.setForeground(Color.WHITE);
        noContactsLabel.setFont(new Font("Verdana", Font.PLAIN, 16));

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/drawable-status/contact-agenda.png"));
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setLocation(size.width / 2 - centerPanel.getWidth() / 2, size.height / 2 - centerPanel.getHeight() / 2);
        centerPanel.add(imageLabel);
        centerPanel.add(noContactsLabel, BorderLayout.SOUTH);

        if (contactList.getContactCount() == 0) {
            // No hay contactos, mostrar la señal visual
            noContactsLabel.setVisible(true);
            contactList.setVisible(false);
        } else {
            // Hay contactos, mostrar la lista de contactos
            noContactsLabel.setVisible(false);
            contactList.setVisible(true);
        }

        // Configuración del menú lateral (sideMenu)
        sideMenu = new SideMenu();
        sideMenu.setBounds(-180, 0, 180, getHeight());
        getLayeredPane().add(sideMenu, JLayeredPane.PALETTE_LAYER);

        // Establecer la referencia de Home en SideMenu
        sideMenu.setHomeReference(this);

        // Configuración del botón de menú para desplegar el menú lateral
        JButton menuButton = new JButton(new ImageIcon(getClass().getResource("/images/drawable-action/menu.png")));
        menuButton.setPreferredSize(new Dimension(40, 40));
        menuButton.setContentAreaFilled(false);
        menuButton.setBorderPainted(false);
        menuButton.setFocusable(false);
        menuButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        menuButton.setMargin(new Insets(5, 5, 5, 5));

        // Manejo de eventos del botón de menú
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!menuVisible) {
                        desplace.desplazarDerecha(sideMenu, sideMenu.getX(), 0, 10, 5);
                        menuVisible = true;

                        searchButton.setEnabled(false);
                        searchBar.setEnabled(false);
                    } else {
                        desplace.desplazarIzquierda(sideMenu, sideMenu.getX(), -180, 10, 5);
                        menuVisible = false;

                        searchButton.setEnabled(true);
                        searchBar.setEnabled(true);
                    }
                } catch (Exception ex) {
                    // Manejo de excepciones relacionadas con el desplazamiento del menú
                    ex.printStackTrace();
                }
            }
        });

        // Configuración del panel superior (topPanel)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(menuButton, BorderLayout.WEST);
        topPanel.add(searchButton, BorderLayout.EAST);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        // Agregar componentes al panel principal (mainPanel)
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);  // Asegúrate de que centerPanel esté en el centro

        getContentPane().add(mainPanel);
        setVisible(true);
    }
}
