package org.ripple.util;

import desplazable.Desface;
import org.ripple.ui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SideMenu extends JLayeredPane {
    private Home homeReference;
    private boolean menuVisible = false;
    private SideMenu sideMenu;
    private Desface desplace;

    public boolean isMenuVisible() {
        return menuVisible;
    }

    public void setMenuVisible(boolean menuVisible) {
        this.menuVisible = menuVisible;
        firePropertyChange("menuVisible", !menuVisible, menuVisible);
    }

    public void setHomeReference(Home homeReference) {
        this.homeReference = homeReference;
    }

    public SideMenu getMenuDesplegable() {
        return sideMenu;
    }

    public void setMenuDesplegable(SideMenu sideMenu) {
        this.sideMenu = sideMenu;
    }

    public SideMenu() {
        setLocation(0, 0);
        setOpaque(true);
        setBackground(new Color(255, 255, 255));

        ImageIcon appIconMenu = new ImageIcon(getClass().getResource("/images/drawable-icons/icon-alt.png"));
        Image appImageMenu = appIconMenu.getImage().getScaledInstance(140, 36, Image.SCALE_SMOOTH);
        appIconMenu = new ImageIcon(appImageMenu);
        JLabel appLabelMenu = new JLabel(appIconMenu);
        appLabelMenu.setHorizontalAlignment(SwingConstants.CENTER);

        appLabelMenu.setBounds(0, 10, 180, 34);
        add(appLabelMenu, Integer.valueOf(1));

        String[] buttonInfo = {"/images/drawable-action/tag.png", "Etiquetas", "/images/drawable-action/sliders.png", "Ajustes", "/images/drawable-action/log-out.png", "Logout"};

        for (int i = 0; i < buttonInfo.length; i += 2) {
            JButton button = createNavButton(buttonInfo[i], buttonInfo[i + 1]);
            button.setBounds(-30, 80 + i * 45, 180, 45);
            add(button, Integer.valueOf(2));
        }
    }

    private JButton createNavButton(String iconPath, String label) {
        JButton button = new JButton();
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusable(false);

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        icon = new ImageIcon(icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
        Font font = CustomFontManager.getCustomFontMedium(20, false);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(font);
        labelComponent.setForeground(Color.decode("#00AAFF"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(new JLabel(icon));
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(labelComponent);

        button.add(buttonPanel);

        switch (label) {
            case "Principal":
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Tags tags = new Tags();
                        tags.setVisible(true);

                        // Cierra la pestaña Home
                        if (homeReference != null) {
                            homeReference.dispose();
                        }
                    }
                });
                break;
            case "Etiquetas":
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Tags tags = new Tags();
                        tags.setVisible(true);

                        // Cierra la pestaña Home
                        if (homeReference != null) {
                            homeReference.dispose();
                        }
                    }
                });
                break;
            case "Ajustes":
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Settings settings = new Settings();
                        settings.setVisible(true);
                    }
                });
                break;
            case "Logout":
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Crear un JOptionPane
                        int response = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres salir de la aplicación?", "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                        // Verificar la respuesta del usuario
                        if (response == JOptionPane.YES_OPTION) {
                            LogIn login = new LogIn();
                        }

                        // Cierra la pestaña Home
                        if (homeReference != null) {
                            homeReference.dispose();
                        }
                    }
                });
                break;

            default:
                break;
        }

        return button;
    }
}