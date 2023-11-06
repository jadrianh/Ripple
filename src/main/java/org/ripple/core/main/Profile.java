package org.ripple.core.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.net.URI;

public class Profile extends JFrame {
    public Profile() {
        initializeUI();
    }

    private static void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    private void initializeUI() {
        //--------------Configuracion principal--------------//
        setTitle("Perfil");
        setMinimumSize(new Dimension(520, 980));
        setLayout(new BorderLayout(0, 100));
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo.png")));
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
        JPanel profilePanel = new JPanel() {
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
        profilePanel.setOpaque(false);

        int margin = 30;
        mainPanel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));

        // Crear el panel para los botones en la parte superior
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        // Crear etiquetas (JLabels) con imágenes y establecer su tamaño
        JButton backButton = new JButton(new ImageIcon(getClass().getResource("/images/Core/chevron-left.png")));
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

        JButton trashButton = new JButton(new ImageIcon(getClass().getResource("/images/Core/trash.png")));
        trashButton.setPreferredSize(new Dimension(60, 60));
        trashButton.setOpaque(true);
        trashButton.setFocusable(false);
        trashButton.setContentAreaFilled(false);
        trashButton.setBorderPainted(false);
        trashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show a confirmation dialog
                int response = JOptionPane.showConfirmDialog(null, "Are you sure about this?", "Confirmation", JOptionPane.YES_NO_OPTION);

