package org.ripple.core.start;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Objects;
import org.ripple.CConexion;
import org.ripple.core.main.Home;


public class LogIn extends JFrame {

    public LogIn() {
        initializeUI();
    }

    private void initializeUI() {
        //--------------Configuracion principal--------------//
        setTitle("Ripple");
        setSize(520, 980);
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

        // Línea divisora
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Color.BLACK);
        constraints.gridy = 1;
        logInPanel.add(separator, constraints);

        // Icono username
        JLabel usernameIcon = new JLabel();
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        try {
            // Cargar la imagen desde el archivo "user.png" en la carpeta "resources"
            ImageIcon originalIcon = new ImageIcon(ImageIO.read(new File("src/main/resources/images/Core/user.png")));

            // Escalar la imagen al tamaño deseado (por ejemplo, 50x50 píxeles)
            int width = 30;
            int height = 30;
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            usernameIcon.setIcon(scaledIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logInPanel.add(usernameIcon, constraints);


        // Crear campos de texto sin bordes y con línea inferior
        PlaceholderTextField usernameField = createTextField("Username");
        PlaceholderTextField passwordField = createTextField("Password");

        constraints.gridx = 1;
        constraints.gridy = 2;
        logInPanel.add(usernameField, constraints);
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Si el usuario presiona la tecla Intro
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Desplazar el cursor del teclado al campo de contraseña
                    passwordField.requestFocus();
                }
            }
        });

        constraints.gridy = 3;
        logInPanel.add(passwordField, constraints);

        // Icono password
        JLabel passwordIcon = new JLabel();
        constraints.gridx = 0;
        constraints.gridy = 3;
        logInPanel.add(passwordIcon, constraints);

        try {
            // Cargar la imagen desde el archivo "password.png" en la carpeta "resources"
            ImageIcon originalPasswordIcon = new ImageIcon(ImageIO.read(new File("src/main/resources/images/Core/lock.png")));

            // Escalar la imagen al tamaño deseado (por ejemplo, 50x50 píxeles)
            int width = 30;
            int height = 30;
            Image scaledPasswordImage = originalPasswordIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledPasswordIcon = new ImageIcon(scaledPasswordImage);

            passwordIcon.setIcon(scaledPasswordIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("La aplicación se ha iniciado.");

        JButton loginButton = new JButton("Login");
        

        loginButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Obtén el contenido de los campos de texto
        String usuario = usernameField.getText();
        String contraseña = passwordField.getText();

        // Verifica que ambos campos no estén vacíos
        if (usuario.isEmpty() || contraseña.isEmpty()) {
            // Muestra un mensaje de error si uno o ambos campos están vacíos
            JOptionPane.showMessageDialog(LogIn.this, "Por favor, ingresa tu usuario y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Sale del método sin continuar
        }

        // Deshabilita el botón de inicio de sesión para evitar múltiples clics
        loginButton.setEnabled(false);

        // Muestra un mensaje de espera
        JOptionPane.showMessageDialog(LogIn.this, "Estableciendo conexión...", "Mensaje", JOptionPane.INFORMATION_MESSAGE);

        // Crea un hilo para establecer la conexión en segundo plano
        Thread connectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                CConexion conexion = new CConexion();
                Connection cn = conexion.obtenerConexion();

                if (cn != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            // Muestra un mensaje de éxito utilizando JOptionPane
                            JOptionPane.showMessageDialog(LogIn.this, "La conexión a la base de datos se ha establecido correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                            // Intenta hacer la consulta
                            boolean loginExitoso = conexion.consulta(cn, usuario, contraseña);

                            if (loginExitoso) {
                                // Usuario y contraseña válidos
                                JOptionPane.showMessageDialog(LogIn.this, "Inicio de sesión exitoso!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                                // Avanza al siguiente panel (Home) aquí
                                setVisible(false); // Oculta el formulario LogIn
                                Home homeForm = new Home(); // Crea una instancia del formulario Home
                                homeForm.setVisible(true); // Muestra el formulario Home
                            } else {
                                // Usuario o contraseña incorrectos
                                JOptionPane.showMessageDialog(LogIn.this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);

                                // Habilita el botón nuevamente
                                loginButton.setEnabled(true);
                            }
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            // Muestra un mensaje de error utilizando JOptionPane
                            JOptionPane.showMessageDialog(LogIn.this, "Error: No se pudo establecer la conexión a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);

                            // Habilita el botón nuevamente
                            loginButton.setEnabled(true);
                        }
                    });
                }
            }
        });
        
        // Inicia el hilo para establecer la conexión
        connectionThread.start();
            }
        });

        loginButton.setPreferredSize(new Dimension(250, 35));
        loginButton.setBackground(Color.decode("#00A7F8"));
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18));
        loginButton.setForeground(Color.decode("#FFFFFF"));
        loginButton.setFocusable(false);
        loginButton.setBorder(null);
        constraints.gridx = 0;
        constraints.gridy = 4;
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
        registerButton.setFont(new Font("Arial", Font.PLAIN, 12));
        registerButton.setFocusable(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorder(null);
        constraints.gridy = 5;
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
                // Oculta el formulario LogIn
                setVisible(false);

                // Crea una instancia del formulario RegistroVentana
                Register registroForm = new Register();
                // Muestra el formulario de registro
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
