package org.ripple.ui;

import desplazable.Desface;
import org.ripple.test.GestionContactos;
import org.ripple.util.CircularButton;
import org.ripple.util.ContactList;
import org.ripple.util.RoundedPanel;
import org.ripple.util.SideMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
                    String searchText = searchBar.getText();
                    JOptionPane.showMessageDialog(Home.this, "Searching for: " + searchText);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
        topPanel.add(searchButton, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.EAST);

        // Agregar componentes al panel principal (mainPanel)
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel);

        getContentPane().add(mainPanel);
        setVisible(true);
    }
}