                // Check the user's response
                if (response == JOptionPane.YES_OPTION) {
                    // The user clicked Yes
                } else if (response == JOptionPane.NO_OPTION) {
                    // The user clicked No
                }
            }
        });


        JButton editButton = new JButton(new ImageIcon(getClass().getResource("/images/Core/edit.png")));
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
        rightButtonPanel.add(trashButton);
        rightButtonPanel.add(editButton);
        rightButtonPanel.setOpaque(false);

        buttonPanel.add(leftButtonPanel, BorderLayout.WEST);
        buttonPanel.add(rightButtonPanel, BorderLayout.EAST);

        // Crea el contactPanel
        JPanel contactPanel = new JPanel();
        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
        contactPanel.setOpaque(false);

        JLabel contactLabel = new JLabel("Perfil");
        contactLabel.setFont(new Font("Arial", Font.BOLD, 26));
        contactLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/profile.png"));
        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance(150, 150,  java.awt.Image.SCALE_SMOOTH);
        final ImageIcon finalImageIcon = new ImageIcon(newImage);

        JLabel profilePicLabel = new JLabel(finalImageIcon){
            @Override
            protected void paintComponent(Graphics g) {
                if (g instanceof Graphics2D) {
                    final int R = 80;
                    final int G = 80;
                    final int borderThickness = 2;
                    final int borderOffset = borderThickness / 2;
                    final int imageWidth = finalImageIcon.getIconWidth();
                    final int imageHeight = finalImageIcon.getIconHeight();

                    final Graphics2D g2d = (Graphics2D) g.create();
                    applyQualityRenderingHints(g2d);

                    // Dibuja el borde del circulo
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.setStroke(new BasicStroke(borderThickness));
                    g2d.drawOval(borderOffset, borderOffset, R * 2 - borderThickness, G * 2 - borderThickness);

                    g2d.setClip(new Ellipse2D.Double(borderOffset, borderOffset, R * 2 - borderThickness, G * 2 - borderThickness));
                    g2d.drawImage(finalImageIcon.getImage(), R - imageWidth / 2, G - imageHeight / 2, null);

                    g2d.dispose();
                } else {
                    super.paintComponent(g);
                }
            }
        };
        profilePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel contactNameLabel = new JLabel("Susana Navarro");
        contactNameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        contactNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel phoneNumberLabel = new JLabel("+503 7519-2425");
        phoneNumberLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        phoneNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel birthDayLabel = new JLabel("24/09/98");
        birthDayLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        birthDayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel addressLabel = new JLabel("Carretera Comalapa, Calle Miramar, Col. Montecristo, Casa No#26");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel addressPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int arcWidth = 60;
                int arcHeight = 60;
                RoundRectangle2D roundRect = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fill(roundRect);
            }
        };

        addressPanel.setLayout(new BorderLayout());
        addressPanel.setOpaque(false);
        addressPanel.setPreferredSize(new Dimension(400, 70));
        addressPanel.setMaximumSize(new Dimension(460, 80));
        addressPanel.add(addressLabel, BorderLayout.CENTER);

        // Crear un nuevo panel con fondo azul
        JPanel socialMediaPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int arcWidth = 50; // Ajusta el radio de las esquinas redondeadas
                int arcHeight = 50; // Ajusta el radio de las esquinas redondeadas
                RoundRectangle2D roundRect = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.decode("#00AAFF"));
                g2d.fill(roundRect);
            }
        };
        socialMediaPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        socialMediaPanel.setPreferredSize(new Dimension(400, 90));
        socialMediaPanel.setMaximumSize(new Dimension(400, 90));
        socialMediaPanel.setOpaque(false);

        // Crear y configurar botones individuales
        JButton mailButton = new JButton(new ImageIcon(getClass().getResource("/images/Core/drawable-icons/mail.png")));
        JButton facebookButton = new JButton(new ImageIcon(getClass().getResource("/images/Core/drawable-icons/facebook.png")));
        JButton instagramButton = new JButton(new ImageIcon(getClass().getResource("/images/Core/drawable-icons/instagram.png")));
        JButton whatsappButton = new JButton(new ImageIcon(getClass().getResource("/images/Core/drawable-icons/phone.png")));
        JButton twitterButton = new JButton(new ImageIcon(getClass().getResource("/images/Core/drawable-icons/twitter.png")));

        // Crear un ActionListener para los botones de redes sociales
        ActionListener socialMediaButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = null;

                if (e.getSource() == mailButton) {
                    url = "https://www.ejemplo.com/correo";
                } else if (e.getSource() == facebookButton) {
                    url = "https://www.facebook.com/ejemplo";
                } else if (e.getSource() == instagramButton) {
                    url = "https://www.instagram.com/ejemplo";
                } else if (e.getSource() == whatsappButton) {
                    url = "https://api.whatsapp.com/send?phone=NUMERO_TELEFONO";
                } else if (e.getSource() == twitterButton) {
                    url = "https://twitter.com/ejemplo";
                }

                if (url != null) {
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        mailButton.setBorderPainted(false);
        mailButton.setContentAreaFilled(false);
        mailButton.setFocusPainted(false);

        facebookButton.setBorderPainted(false);
        facebookButton.setContentAreaFilled(false);
        facebookButton.setFocusPainted(false);

        instagramButton.setBorderPainted(false);
        instagramButton.setContentAreaFilled(false);
        instagramButton.setFocusPainted(false);

        whatsappButton.setBorderPainted(false);
        whatsappButton.setContentAreaFilled(false);
        whatsappButton.setFocusPainted(false);

        twitterButton.setBorderPainted(false);
        twitterButton.setContentAreaFilled(false);
        twitterButton.setFocusPainted(false);

        // Asignar ActionListener a cada botón
        mailButton.addActionListener(socialMediaButtonListener);
        facebookButton.addActionListener(socialMediaButtonListener);
        instagramButton.addActionListener(socialMediaButtonListener);
        whatsappButton.addActionListener(socialMediaButtonListener);
        twitterButton.addActionListener(socialMediaButtonListener);

        // Actualizar el bucle para agregar botones al socialMediaPanel
        for (JButton button : new JButton[]{mailButton, facebookButton, instagramButton, whatsappButton, twitterButton}) {
            JPanel socialMediaButtonPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    int size = Math.min(getWidth(), getHeight());
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.WHITE);
                    g2d.fill(new Ellipse2D.Double(0, 0, size, size));
                }
            };
            socialMediaButtonPanel.setPreferredSize(new Dimension(60, 60));
            socialMediaButtonPanel.setLayout(new BorderLayout());
            socialMediaButtonPanel.setOpaque(false);

            socialMediaButtonPanel.add(button, BorderLayout.CENTER);
            socialMediaPanel.add(socialMediaButtonPanel);
        }


        // Agregar el panel de botones en la parte superior del profilePanel
        profilePanel.setLayout(new BorderLayout());
        profilePanel.add(buttonPanel, BorderLayout.NORTH);

        mainPanel.add(profilePanel, BorderLayout.CENTER);

        // Add the labels to the contact panel
        contactPanel.add(contactLabel);
        contactPanel.add(profilePicLabel);
        contactPanel.add(contactNameLabel);
        contactPanel.add(phoneNumberLabel);
        contactPanel.add(birthDayLabel);
        contactPanel.add(addressPanel);
        contactPanel.add(socialMediaPanel);

        profilePanel.add(contactPanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        setVisible(true);
    }
}
