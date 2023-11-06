package org.ripple.core.main;

import desplazable.Desface;
import org.ripple.CConexion;
import org.ripple.core.GestionContactos;
import org.ripple.core.start.LogIn;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.ripple.core.Profile;
import org.ripple.core.start.LogIn;

//////////// Clase que representa un contacto ////////////
class Contact {

    private String name;
    private String phoneNumber;
    private BufferedImage profileImage;

    // Constructor de la clase Contact
    public Contact(String name, String phoneNumber, String profileImagePath) {
        // Inicializa un contacto con su nombre, número de teléfono y una imagen de perfil
        this.name = name;
        this.phoneNumber = phoneNumber;
        loadProfileImage(profileImagePath);
    }

    private void loadProfileImage(String imagePath) {
        // Carga la imagen de perfil desde un archivo
        try {
            profileImage = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            // Manejo de errores si la carga de la imagen falla
            e.printStackTrace();
        }
    }

    // Métodos para obtener información del contacto
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public BufferedImage getProfileImage() {
        return profileImage;
    }
}

//////////// Panel que representa visualmente un contacto ////////////
class ContactPanel extends JPanel {

    private final Contact contact;

    // Constructor de la clase ContactPanel
    public ContactPanel(Contact contact) {
        // Inicializa el panel con un contacto y configura acciones de clic
        this.contact = contact;
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Clickeo sobre: " + contact.getName());
                Profile profile = new Profile();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Dibuja la representación visual del contacto en cada panel
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        int arc = 40;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, width, height, arc, arc);

        BufferedImage profileImage = contact.getProfileImage();
        int imageSize = 0;
        if (profileImage != null) {
            imageSize = height - 40;
            int x = 20;
            int y = 20;
            g2d.setClip(new Ellipse2D.Double(x, y, imageSize, imageSize));
            g2d.drawImage(profileImage, x, y, imageSize, imageSize, null);
            g2d.setClip(null);
        }

        // Formato de elemento: Nombre
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        String name = contact.getName();
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

//////////// Panel que muestra una lista de contactos ////////////
class ContactList extends JPanel {

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
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        loadContactsFromDatabase(); // Cargar contactos desde la base de datos
        updateContactPanels();
    }

    private void loadContactsFromDatabase() {
        // Carga contactos desde una base de datos (MySQL)
        Connection dbConnection = null;

        try {
            String jdbcURL = "jdbc:mysql://localhost:3307/rippledb";
            String user = "root";
            String password = "Ragnar2105";

            dbConnection = DriverManager.getConnection(jdbcURL, user, password);

            if (dbConnection != null) {
                Statement statement = dbConnection.createStatement();
                String selectQuery = "SELECT c.firstName, p.phoneNumber FROM Contacts c INNER JOIN Phone p ON c.firstName = p.phoneNumber";
                ResultSet resultSet = statement.executeQuery(selectQuery);

                while (resultSet.next()) {
                    String firstName = resultSet.getString("firstName");
                    String phoneNumber = resultSet.getString("phoneNumber");

                    Contact contact = new Contact(firstName, phoneNumber, null); // No hay imagen por ahora
                    contacts.add(contact);
                }

                resultSet.close();
                statement.close();
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
            contactPanel.setLocation(20, 100 * i); // Corrección: Cambiar 100 * i a 100
            add(contactPanel);

            if (i < visibleContacts - 1) {
                add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        revalidate();
        repaint();
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
    private int scrollPosition;

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
                getClass().getResource("/images/logo.png")));
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

        // Configuración del botón "Búsqueda" (seacrhButton)
        JButton searchButton = new JButton(new ImageIcon(getClass().getResource("/images/Core/search.png")));
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
                    String searchText = searchBar.getText();
                    JOptionPane.showMessageDialog(Home.this, "Searching for: " + searchText);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Configuración del botón "Agregar" (addButton)
        Icon addButtonIcon = new ImageIcon(getClass().getResource("/images/Core/plus.png"));
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
                    GestionContactos.main(new String[0]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        getLayeredPane().add(addButton, JLayeredPane.PALETTE_LAYER);

        // Configuración de la lista de contactos (contactList)
        contactList = new ContactList();
        contactList.setBounds(20, 100, 520, 400);
        getContentPane().add(contactList);

        // Configuración del menú lateral (sideMenu)
        sideMenu = new SideMenu();
        sideMenu.setBounds(-180, 0, 180, getHeight());
        getLayeredPane().add(sideMenu, JLayeredPane.PALETTE_LAYER);

        // Configuración del botón de menú para desplegar el menú lateral
        JButton menuButton = new JButton(new ImageIcon(getClass().getResource("/images/Core/menu.png")));
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
        topPanel.add(searchButton, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.EAST);

        // Agregar componentes al panel principal (mainPanel)
        mainPanel.add(topPanel, BorderLayout.NORTH);

        getContentPane().add(mainPanel);
        setVisible(true);

        // Manejo de redimensionamiento de la ventana
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
    }
}
