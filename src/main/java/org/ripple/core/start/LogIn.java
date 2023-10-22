package org.ripple.core.start;

import org.ripple.core.Home;
import org.ripple.core.Registro;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class LogIn extends JFrame {

    public LogIn() {
        initializeUI();
    }

    private void initializeUI() {
        //--------------Configuracion principal--------------//
        setTitle("Ripple");
        setMinimumSize(new Dimension(520, 980));
        setLayout(new BorderLayout(0, 100));
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/images/logo.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JPanel logInPanel = createLogInPanel();

        getContentPane().add(mainPanel);
        mainPanel.add(logInPanel, BorderLayout.PAGE_END);
        setVisible(true);
    }

    private JPanel createLogInPanel() {
        JPanel logInPanel = new JPanel(new GridBagLayout());
        logInPanel.setPreferredSize(new Dimension(450, 360));
        logInPanel.setBackground(Color.WHITE);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Inicio de Sesión");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        logInPanel.add(titleLabel, constraints);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Color.BLACK);
        constraints.gridy = 1;
        logInPanel.add(separator, constraints);

        JLabel usernameIcon = new JLabel();
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        try {
            ImageIcon originalIcon = new ImageIcon(ImageIO.read(new File("src/main/resources/images/Core/user.png")));

            int width = 30;
            int height = 30;
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            usernameIcon.setIcon(scaledIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logInPanel.add(usernameIcon, constraints);


        PlaceholderTextField usernameField = createTextField("Username");
        PlaceholderTextField passwordField = createTextField("Password");

        constraints.gridx = 1;
        constraints.gridy = 2;
        logInPanel.add(usernameField, constraints);
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });

        constraints.gridy = 3;
        logInPanel.add(passwordField, constraints);

        JLabel passwordIcon = new JLabel();
        constraints.gridx = 0;
        constraints.gridy = 3;
        logInPanel.add(passwordIcon, constraints);

        try {
            ImageIcon originalPasswordIcon = new ImageIcon(ImageIO.read(new File("src/main/resources/images/Core/lock.png")));

            int width = 30;
            int height = 30;
            Image scaledPasswordImage = originalPasswordIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledPasswordIcon = new ImageIcon(scaledPasswordImage);

            passwordIcon.setIcon(scaledPasswordIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Crear el nuevo CheckBox
        JCheckBox rememberCheckBox = new JCheckBox("Recordar contraseña?");
        rememberCheckBox.setBackground(Color.decode("#FFFFFF"));
        rememberCheckBox.setFont(new Font("Verdana", Font.PLAIN, 12));
        rememberCheckBox.setForeground(Color.decode("#00A7F8"));
        rememberCheckBox.setFocusable(false);
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        logInPanel.add(rememberCheckBox, constraints);

        JButton loginButton = new JButton("Login");
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);

                Home homeForm = new Home();
                homeForm.setVisible(true);
            }
        });
        
        loginButton.setPreferredSize(new Dimension(250, 35));
        loginButton.setBackground(Color.decode("#00A7F8"));
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18));
        loginButton.setForeground(Color.decode("#FFFFFF"));
        loginButton.setFocusable(false);
        loginButton.setBorder(null);
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        logInPanel.add(loginButton, constraints);

        JButton registerButton = new JButton("¿No tienes cuenta? Regístrate");
        registerButton.setHorizontalAlignment(SwingConstants.LEFT);
        registerButton.setForeground(Color.decode("#00A7F8"));
        registerButton.setFont(new Font("Verdana", Font.PLAIN, 14));
        registerButton.setFocusable(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorder(null);
        constraints.gridy = 6;
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        logInPanel.add(registerButton, constraints);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);

                Registro registroForm = new Registro();
                registroForm.setVisible(true);
            }
        });

        return logInPanel;
    }

    private PlaceholderTextField createTextField(String placeholderText) {
        PlaceholderTextField textField = new PlaceholderTextField(placeholderText);
        textField.setPreferredSize(new Dimension(300, 30));
        textField.setBorder(null); // Eliminar el borde predeterminado
        textField.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY)); // Agregar una línea inferior
        return textField;
    }
}
