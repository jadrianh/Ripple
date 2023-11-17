package org.ripple.util;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PhoneNumberComponent extends JPanel {
    private JComboBox<String> phoneSuffixCB;
    private PlaceholderTextField phoneNumberField;
    private ClickEffectButton addButton;
    private ClickEffectButton removeButton;

    public PhoneNumberComponent(boolean showAddButton) {
        initializeUI(showAddButton);
    }

    private void initializeUI(boolean showAddButton) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setOpaque(false);

        phoneSuffixCB = new JComboBox<>(new String[]{"+1", "+86", "+91", "+44", "+81", "+49", "+7", "+55", "+33", "+61", "+52", "+82", "+34", "+39", "+52", "+20", "+27", "+351", "+503", "+502", "+507", "+371", "+569"});
        phoneSuffixCB.setPreferredSize(new Dimension(55, 20));
        phoneSuffixCB.setBackground(Color.WHITE);
        phoneSuffixCB.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        phoneSuffixCB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        phoneSuffixCB.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBackground(Color.WHITE);
                return button;
            }
        });

        phoneNumberField = createTextField("Número de teléfono");

        add(phoneSuffixCB);
        add(phoneNumberField);

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
                    addPhoneNumber();
                }
            });
            add(addButton);
        } else {
            // Configuración para el botón de eliminar
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removePhoneNumber();
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
        textField.setPreferredSize(new Dimension(240, 42));
        textField.setBorder(null);
        textField.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
        return textField;
    }

    private void addPhoneNumber() {
        getParent().add(new PhoneNumberComponent(false));
        getParent().revalidate();
        getParent().repaint();
    }

    private void removePhoneNumber() {
        Container parent = getParent();
        parent.remove(this);
        parent.revalidate();
        parent.repaint();
    }


    public String getPhoneNumber() {
        return phoneNumberField.getText();
    }

    public String getPhoneSuffix() {
        return (String) phoneSuffixCB.getSelectedItem();
    }
}