package org.ripple.core;

import desplazable.Desface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

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

    private static final int BUTTON_SIZE = 50; // Adjust the button size as needed

    public CircularButton(Icon icon) {
        setIcon(icon);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusable(false);
        setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE)); // Set the button size
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

        String[] buttonInfo = {"/images/Core/home.png", "Home", "/images/Core/user.png", "Perfil", "/images/Core/hash.png", "Eventos", "/images/Core/sliders.png", "Ajustes", "/images/Core/log-out.png", "Logout"};

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

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        return button;
    }
}

public class Home extends JFrame {
    private Desface desplace;
    private boolean menuVisible = false;
    private SideMenu sideMenu;

    public Home() {
        initializeUI();
        desplace = new Desface();
    }

    private void initializeUI() {
        setTitle("Ripple");
        setSize(520, 980);
        setMinimumSize(new Dimension(520, 400));
        setLayout(new BorderLayout(0, 100));
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/images/logo.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setResizable(false);
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
                if (!menuVisible) {
                    desplace.desplazarIzquierda(sideMenu, sideMenu.getX(), -180, 10, 5);
                    menuVisible = true;
                } else {
                    desplace.desplazarDerecha(sideMenu, sideMenu.getX(), 0, 10, 5);
                    menuVisible = false;
                }
            }
        });

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
                String searchText = searchBar.getText();
                // Implement your search logic here using the 'searchText'
                // For example, display a message with the search text:
                JOptionPane.showMessageDialog(Home.this, "Searching for: " + searchText);
            }
        });

        Icon addButtonIcon = new ImageIcon(getClass().getResource("/images/Core/user-plus.png"));
        CircularButton addButton = new CircularButton(addButtonIcon);

        addButton.setBounds(400, 840, 80, 80);
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        getLayeredPane().add(addButton, JLayeredPane.PALETTE_LAYER);


        sideMenu = new SideMenu();
        sideMenu.setBounds(0, 0, 180, getHeight());

        getLayeredPane().add(sideMenu, JLayeredPane.PALETTE_LAYER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(menuButton, BorderLayout.WEST);
        topPanel.add(searchButton, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        getContentPane().add(mainPanel);
        setVisible(true);
    }
}
