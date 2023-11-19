package org.ripple.ui;

import org.ripple.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class AddContact extends JFrame {
    private JLabel profilePictureLabel;
    private JPanel phoneNumbersPanel;
    private JPanel socialMediaPanel;
    private Process process;
    private static final int MAX_PHONE_NUMBER_PANELS = 2;

    public AddContact(){
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
        //---------------Configuracion principal---------------//
        // Configuración de la ventana de agregar contacto, incluyendo título, tamaño y diseño.
        setTitle("Agregar Contacto");
        setMinimumSize(new Dimension(520, 980));
        setLayout(new BorderLayout(0, 100));
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/images/drawable-icons/icon.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //---------------Configuracion resolucion---------------//
        // Ajusta el tamaño y la ubicación de la ventana en función de la resolución de la pantalla.
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

        //---------------Configuracion mainPanel---------------//
        // Configuración del panel principal que contiene los elementos de la ventana.
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

        //---------------Configuracion del panel blanco---------------//
        // Configura un panel blanco con bordes redondeados como contenedor principal.
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

        //---------------Configuracion del panel de botones---------------//
        // Configura el panel de botones en la parte superior de la ventana.
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        // Crear el backButton
        ClickEffectButton backButton = new ClickEffectButton("/images/drawable-navigation/chevron-left.png", 45, 45);

        backButton.addActionListener(e -> {
            Home home = new Home();
            dispose();
        });

        // Crea el doneButton
        ClickEffectButton doneButton = new ClickEffectButton("/images/drawable-action/check.png", 45, 45);

        doneButton.addActionListener(e -> {
            // Manejo de la acción del botón de listo
        });

        // Configurar la disposición del panel de botones
        JPanel leftButtonPanel = new JPanel();
        leftButtonPanel.add(backButton);
        leftButtonPanel.setOpaque(false);

        JPanel rightButtonPanel = new JPanel();
        rightButtonPanel.add(doneButton);
        rightButtonPanel.setOpaque(false);

        buttonPanel.add(leftButtonPanel, BorderLayout.WEST);
        buttonPanel.add(rightButtonPanel, BorderLayout.EAST);

        //---------------Configuracion del panel de añadir contacto---------------//
        // Configura el panel que contiene la información del contacto y los componentes.
        JPanel addContactPanel = new JPanel();
        addContactPanel.setLayout(new BoxLayout(addContactPanel, BoxLayout.Y_AXIS));
        addContactPanel.setOpaque(false);

        // Crea el titleLabel
        JLabel titleLabel = new JLabel("Agregar Contacto");
        titleLabel.setFont(CustomFontManager.getCustomFontMedium(26, false));
        titleLabel.setForeground(Color.decode("#28282B"));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Obtener un número aleatorio entre 0 y 12
        Random random = new Random();
        int randomNumber = random.nextInt(13);

        // Construir la ruta de la imagen aleatoria
        String imagePath = String.format("/images/drawable-pictures/profile%02d.png", randomNumber);

        // Crea la imagen de perfil
        ImageIcon imageIcon = new ImageIcon(org.ripple.ui.AddContact.class.getResource(imagePath));
        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance(182, 182, java.awt.Image.SCALE_SMOOTH);
        final ImageIcon finalImageIcon = new ImageIcon(newImage);

        JLabel profilePicLabel = new JLabel(finalImageIcon);
        profilePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Crea el plusButton
        ImageIcon plusIcon = new ImageIcon(getClass().getResource("/images/drawable-action/plus-alt.png"));
        JButton plusButton = new JButton(plusIcon);
        plusButton.setOpaque(false);
        plusButton.setContentAreaFilled(false);
        plusButton.setBorderPainted(false);

        plusButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Obtiene las dimensiones originales del icono
        int iconWidth = plusIcon.getIconWidth();
        int iconHeight = plusIcon.getIconHeight();
        plusButton.setBounds(320, 250, iconWidth, iconHeight);

        plusButton.addActionListener(e -> {
            try {
                Process process = Runtime.getRuntime().exec("powershell.exe -ExecutionPolicy Bypass -File ./src/main/java/org/ripple/scripts/file_selector.ps1");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                StringBuilder output = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    String selectedFile = output.toString().trim();
                    // Hacer algo con el archivo seleccionado
                    System.out.println("Selected file: " + selectedFile);
                } else {
                    // Manejar error de ejecución de script PowerShell
                    System.out.println("PowerShell script execution failed");
                }
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        getLayeredPane().add(plusButton, JLayeredPane.PALETTE_LAYER);

        // Manejo del plusButton con el redimensionamiento de la ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                try {
                    int iconWidth = plusIcon.getIconWidth();
                    int iconHeight = plusIcon.getIconHeight();
                    plusButton.setBounds(320, 250, iconWidth, iconHeight);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Creacion de los PlaceHolderTextField usando createTextField();
        PlaceholderTextField nameField = createTextField("Nombre");
        PlaceholderTextField lastField = createTextField("Apellido");
        PlaceholderTextField nickNameField = createTextField("Apodo (Opcional)");
        PlaceholderTextField companyField = createTextField("Compañia (Opcional)");
        PlaceholderTextField mailField = createTextField("Email");
        JLabel birthDayLabel = new JLabel("Dia de Nacimiento:");
        birthDayLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        birthDayLabel.setHorizontalAlignment(JLabel.LEFT);

        // Crea el birthDayPanel
        JPanel birthDayPanel = new JPanel();
        birthDayPanel.setLayout(new BoxLayout(birthDayPanel, BoxLayout.X_AXIS));
        birthDayPanel.setOpaque(false);
        birthDayPanel.setMaximumSize(new Dimension(360, 45));

        // Crea el birthDayField
        JTextField birthDayField = new JTextField("DD");
        birthDayField.setMaximumSize(new Dimension(70, 25));
        birthDayField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        birthDayField.setFont( new Font("Verdana", Font.PLAIN, 16));

        // Crea el birthDayMonthCB
        JComboBox<String> birthDayMonthCB = new JComboBox<>(new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"});
        birthDayMonthCB.setMaximumSize(new Dimension(160, 25));
        birthDayMonthCB.setBackground(Color.WHITE);
        birthDayMonthCB.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        birthDayMonthCB.setFont( new Font("Verdana", Font.PLAIN, 15));

        // Personaliza el botón de la flecha
        JButton arrowButton = ((JButton) birthDayMonthCB.getComponent(0));
        arrowButton.setBackground(Color.WHITE);
        arrowButton.setBorder(BorderFactory.createEmptyBorder()); // Elimina el borde del botón de la flecha

        birthDayMonthCB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        birthDayMonthCB.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBackground(Color.WHITE);
                return button;
            }
        });

        // Crea el birthYearField
        JTextField birthYearField = new JTextField("YYYY");
        birthYearField.setMaximumSize(new Dimension(120, 25));
        birthYearField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        birthYearField.setFont(new Font("Verdana", Font.PLAIN, 16));

        birthDayPanel.add(birthDayField);
        birthDayPanel.add(birthDayMonthCB);
        birthDayPanel.add(birthYearField);

        // Crea el addressLabel
        JLabel addressLabel = new JLabel("Direccion:");
        addressLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        addressLabel.setHorizontalAlignment(JLabel.LEFT);

        // Crea el addressTextArea
        JTextArea addressTextArea = new JTextArea();
        addressTextArea.setMaximumSize(new Dimension(350, 92));
        addressTextArea.setFont(new Font("Verdana", Font.PLAIN, 15));
        addressTextArea.setLineWrap(true);
        addressTextArea.setWrapStyleWord(true);

        // Crear un borde gris claro
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
        addressTextArea.setBorder(border);

        //---------------Configuracion del panel de número de teléfono---------------//
        // Configura el panel que contiene los componentes de los números de teléfono.
        JPanel phoneNumbersPanel = createPhoneNumbersPanel();
        CustomScrollPane phonesScrollPane = new CustomScrollPane(phoneNumbersPanel, 375, 120);
        phonesScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));

        //---------------Configuracion del panel de redes sociales---------------//
        // Configura el panel que contiene los componentes de las redes sociales.
        JPanel socialMediaPanel = createSocialMediaPanel();
        CustomScrollPane socialMediaScrollPane = new CustomScrollPane(socialMediaPanel, 375, 150);

        addContactPanel.add(titleLabel);
        addContactPanel.add(profilePicLabel);
        mainPanel.add(plusButton, 0);
        addContactPanel.add(nameField);
        addContactPanel.add(lastField);
        addContactPanel.add(nickNameField);
        addContactPanel.add(companyField);
        addContactPanel.add(birthDayLabel);
        addContactPanel.add(birthDayPanel);
        addContactPanel.add(addressLabel);
        addContactPanel.add(addressTextArea);
        addContactPanel.add(phonesScrollPane);
        addContactPanel.add(socialMediaScrollPane);

        whitePanel.setLayout(new BorderLayout());
        whitePanel.add(buttonPanel, BorderLayout.NORTH);
        whitePanel.add(addContactPanel, BorderLayout.CENTER);

        mainPanel.add(whitePanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    protected void paintBorder(Graphics g) {
        // Establece el color del borde que deseas
        g.setColor(Color.LIGHT_GRAY);

        // Pinta el borde utilizando los límites del componente
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    //---------------Método createPhoneNumbersPanel()---------------//
    // Crea y devuelve el panel que contiene los componentes de los números de teléfono.
    private JPanel createPhoneNumbersPanel() {
        JPanel phoneNumbersPanel = new JPanel();
        phoneNumbersPanel.setLayout(new BoxLayout(phoneNumbersPanel, BoxLayout.Y_AXIS));
        phoneNumbersPanel.setBackground(Color.WHITE);

        PhoneNumberComponent firstPhoneNumber = new PhoneNumberComponent(true);
        phoneNumbersPanel.add(firstPhoneNumber);

        return phoneNumbersPanel;
    }

    //---------------Método createSocialMediaPanel()---------------//
    // Crea y devuelve el panel que contiene los componentes de las redes sociales.
    private JPanel createSocialMediaPanel() {
        JPanel socialMediaPanel = new JPanel();
        socialMediaPanel.setLayout(new BoxLayout(socialMediaPanel, BoxLayout.Y_AXIS));
        socialMediaPanel.setBackground(Color.WHITE);

        SocialMediaComponent firstSocialNetwork = new SocialMediaComponent(true);
        socialMediaPanel.add(firstSocialNetwork);

        return socialMediaPanel;
    }

    //---------------Método createTextField()---------------//
    // Crea y devuelve un campo de texto con un texto de marcador de posición y diseño específico.
    private PlaceholderTextField createTextField(String placeholderText) {
        PlaceholderTextField textField = new PlaceholderTextField(placeholderText);
        textField.setSize(new Dimension(360, 42));
        textField.setMaximumSize(new Dimension(350, 45));
        textField.setBorder(null);
        textField.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
        return textField;
    }

    //---------------Método executePowerShellScript()---------------//
    // Ejecuta un script de PowerShell para seleccionar archivos y muestra la salida en la consola.
    private static void executePowerShellScript() {
        try {
            // Ejecutar el script PowerShell desde Java
            Process process = Runtime.getRuntime().exec("powershell.exe -File /org/ripple/scripts/file_selector.ps1");

            // Leer la salida del proceso
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Aquí, puedes realizar acciones adicionales con los nombres de los archivos
                System.out.println("Archivo seleccionado: " + line);
            }

            // Esperar a que el proceso termine
            int exitCode = process.waitFor();
            System.out.println("Proceso finalizado con código de salida: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //---------------Método removePhoneNumber()---------------//
    // Elimina el último componente del panel de números de teléfono, si hay más de uno.
    private void removePhoneNumber() {
        if (phoneNumbersPanel != null) {
            int componentCount = phoneNumbersPanel.getComponentCount();
            if (componentCount > 1) {
                phoneNumbersPanel.remove(componentCount - 1);
            }
        }
    }

    //---------------Método removeSocialMedia()---------------//
    // Elimina el último componente del panel de redes sociales, si hay más de uno.
    private void removeSocialMedia() {
        if (socialMediaPanel != null) {
            int componentCount = socialMediaPanel.getComponentCount();
            if (componentCount > 1) {
                socialMediaPanel.remove(componentCount - 1);
            }
        }
    }
}

