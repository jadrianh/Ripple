package org.ripple.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickEffectButton extends JButton {
    private ImageIcon iconoOriginal;

    public ClickEffectButton(String rutaImagen, int ancho, int alto) {
        iconoOriginal = new ImageIcon(ClickEffectButton.class.getResource(rutaImagen));

        // Escalar la imagen al tamaño deseado
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(imagenEscalada));
        setPreferredSize(new Dimension(ancho, alto));

        setOpaque(false);
        setFocusable(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addClickEffect();
    }

    private void addClickEffect() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                cambiarTamanioImagen(0.9);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                cambiarTamanioImagen(1.0);
            }
        });
    }

    private void cambiarTamanioImagen(double escala) {
        // Escalar la imagen al nuevo tamaño
        int anchoNuevo = (int) (iconoOriginal.getIconWidth() * escala);
        int altoNuevo = (int) (iconoOriginal.getIconHeight() * escala);
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(anchoNuevo, altoNuevo, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(imagenEscalada));
    }
}
