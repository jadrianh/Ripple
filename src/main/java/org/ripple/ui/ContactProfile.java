package org.ripple.ui;

import org.ripple.entities.PhoneNumber;
import org.ripple.util.*;

import javax.swing.*;
import java.awt.*;

public class ContactProfile extends JFrame {
    private int iconSize = 32;
    public ContactProfile(){
        initializeUI();
    }

    private void initializeUI() {
        //--------------Configuracion principal--------------//
        setTitle("Perfil del contacto");
        setMinimumSize(new Dimension(520, 980));
        setLayout(null);
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
                g2d.fill(new Rectangle(0, 0, getWidth(), getHeight()));
            }
        };
        mainPanel.setPreferredSize(new Dimension(520, 190));
        mainPanel.setLayout(new BorderLayout());
        //mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        int margin = 10;
        mainPanel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));

        JPanel whitePanel = new JPanel();
        whitePanel.setPreferredSize(new Dimension(520, 860));
        whitePanel.setBackground(Color.WHITE);

        // Crear el panel para los botones en la parte superior
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        ClickEffectButton backButton = new ClickEffectButton("/images/drawable-navigation/chevron-left-alt.png", 44, 44);

        backButton.addActionListener(e -> {
            Home home = new Home();
            dispose();
        });

        // Configurar la disposición del panel de botones izquierdo
        JPanel leftButtonPanel = new JPanel();
        leftButtonPanel.add(backButton);
        leftButtonPanel.setOpaque(false);

        // Agregar botón de edición en la esquina superior derecha
        ClickEffectButton editButton = new ClickEffectButton("/images/drawable-action/edit-alt.png", 44, 44);

        editButton.addActionListener(e -> {
            // Lógica de edición
            System.out.println("Botón de edición presionado");
        });

        // Agregar botón de edición en la esquina superior derecha
        ClickEffectButton trashButton = new ClickEffectButton("/images/drawable-action/trash-alt.png", 44, 44);

        trashButton.addActionListener(e -> {
            // Muestra un cuadro de diálogo de confirmación
            int response = JOptionPane.showConfirmDialog(
                    null, "¿Estás seguro de que deseas eliminar este contacto?", "Confirmación", JOptionPane.YES_NO_OPTION);

            // Verifica la respuesta del usuario
            if (response == JOptionPane.YES_OPTION) {
                // El usuario hizo clic en Sí
            } else if (response == JOptionPane.NO_OPTION) {
                // El usuario hizo clic en No
            }
        });

        // Configurar la disposición del panel de botones derecho
        JPanel rightButtonPanel = new JPanel();
        rightButtonPanel.add(editButton);
        rightButtonPanel.add(trashButton);
        rightButtonPanel.setOpaque(false);

        // Crear una imagen de ejemplo (reemplázala con tu lógica de carga de imágenes)
        Image profileImage = new ImageIcon(getClass().getResource("/images/drawable-pictures/steamuserimages-a.akamaihd.gif")).getImage();

        // Crear el panel de imagen con bordes redondeados
        PicturePanel picturePanel = new PicturePanel(profileImage, 180, 180);
        picturePanel.setOpaque(false);

        // Configurar la capa del PicturePanel y su posición
        int xPosition = 30;
        int yPosition = 150;
        JLayeredPane layeredPane = getLayeredPane();
        layeredPane.add(picturePanel, JLayeredPane.PALETTE_LAYER);
        picturePanel.setBounds(xPosition, yPosition, 180, 180);

        // Crear JLabels
        JLabel nameLabel = new JLabel("Nombre" + " " + "Apellido");
        nameLabel.setFont(CustomFontManager.getCustomFontMedium(26, true));
        JLabel nickNameLabel = new JLabel("Nickname");
        nickNameLabel.setFont(CustomFontManager.getCustomFont(20, false));

        Image companyImage = new ImageIcon(getClass().getResource("/images/drawable-indication/briefcase.png")).getImage();
        Image companyIconImage = companyImage.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        JLabel companyIcon = new JLabel(new ImageIcon(companyIconImage));

        JLabel companyLabel = new JLabel("Empresa");
        companyLabel.setFont(CustomFontManager.getCustomFont(20, false));

        Image birthdayImage = new ImageIcon(getClass().getResource("/images/drawable-indication/gift.png")).getImage();
        Image birthdayIconImage = birthdayImage.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        JLabel birthdayIcon = new JLabel(new ImageIcon(birthdayIconImage));

        JLabel birthdayLabel = new JLabel("12/09/2003");
        birthdayLabel.setFont(CustomFontManager.getCustomFont(20, false));

        // Crear un panel redondeado para la dirección
        RoundedPanel addressPanel = new RoundedPanel(50);
        addressPanel.setLayout(new BorderLayout());
        addressPanel.setPreferredSize(new Dimension(240, 240));
        addressPanel.setBackground(Color.decode("#E5E4E2"));

        // Añadir un JLabel para la dirección con saltos de línea
        JLabel addressLabel = new JLabel("<html>Dirección Línea 1<br>Dirección Línea 2</html>");
        addressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        addressLabel.setVerticalAlignment(SwingConstants.NORTH);
        addressPanel.add(addressLabel, BorderLayout.CENTER);

        nameLabel.setBounds(240, 220, 300, 30);
        nickNameLabel.setBounds(240, 250, 200, 30);
        companyIcon.setBounds(30, 430, iconSize, iconSize);
        companyLabel.setBounds(75, 430, 160, 30);
        birthdayIcon.setBounds(30, 470, iconSize, iconSize);
        birthdayLabel.setBounds(75, 470, 160, 30);
        addressPanel.setBounds(240, 350, 240, 240);

        PhoneNumber[] phoneNumbers = PhoneNumber.getSamplePhoneNumbers();

        int yOffset = 350;
        for (PhoneNumber phone : phoneNumbers) {
            // Agregar el ícono del teléfono
            Image phoneImage = new ImageIcon(getClass().getResource("/images/drawable-indication/phone.png")).getImage();
            Image scaledIconImage = phoneImage.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            JLabel phoneIcon = new JLabel(new ImageIcon(scaledIconImage));
            phoneIcon.setBounds(30, yOffset, iconSize, iconSize);
            add(phoneIcon);

            // Agregar el número de teléfono
            JLabel phoneLabel = new JLabel(phone.getPhoneSuffix() + " " + phone.getPhoneNumber());
            phoneLabel.setFont(CustomFontManager.getCustomFont(20, false));
            phoneLabel.setBounds(70, yOffset, 160, 30);
            add(phoneLabel);

            // Incrementar el desplazamiento vertical para el siguiente conjunto de JLabel y JIcon
            yOffset += 40;
        }

        // Crear el panel de redes sociales
        SocialNetworkPanel socialNetworkPanel = new SocialNetworkPanel();
        socialNetworkPanel.setOpaque(false);

        // Crear un JPanel contenedor para el panel de redes sociales
        JPanel socialPanelContainer = new JPanel();
        socialPanelContainer.setOpaque(false);
        socialPanelContainer.add(socialNetworkPanel);

        // Agregar el panel contenedor al JLayeredPane
        int xPositionForSocialPanel = 0; // Ajusta la posición según tus necesidades
        int yPositionForSocialPanel = 510; // Ajusta la posición según tus necesidades
        JLayeredPane socialLayeredPane = getLayeredPane();
        socialLayeredPane.add(socialPanelContainer, JLayeredPane.PALETTE_LAYER);
        socialPanelContainer.setBounds(xPositionForSocialPanel, yPositionForSocialPanel, 244, 150);


        // Agregar los JLabels al JFrame
        add(nameLabel);
        add(nickNameLabel);
        add(companyIcon);
        add(companyLabel);
        add(birthdayIcon);
        add(birthdayLabel);
        add(addressPanel);

        // Set layout for the main frame
        setLayout(new BorderLayout());

        // Agregar los paneles de botones al panel principal
        buttonPanel.add(leftButtonPanel, BorderLayout.WEST);
        buttonPanel.add(rightButtonPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.NORTH);
        add(whitePanel, BorderLayout.SOUTH);

        // Display the frame
        setVisible(true);
    }

    private void addSocialNetworkPanel(SocialNetworkPanel socialNetworkPanel) {
        // Agregar el panel de redes sociales a tu diseño
        // (Ajusta las coordenadas y tamaños según sea necesario)
        socialNetworkPanel.setBounds(10, 10, 200, 240);
        add(socialNetworkPanel);
    }
}
