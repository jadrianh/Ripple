package org.ripple.ui;

import org.ripple.util.CustomFontManager;
import org.ripple.util.PlaceholderTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class Register extends JFrame {
    private JComboBox<String> phoneSuffixCB;

    public Register() {
        initializeUI();
    }

    private void initializeUI() {
        //--------------Configuracion principal--------------//
        setTitle("Registro");
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
        int smallerDimension = Math.min(screenWidth, screenHeight - 42);

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

        JPanel registroPanel = createRegistroPanel();

        getContentPane().add(mainPanel);
        mainPanel.add(registroPanel, BorderLayout.PAGE_END);
        setVisible(true);
    }

    private JPanel createRegistroPanel() {
        JPanel registroPanel = new JPanel(new GridBagLayout());
        registroPanel.setPreferredSize(new Dimension(450, 500));
        registroPanel.setBackground(Color.WHITE);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Registro");
        titleLabel.setFont(CustomFontManager.getCustomFontMedium(26, false));
        titleLabel.setForeground(Color.decode("#28282B"));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        registroPanel.add(titleLabel, constraints);

        ImageIcon usernameIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/drawable-indication/at-sign.png"))
                .getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        JLabel usernameIconLabel = new JLabel(usernameIcon);
        constraints.gridy = 2;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        registroPanel.add(usernameIconLabel, constraints);

        PlaceholderTextField usernameField = createTextField("Nombre de usuario");
        constraints.gridx = 1;
        constraints.gridwidth = 1;
        registroPanel.add(usernameField, constraints);

        ImageIcon nameIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/drawable-indication/user.png"))
                .getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        JLabel nameIconLabel = new JLabel(nameIcon);
        constraints.gridy = 3;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        registroPanel.add(nameIconLabel, constraints);

        PlaceholderTextField nameField = createTextField("Nombre");
        constraints.gridx = 1;
        constraints.gridwidth = 1;
        registroPanel.add(nameField, constraints);

        ImageIcon lastNameIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/drawable-indication/users.png"))
                .getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        JLabel lastNameLabel = new JLabel(lastNameIcon);
        constraints.gridy = 4;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        registroPanel.add(lastNameLabel, constraints);

        PlaceholderTextField lastNameField = createTextField("Apellidos");
        constraints.gridx = 1;
        constraints.gridwidth = 1;
        registroPanel.add(lastNameField, constraints);

        ImageIcon phoneNumberIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/drawable-indication/phone.png"))
                .getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        JLabel phoneNumberLabel = new JLabel(phoneNumberIcon);
        constraints.gridy = 5;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        
        JPanel phoneNumberPanel = new JPanel();
        phoneNumberPanel.setOpaque(false);
        phoneNumberPanel.setLayout(new GridBagLayout());

        GridBagConstraints panelConstraints = new GridBagConstraints();
        panelConstraints.gridx = 0;
        panelConstraints.gridy = 0;
        panelConstraints.fill = GridBagConstraints.NONE;
        phoneNumberPanel.add(phoneNumberLabel, panelConstraints);
        panelConstraints.gridx = 1;
        panelConstraints.fill = GridBagConstraints.HORIZONTAL;
        panelConstraints.weightx = 1;

        phoneSuffixCB = new JComboBox<>(new String[]{"+1", "+86", "+91", "+44", "+81", "+49", "+7", "+55", "+33", "+61", "+52", "+82", "+34", "+39", "+52", "+20", "+27", "+351", "+503", "+502", "+507", "+371", "+569"});
        phoneSuffixCB.setPreferredSize(new Dimension(55, 20));
        phoneSuffixCB.setBackground(Color.WHITE);
        phoneSuffixCB.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        phoneSuffixCB.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBackground(Color.WHITE);
                return button;
            }
        });
        phoneNumberPanel.add(phoneSuffixCB, panelConstraints);
        panelConstraints.gridx = 2;
        panelConstraints.weightx = 1;

        PlaceholderTextField phoneNumberField = createTextField("Numero de teléfono");
        phoneNumberPanel.add(phoneNumberField, panelConstraints);
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        registroPanel.add(phoneNumberPanel, constraints);
        /////////////////////////////////////////////////

        ImageIcon passwordIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/drawable-indication/key.png"))
                .getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        JLabel passwordIconLabel = new JLabel(passwordIcon);
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 1;
        registroPanel.add(passwordIconLabel, constraints);

        PlaceholderTextField passwordField = createTextField("Contraseña");
        constraints.gridx = 1;
        constraints.gridwidth = 1;
        registroPanel.add(passwordField, constraints);

        ImageIcon passwordConfirmIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/drawable-indication/lock.png"))
                .getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        JLabel confirmPasswordIconLabel = new JLabel(passwordConfirmIcon);
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 1;
        registroPanel.add(confirmPasswordIconLabel, constraints);

        PlaceholderTextField confirmPasswordField = createTextField("Confirmar contraseña");
        constraints.gridx = 1;
        constraints.gridwidth = 1;
        registroPanel.add(confirmPasswordField, constraints);

        JButton registerButton = new JButton("Registrarse");
        registerButton.setPreferredSize(new Dimension(250, 35));
        registerButton.setFont(CustomFontManager.getCustomFontMedium(26, false));
        registerButton.setBackground(Color.decode("#00A7F8"));
        registerButton.setForeground(Color.decode("#FFFFFF"));
        registerButton.setFocusable(false);
        registerButton.setBorder(null);
        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.gridwidth = 2;
        registroPanel.add(registerButton, constraints);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logica del boton registro
                //
                //
                //
            }
        });

        JButton loginButton = new JButton("¿Tienes una cuenta? Inicia sesión aquí");
        loginButton.setHorizontalAlignment(SwingConstants.LEFT);
        loginButton.setForeground(Color.decode("#00A7F8"));
        loginButton.setFont(new Font("Verdana", Font.PLAIN, 14));
        loginButton.setFocusable(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorder(null);
        constraints.gridx = 0;
        constraints.gridy = 11;
        constraints.gridwidth = 2;
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        registroPanel.add(loginButton, constraints);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                LogIn loginForm = new LogIn();
                loginForm.setVisible(true);
            }
        });

        return registroPanel;
    }
    private PlaceholderTextField createTextField(String placeholderText) {
        PlaceholderTextField textField = new PlaceholderTextField(placeholderText);
        textField.setPreferredSize(new Dimension(300, 30));
        textField.setBorder(null); // Eliminar el borde predeterminado
        textField.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY)); // Agregar una línea inferior
        return textField;
    }
}
