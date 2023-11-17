package org.ripple.util;



import org.ripple.entities.Contact;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class ContactPanel extends JPanel {

    private final Contact contact;
    private static final int PANEL_HEIGHT = 100; // Establece la altura fija del panel

    // Constructor de la clase ContactPanel
    public ContactPanel(Contact contact) {
        // Inicializa el panel con un contacto y configura acciones de clic
        this.contact = contact;
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            // Agrega acciones de clic si es necesario
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Dibuja la representación visual del contacto en cada panel
        super.paintComponent(g);

        int width = getWidth();
        int height = PANEL_HEIGHT; // Utiliza la altura fija
        int arc = 40;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, width, height, arc, arc);

        BufferedImage profileImage = contact.getProfileImage();
        int imageSize = height - 40;
        int x = 20;
        int y = (height - imageSize) / 2; // Centra la imagen verticalmente

        try {
            g2d.setClip(new Ellipse2D.Double(x, y, imageSize, imageSize));
            g2d.drawImage(profileImage, x, y, imageSize, imageSize, null);
            g2d.setClip(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Formato de elemento: Nombre y Apellido
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        String name = contact.getFirstName() + " " + contact.getLastName();
        int textX = imageSize + 40;
        int textY = height / 4 + g2d.getFontMetrics().getHeight() / 2;
        g2d.drawString(name, textX, textY);

        // Formato de elemento: Linea divisora
        int lineY = height / 2 - 9;
        int lineStartX = textX;
        int lineEndX = width - 300;
        g2d.setColor(Color.GRAY);
        g2d.drawLine(lineStartX, lineY, lineEndX, lineY);

        // Formato de elemento: Numero de telefono
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        String phoneNumber = contact.getPhoneNumber();
        textY = (height + lineY) / 3 + g2d.getFontMetrics().getHeight() / 2;
        g2d.drawString(phoneNumber, textX, textY);
    }
}
