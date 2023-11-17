package org.ripple.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class AboutUs extends JFrame{
    public AboutUs() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Informacion");
        setSize(580, 450);
        setLayout(new BorderLayout());
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/images/drawable-indication/info.png")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el panel principal que contiene los otros dos paneles
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
        JPanel infoPanel = new JPanel() {
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
        infoPanel.setOpaque(false);

        // Create a title panel with an image and label
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);

        // Create a new panel with a BorderLayout
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        // Create a button with an image
        ImageIcon buttonIcon = new ImageIcon(getClass().getResource("/images/drawable-navigation/chevron-left.png"));
        JButton backButton = new JButton(buttonIcon);
        backButton.setFocusable(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings settings = new Settings();
                dispose();
            }
        });

        // Add the image
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/drawable-indication/info.png"));
        JLabel imageLabel = new JLabel(imageIcon);
        titlePanel.add(imageLabel);

        // Add the title label
        JLabel titleLabel = new JLabel("Acerca de Ripple");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 26));
        titlePanel.add(titleLabel);

        // Create a description label with line breaks
        JLabel descriptionLabel = new JLabel("<html><body style='width: 250px;'>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.</body></html>");
        descriptionLabel.setVerticalAlignment(JLabel.TOP);
        descriptionLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a 100x100 image label
        Image image1 = new ImageIcon(getClass().getResource("/images/drawable-icons/icon.png")).getImage();
        Image scaledImage1 = image1.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon imageIcon1 = new ImageIcon(scaledImage1);
        JLabel imageLabel1 = new JLabel(imageIcon1);

        // Create a 200x50 rectangular image label
        Image image2 = new ImageIcon(getClass().getResource("/images/drawable-icons/icon-alt.png")).getImage();
        Image scaledImage2 = image2.getScaledInstance(200, 50, Image.SCALE_SMOOTH);
        ImageIcon imageIcon2 = new ImageIcon(scaledImage2);
        JLabel imageLabel2 = new JLabel(imageIcon2);

        // Create a panel with a BoxLayout to arrange the image labels horizontally
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
        imagePanel.add(imageLabel1);
        imagePanel.add(Box.createRigidArea(new Dimension(20, 0))); // Add a 20px space
        imagePanel.add(imageLabel2);
        imagePanel.setOpaque(false);

        // Add the button to the new panel
        buttonPanel.add(backButton, BorderLayout.WEST);
        infoPanel.add(buttonPanel, BorderLayout.NORTH);

        // Add the title panel and description and the image panel label to the infoPanel
        infoPanel.add(titlePanel, BorderLayout.NORTH);
        infoPanel.add(descriptionLabel, BorderLayout.CENTER);
        infoPanel.add(imagePanel, BorderLayout.CENTER);

        // Set the infoPanel properties
        int arcWidth = 60;
        int arcHeight = 60;
        RoundRectangle2D roundRect = new RoundRectangle2D.Float(
                0, 0, infoPanel.getWidth(), infoPanel.getHeight(), arcWidth, arcHeight);

        //infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        int margin = 30;
        mainPanel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));

        mainPanel.add(infoPanel);

        getContentPane().add(mainPanel);
        setVisible(true);
    }
}
