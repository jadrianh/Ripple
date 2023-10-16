package org.ripple.core;

import desplazable.Desface;
import org.ripple.CConexion;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ProfileDataException;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class Contact {
    private String name;
    private String phoneNumber;
    private BufferedImage profileImage;

    public Contact(String name, String phoneNumber, String profileImagePath) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        loadProfileImage(profileImagePath);
    }

    private void loadProfileImage(String imagePath) {
        try {
            profileImage = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            // Manejar la excepción adecuadamente, por ejemplo, lanzando una excepción personalizada
            e.printStackTrace();
        }
    }

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

class ContactPanel extends JPanel {
    private final Contact contact;

    public ContactPanel(Contact contact) {
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
            g2d.setClip(null); // Restablecer el clip
        }

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        String name = contact.getName();
        int textX = imageSize + 40; // Comenzar el texto después de la imagen
        int textY = height / 4 + g2d.getFontMetrics().getHeight() / 2; // Centrar verticalmente en la parte superior
        g2d.drawString(name, textX, textY);

        int lineY = height / 2 - 9;
        int lineStartX = textX; // Inicio de la línea
        int lineEndX = width - 300; // Fin de la línea
        g2d.setColor(Color.GRAY);
        g2d.drawLine(lineStartX, lineY, lineEndX, lineY);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        String phoneNumber = contact.getPhoneNumber();
        textY = (height + lineY) / 3 + g2d.getFontMetrics().getHeight() / 2; // Centrar verticalmente entre la línea y la parte inferior
        g2d.drawString(phoneNumber, textX, textY);
    }
}

class ContactList extends JPanel {
    private List<Contact> contacts;
    private int visibleContacts;
    private int scrollPosition;

    public ContactList() {
        this.contacts = new ArrayList<>();
        this.scrollPosition = 0;
        this.visibleContacts = 4;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Corrección: Agregar un paréntesis adicional
        loadContactsFromDatabase(); // Cargar contactos desde la base de datos
        updateContactPanels();
    }

