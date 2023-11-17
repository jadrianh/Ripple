package org.ripple.util;

import java.awt.Font;
import java.io.InputStream;

public class CustomFontManager {

    private static Font customFont;
    private static Font customFontMedium;

    // Cargar la fuente: Product-Sans-Regular
    static {
        try {
            // Cargar la fuente personalizada desde el archivo ttf
            InputStream is = CustomFontManager.class.getResourceAsStream("/fonts/ProductSans-Regular.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, usar una fuente por defecto o manejar la excepción según tus necesidades
            customFont = new Font("Verdana", Font.PLAIN, 16);
        }
    }

    public static Font getCustomFont(int size, boolean bold) {
        int style = bold ? Font.BOLD : Font.PLAIN;
        return customFont.deriveFont(style, size);
    }

    public static Font getCustomFont(int size) {
        return customFont.deriveFont(Font.PLAIN, size);
    }

    // Cargar la fuente: Product-Sans-Medium
    static {
        try {
            // Cargar la fuente personalizada desde el archivo ttf
            InputStream is = CustomFontManager.class.getResourceAsStream("/fonts/ProductSans-Medium.ttf");
            customFontMedium = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, usar una fuente por defecto o manejar la excepción según tus necesidades
            customFontMedium = new Font("Verdana", Font.PLAIN, 16);
        }
    }

    public static Font getCustomFontMedium(int size, boolean bold) {
        int style = bold ? Font.BOLD : Font.PLAIN;
        return customFontMedium.deriveFont(style, size);
    }

    public static Font getCustomFontMedium(int size) {
        return customFontMedium.deriveFont(Font.PLAIN, size);
    }
}
