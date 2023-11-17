package org.ripple.util;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SocialMediaComponent extends JPanel {
    private JComboBox<String> socialNetworkCB;
    private PlaceholderTextField socialUsername;
    private JButton addButton;
    private JButton removeButton;

    public SocialMediaComponent(boolean showAddButton) {
        initializeUI(showAddButton);
    }

    private void initializeUI(boolean showAddButton) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setOpaque(false);

        socialNetworkCB = new JComboBox<>(new String[]{"EMail","WhatsApp","Facebook","Instagram","Twitter","LinkedIn"});
        socialNetworkCB.setPreferredSize(new Dimension(80, 20));
        socialNetworkCB.setBackground(Color.WHITE);
        socialNetworkCB.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        socialNetworkCB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        socialNetworkCB.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBackground(Color.WHITE);
                return button;
            }
        });

        socialUsername = createTextField("Usuario ó Telefono");

        add(socialNetworkCB);
        add(socialUsername);

        addButton = new ClickEffectButton("/images/drawable-action/plus-circle.png", 44, 44);
        removeButton = new ClickEffectButton("/images/drawable-action/minus-circle.png", 44, 44);

        // Agregar MouseAdapter para manejar eventos de clic
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Cambiar el tamaño al hacer clic
                pressChange(0.9);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Restaurar el tamaño original al soltar el clic
                pressChange(1.0);
            }
        });

        if (showAddButton) {
            // Configuración para el botón de agregar
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addSocialMedia();
                }
            });
            add(addButton);
        } else {
            // Configuración para el botón de eliminar
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeSocialMedia();
                }
            });
            add(removeButton);
        }
    }

    private void pressChange(double scale) {
        // Obtener el ícono y crear una transformación de scale
        ImageIcon icon = (ImageIcon) addButton.getIcon();
        Image img = icon.getImage();
        int ancho = (int) (img.getWidth(null) * scale);
        int alto = (int) (img.getHeight(null) * scale);

        // Aplicar la transformación al ícono y establecer el nuevo tamaño
        Image newImg = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        addButton.setIcon(new ImageIcon(newImg));
    }

    private PlaceholderTextField createTextField(String placeholderText) {
        PlaceholderTextField textField = new PlaceholderTextField(placeholderText);
        textField.setPreferredSize(new Dimension(220, 42));
        textField.setBorder(null);
        textField.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
        return textField;
    }

    private void addSocialMedia() {
        getParent().add(new SocialMediaComponent(false));
        getParent().revalidate();
        getParent().repaint();
    }

    private void removeSocialMedia() {
        Container parent = getParent();
        parent.remove(this);
        parent.revalidate();
        parent.repaint();
    }


    public String getPhoneNumber() {
        return socialUsername.getText();
    }

    public String getPhoneSuffix() {
        return (String) socialNetworkCB.getSelectedItem();
    }
}
