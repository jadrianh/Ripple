package org.ripple.core;

import desplazable.Desface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

class RoundedPanel extends JPanel {
    private int arc;

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

        // Fondo blanco
        graphics.setColor(Color.WHITE);
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
    }
}

class MenuDesplegable extends JPanel {

    public MenuDesplegable() {
        setBackground(new Color(0, 99, 183));
        setLayout(new BorderLayout());

        // Icono de la aplicación en el menú desplegable (ajustado a 64x64)
        ImageIcon appIconMenu = new ImageIcon(getClass().getResource("/images/logo.png"));
        Image appImageMenu = appIconMenu.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        appIconMenu = new ImageIcon(appImageMenu);
        JLabel appLabelMenu = new JLabel(appIconMenu);
        appLabelMenu.setHorizontalAlignment(SwingConstants.CENTER);
        add(appLabelMenu, BorderLayout.NORTH);

        // Panel de botones de navegación
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setOpaque(false);

        // Botones de navegación
        String[] buttonInfo = {"/images/Core/home.png", "Home", "/images/Core/user.png", "Perfil", "/images/Core/log-out.png", "Logout"};

        for (int i = 0; i < buttonInfo.length; i += 2) {
            JButton button = createNavButton(buttonInfo[i], buttonInfo[i + 1]);
            navPanel.add(button);
        }

        add(navPanel, BorderLayout.WEST);
    }

    private JButton createNavButton(String iconPath, String label) {
        JButton button = new JButton();
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusable(false);

        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        icon = new ImageIcon(icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
        button.setIcon(icon);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setForeground(Color.WHITE);
        labelComponent.setHorizontalAlignment(SwingConstants.CENTER);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Agregar lógica para manejar la acción del botón aquí
            }
        });

        // Agregar el label al botón
        button.setLayout(new BorderLayout());
        button.add(labelComponent, BorderLayout.SOUTH);

        return button;
    }
}

public class Home extends JFrame {
    Desface desplace;
    private boolean menuVisible = false;
    private MenuDesplegable menuDesplegable;

    public Home() {
        initializeUI();
        desplace = new Desface();
    }

    private void initializeUI() {
        //--------------Configuracion principal--------------//
        setTitle("Ripple");
        setSize(520, 740);
        setMinimumSize(new Dimension(520, 400));
        setLayout(new BorderLayout(0, 100));
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                Objects.requireNonNull(getClass().getResource("/images/logo.png"))));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        // Crear el botón de menú con el icono
        JButton menuButton = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/Core/menu.png"))));
        menuButton.setPreferredSize(new Dimension(40, 40));
        menuButton.setContentAreaFilled(false);
        menuButton.setBorderPainted(false);
        menuButton.setFocusable(false);
        menuButton.setMargin(new Insets(5, 5, 5, 5));

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!menuVisible) {
                    desplace.desplazarIzquierda(menuDesplegable, menuDesplegable.getX(), -240, 10, 4);
                    menuVisible = true;
                } else {
                    desplace.desplazarDerecha(menuDesplegable, menuDesplegable.getX(), 0, 10, 4);
                    menuVisible = false;
                }
            }
        });
        
        
        

        // Espacio vacío para separar el botón de menú de los otros elementos
        Component rigidArea = Box.createRigidArea(new Dimension(10, 40)); // Ajusta el valor para controlar la separación

        // Crear el botón de búsqueda con el icono de lupa y tamaño de 40x40
        JButton searchButton = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/Core/search.png"))));
        searchButton.setPreferredSize(new Dimension(40, 40));
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);
        searchButton.setFocusable(false);

        // Crear un panel redondeado para la barra de búsqueda con bordes más redondeados
        RoundedPanel searchPanel = new RoundedPanel(42); // Redondeo extremo
        searchPanel.setPreferredSize(new Dimension(330, 12)); // Tamaño del panel (mínimo 40px de alto)

        // Crear la barra de búsqueda y configurarla
        JTextField searchBar = new JTextField();
        searchBar.setOpaque(false);
        searchBar.setBorder(BorderFactory.createEmptyBorder()); // Sin borde
        searchBar.setPreferredSize(new Dimension(310, 30));
        searchBar.setForeground(Color.GRAY); // Color de texto gris
        searchBar.setCaretColor(Color.GRAY); // Color del cursor gris

        // Configurar la fuente a Arial 12
        Font font = new Font("Arial", Font.PLAIN, 15);
        searchBar.setFont(font);

        // Agregar ActionListener para el botón de búsqueda
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Agregar aquí la lógica para realizar la búsqueda
                // Por ejemplo, obtener el texto ingresado en searchBar.getText()
            }
        });

        // Agregar la barra de búsqueda al panel redondeado
        searchPanel.add(searchBar);

        // Agregar el menu a la ventana principal
        menuDesplegable = new MenuDesplegable();
        menuDesplegable.setPreferredSize(new Dimension(80, getHeight()));
        menuDesplegable.setLocation(0, 0);
        mainPanel.add(menuDesplegable, BorderLayout.WEST);

        // Crear un panel para contener los elementos de la parte superior
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(menuButton, BorderLayout.WEST); // Agregar botón de menú
        topPanel.add(searchButton, BorderLayout.CENTER); // Agregar botón de búsqueda
        topPanel.add(searchPanel, BorderLayout.EAST); // Agregar panel de búsqueda

        // Agregar el panel superior a la ventana principal
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Establecer el z-order del menú desplegable para que esté en la parte superior
        mainPanel.setComponentZOrder(menuDesplegable, 0);

        getContentPane().add(mainPanel);
        setVisible(true);
    }
}