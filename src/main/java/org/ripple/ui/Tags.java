package org.ripple.ui;

import desplazable.Desface;
import org.ripple.util.CircularButton;
import org.ripple.util.SideMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

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
                getClass().getResource("/images/drawable-action/sliders.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //-------------Configuracion resolucion-------------//
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        DisplayMode mode = gd.getDisplayMode();

        int screenWidth = mode.getWidth();
        int screenHeight = mode.getHeight();
        int smallerDimension = Math.min(screenWidth, screenHeight - 42);

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
        mainPanel.setLayout(new BorderLayout());

        // Agregar el nuevo panel blanco con margen
        JPanel tagsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int arcWidth = 60;
                int arcHeight = 60;
                RoundRectangle2D roundRect = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.WHITE);
                g2d.fill(roundRect);
            }
        };
        tagsPanel.setOpaque(false);

        Icon addButtonIcon = new ImageIcon(getClass().getResource("/images/drawable-action/plus.png"));
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        getLayeredPane().add(addButton, JLayeredPane.PALETTE_LAYER);

        getContentPane().add(mainPanel);
        setVisible(true);
    }
}