    private void loadContactsFromDatabase() {
        CConexion conexion = new CConexion();
        Connection dbConnection = conexion.establecerConexion();

        if (dbConnection != null) {
            try {
                String query = "SELECT name, phoneNumber FROM contacts";
                PreparedStatement statement = dbConnection.prepareStatement(query);
                ResultSet result = statement.executeQuery();

                while (result.next()) {
                    String name = result.getString("name");
                    String phoneNumber = result.getString("phoneNumber");
                    Contact contact = new Contact(name, phoneNumber, null); // No hay imagen por ahora
                    contacts.add(contact);
                }

                result.close();
                statement.close();
                dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
        updateContactPanels();
    }

    private void updateContactPanelsFromDatabase() {
        removeAll();

        try {
            String jdbcURL = "jdbc:mysql://localhost:3306/c";
            String user = "root";
            String password = "Ragnar2105";

            Connection connection = DriverManager.getConnection(jdbcURL, user, password);
            Statement statement = connection.createStatement();

            String selectQuery = "SELECT name, phoneNumber FROM contacts";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phoneNumber");

                Contact contact = new Contact(name, phoneNumber, null); // You may load profile image if available
                contacts.add(contact);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            // Manejo de excepciones relacionadas con la base de datos
            e.printStackTrace();
        }

        for (Contact contact : contacts) {
            ContactPanel contactPanel = new ContactPanel(contact);
            add(contactPanel);
            add(Box.createRigidArea(new Dimension(0, 10))); // Corrección: Agregar un paréntesis adicional y punto y coma al final
        }

        revalidate();
        repaint();
    }

    private void updateContactPanels() {
        removeAll();

        for (int i = scrollPosition; i < Math.min(scrollPosition + visibleContacts, contacts.size()); i++) {
            Contact contact = contacts.get(i);
            ContactPanel contactPanel = new ContactPanel(contact);
            contactPanel.setLocation(20 + 100 * i, 100);
            add(contactPanel);

            if (i < visibleContacts - 1) {
                add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        revalidate();
        repaint();
    }
}

// Creacion de la clase RoundedPanel
class RoundedPanel extends JPanel {
    private final int arc;

    public RoundedPanel(int arc) {
        this.arc = arc;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
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

class CircularButton extends JButton {
    private static final int BUTTON_SIZE = 50;

    public CircularButton(Icon icon) {
        setIcon(icon);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusable(false);
        setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1));
        super.paintComponent(g2);
        g2.dispose();
    }
}

class SideMenu extends JLayeredPane {
    private boolean menuVisible = false;
    private SideMenu sideMenu;
    private Desface desplace;

    public boolean isMenuVisible() {
        return menuVisible;
    }

    public void setMenuVisible(boolean menuVisible) {
        this.menuVisible = menuVisible;
        firePropertyChange("menuVisible", !menuVisible, menuVisible);
    }

    public SideMenu getMenuDesplegable() {
        return sideMenu;
    }

    public void setMenuDesplegable(SideMenu sideMenu) {
        this.sideMenu = sideMenu;
    }

    public SideMenu() {
        setLocation(0, 0);
        setOpaque(true);
        setBackground(new Color(255, 255, 255));

        ImageIcon appIconMenu = new ImageIcon(getClass().getResource("/images/ripple.png"));
        Image appImageMenu = appIconMenu.getImage().getScaledInstance(140, 36, Image.SCALE_SMOOTH);
        appIconMenu = new ImageIcon(appImageMenu);
        JLabel appLabelMenu = new JLabel(appIconMenu);
        appLabelMenu.setHorizontalAlignment(SwingConstants.CENTER);

        appLabelMenu.setBounds(0, 10, 180, 34);
        add(appLabelMenu, Integer.valueOf(1));

        String[] buttonInfo = {"/images/Core/user.png", "Perfil", "/images/Core/hash.png", "Etiquetas", "/images/Core/sliders.png", "Ajustes", "/images/Core/log-out.png", "Logout"};

        for (int i = 0; i < buttonInfo.length; i += 2) {
            JButton button = createNavButton(buttonInfo[i], buttonInfo[i + 1]);
            button.setBounds(-30, 80 + i * 45, 180, 45);
            add(button, Integer.valueOf(2));
        }
    }

    private JButton createNavButton(String iconPath, String label) {
        JButton button = new JButton();
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusable(false);

        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        icon = new ImageIcon(icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
        Font font = new Font("Roboto", Font.PLAIN, 20);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(font);
        labelComponent.setForeground(Color.decode("#00AAFF"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(new JLabel(icon));
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(labelComponent);

        button.add(buttonPanel);

        switch (label) {
            case "Perfil":
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    }
                });
                break;
            case "Etiquetas":
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    }
                });
                break;
            case "Ajustes":
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Settings settings = new Settings();
                        settings.setVisible(true);
                    }
                });
                break;
            case "Logout":
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Crear un JOptionPane
                        int response = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres salir de la aplicación?", "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                        // Verificar la respuesta del usuario
                        if (response == JOptionPane.YES_OPTION) {
                            LogIn login = new LogIn();
                        }
                    }
                });
                break;

            default:
                break;
        }

        return button;
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

        JButton searchButton = new JButton(new ImageIcon(getClass().getResource("/images/Core/search.png")));
        searchButton.setPreferredSize(new Dimension(40, 40));
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);
        searchButton.setFocusable(false);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        RoundedPanel searchPanel = new RoundedPanel(42);
        searchPanel.setPreferredSize(new Dimension(330, 30));

        JTextField searchBar = new JTextField();
        searchBar.setOpaque(false);
        searchBar.setBorder(BorderFactory.createEmptyBorder());
        searchBar.setPreferredSize(new Dimension(300, 30));
        searchBar.setForeground(Color.decode("#C0C0C0"));
        searchBar.setCaretColor(Color.decode("#C0C0C0"));

        Font font = new Font("Verdana", Font.PLAIN, 18);
        searchBar.setFont(font);

        searchPanel.add(searchBar, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String searchText = searchBar.getText();
                    JOptionPane.showMessageDialog(Home.this, "Searching for: " + searchText);
                } catch (Exception ex) {
                    // Manejo de excepciones relacionadas con la búsqueda
                    ex.printStackTrace();
                }
            }
        });

        Icon addButtonIcon = new ImageIcon(getClass().getResource("/images/Core/user-plus.png"));
        CircularButton addButton = new CircularButton(addButtonIcon);

        int addButtonX = getWidth() - 80 - 30;
        int addButtonY = getHeight() - 80 - 50;
        addButton.setBounds(addButtonX, addButtonY, 80, 80);
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Abre el formulario
                    dispose();
                    GestionContactos.main(new String[0]);
                } catch (Exception ex) {
                    // Manejo de excepciones relacionadas con la apertura del formulario
                    ex.printStackTrace();
                }
            }
        });
        getLayeredPane().add(addButton, JLayeredPane.PALETTE_LAYER);

        contactList = new ContactList();
        contactList.setBounds(20, 100, 520, 400);
        getContentPane().add(contactList);

        sideMenu = new SideMenu();
        sideMenu.setBounds(-180, 0, 180, getHeight());
        getLayeredPane().add(sideMenu, JLayeredPane.PALETTE_LAYER);

        JButton menuButton = new JButton(new ImageIcon(getClass().getResource("/images/Core/menu.png")));
        menuButton.setPreferredSize(new Dimension(40, 40));
        menuButton.setContentAreaFilled(false);
        menuButton.setBorderPainted(false);
        menuButton.setFocusable(false);
        menuButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        menuButton.setMargin(new Insets(5, 5, 5, 5));

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

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(menuButton, BorderLayout.WEST);
        topPanel.add(searchButton, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        getContentPane().add(mainPanel);
        setVisible(true);

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
