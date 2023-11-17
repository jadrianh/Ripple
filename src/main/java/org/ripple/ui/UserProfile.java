package org.ripple.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class UserProfile extends JFrame{

    public UserProfile(){
        initializeUI();
    }

    private void initializeUI() {
        //--------------Configuracion principal--------------//
        setTitle("Perfil");
        setMinimumSize(new Dimension(520, 980));
        setLayout(new BorderLayout(0, 100));
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/images/drawable-icons/icon.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
                g2d.fill(new Rectangle(0, 0, getWidth(), getHeight()));
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Agregar el nuevo panel blanco con margen
        JPanel whitePanel = new JPanel() {
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
        whitePanel.setOpaque(false);

        int margin = 30;
        mainPanel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));

        // Crear el panel para los botones en la parte superior
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        // Crear etiquetas (JLabels) con imágenes y establecer su tamaño
        JButton backButton = new JButton(new ImageIcon(getClass().getResource("/images/drawable-navigation/chevron-left.png")));
        backButton.setPreferredSize(new Dimension(60, 60));
        backButton.setOpaque(false);
        backButton.setFocusable(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home home = new Home();
                dispose();
            }
        });

        JButton editButton = new JButton(new ImageIcon(getClass().getResource("/images/drawable-action/edit.png")));
        editButton.setPreferredSize(new Dimension(60, 60));
        editButton.setOpaque(true);
        editButton.setFocusable(false);
        editButton.setContentAreaFilled(false);
        editButton.setBorderPainted(false);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your code here to handle the edit button action
            }
        });

        // Configurar la disposición del panel de botones
        JPanel leftButtonPanel = new JPanel();
        leftButtonPanel.add(backButton);
        leftButtonPanel.setOpaque(false);

        JPanel rightButtonPanel = new JPanel();
        rightButtonPanel.add(editButton);
        rightButtonPanel.setOpaque(false);

        buttonPanel.add(leftButtonPanel, BorderLayout.WEST);
        buttonPanel.add(rightButtonPanel, BorderLayout.EAST);

        // Crea el userPanel
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Perfil");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 25));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        userPanel.add(titleLabel);

        whitePanel.setLayout(new BorderLayout());

        mainPanel.add(whitePanel);
        whitePanel.add(buttonPanel);
        whitePanel.add(userPanel);

        getContentPane().add(mainPanel);
        setVisible(true);
    }
}
