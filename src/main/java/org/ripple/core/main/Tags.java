package org.ripple.core.main;

import desplazable.Desface;
import org.ripple.core.GestionContactos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tags extends JFrame {
    private Desface desplace;
    private boolean menuVisible = false;
    private SideMenu sideMenu;
    public Tags() {
        desplace = new Desface();
        initializeUI();
    }
    private void initializeUI(){
        //--------------Configuracion principal--------------//
        setTitle("Etiquetas");
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

        //---------------Configuracion panel---------------//
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

        Icon addButtonIcon = new ImageIcon(getClass().getResource("/images/Core/plus.png"));
        CircularButton addButton = new CircularButton(addButtonIcon);
        int addButtonX = getWidth() - 80 - 30;
        int addButtonY = getHeight() - 80 - 50;
        addButton.setBounds(addButtonX, addButtonY, 80, 80);
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
                    } else {
                        desplace.desplazarIzquierda(sideMenu, sideMenu.getX(), -180, 10, 5);
                        menuVisible = false;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(menuButton, BorderLayout.NORTH); // Aquí usamos BorderLayout.WEST

        mainPanel.add(topPanel, BorderLayout.WEST);

        getContentPane().add(mainPanel);
        setVisible(true);
    }
}
