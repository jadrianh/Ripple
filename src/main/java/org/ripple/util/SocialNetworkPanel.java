package org.ripple.util;

import org.ripple.entities.SocialNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SocialNetworkPanel extends JPanel {

    private List<SocialNetwork> socialNetworks;

    // Constructor sin argumentos para crear una lista predeterminada
    public SocialNetworkPanel() {
        // Crea una lista predeterminada con algunos datos de prueba
        this.socialNetworks = getDefaultSocialNetworks();
        initializeUI();
    }

    // Constructor que toma una lista como argumento
    public SocialNetworkPanel(List<SocialNetwork> socialNetworks) {
        this.socialNetworks = socialNetworks;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridLayout(0, 3)); // GridLayout con 3 columnas, las filas se ajustarán automáticamente

        for (SocialNetwork socialNetwork : socialNetworks) {
            JButton socialButton = createSocialButton(socialNetwork);
            add(socialButton);
        }
    }

    private JButton createSocialButton(SocialNetwork socialNetwork) {
        JButton button = new JButton();

        // Obtener la imagen del logo de la red social
        String imageName = socialNetwork.getSocialNetwork().toLowerCase() + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/drawable-icons/" + imageName));
        button.setIcon(icon);

        // Configurar el ActionListener para redirigir al usuario al hacer clic
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String socialUrl = getSocialUrl(socialNetwork.getSocialNetwork());
                String userUrl = socialUrl + socialNetwork.getSocialUsername();
                // Abrir el enlace en el navegador
                openWebPage(userUrl);
            }
        });

        // Establecer el tamaño preferido del botón según el tamaño de la imagen
        int buttonSize = 70; // Tamaño deseado
        button.setPreferredSize(new Dimension(buttonSize, buttonSize));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    private String getSocialUrl(String socialNetwork) {
        // Definir los URLs base para cada red social
        switch (socialNetwork.toLowerCase()) {
            case "facebook":
                return "https://www.facebook.com/";
            case "twitter":
                return "https://twitter.com/";
            case "instagram":
                return "https://instagram.com/";
            case "whatsapp":
                return "https://api.whatsapp.com/send?phone=";
            case "gmail":
                return "mailto:";
            default:
                return "https://www.example.com/";
        }
    }

    // Método para abrir un enlace en el navegador predeterminado
    private void openWebPage(String url) {
        try {
            Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método de prueba
    public static void main(String[] args) {
        // Crear y mostrar el panel sin proporcionar una lista (utilizará la lista predeterminada)
        JFrame frame = new JFrame("Social Network Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        SocialNetworkPanel panel = new SocialNetworkPanel();
        frame.add(panel);

        frame.setVisible(true);
    }

    // Método que devuelve una lista predeterminada para pruebas
    private List<SocialNetwork> getDefaultSocialNetworks() {
        List<SocialNetwork> defaultNetworks = new ArrayList<>();
        defaultNetworks.add(new SocialNetwork(1, "Facebook", "alejandrito"));
        defaultNetworks.add(new SocialNetwork(2, "Twitter", "faker"));
        defaultNetworks.add(new SocialNetwork(3, "Whatsapp", "50373614554"));
        defaultNetworks.add(new SocialNetwork(4, "Instagram", "ragnar21_05"));
        defaultNetworks.add(new SocialNetwork(6, "Gmail", "jadrianhernandez13@gmail.com"));
        // Agregar más redes sociales según sea necesario
        return defaultNetworks;
    }
}