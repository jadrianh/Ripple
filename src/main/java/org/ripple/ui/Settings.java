package org.ripple.ui;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.Birthday;
import ezvcard.property.Photo;
import org.ripple.util.CustomFontManager;
import org.ripple.util.DeleteDialog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.ripple.connection.CConexion;

public class Settings extends JFrame {

    public Settings() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Ajustes");
        setSize(580, 450);
        setLayout(new BorderLayout());
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/images/drawable-action/sliders.png")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

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

        // Crear el panel superior para el título "Opciones" (transparente)
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Opciones");
        titleLabel.setFont(CustomFontManager.getCustomFontMedium(26, false));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);

        // Crear el panel de opciones con fondo blanco y bordes redondeados
        JPanel optionsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.WHITE);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = 40;
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            }
        };

        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        optionsPanel.add(createButton(" Importar Contactos", "/images/drawable-action/download.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Crear un JFileChooser
                    JFileChooser fileChooser = new JFileChooser();

                    // Agregar un filtro para mostrar solo archivos VCF
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos VCF (*.vcf)", "vcf");
                    fileChooser.setFileFilter(filter);

                    // Mostrar el cuadro de diálogo para seleccionar el archivo
                    int result = fileChooser.showOpenDialog(Settings.this);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        // Obtener el archivo seleccionado
                        File selectedFile = fileChooser.getSelectedFile();

                        // Verificar si la extensión del archivo es ".vcf"
                        if (selectedFile.getName().toLowerCase().endsWith(".vcf")) {
                            importarContactosDesdeVCF(selectedFile.getAbsolutePath());
                        } else {
                            JOptionPane.showMessageDialog(Settings.this, "Por favor, selecciona un archivo VCF válido.");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }));
        
        optionsPanel.add(createSeparator());

        optionsPanel.add(createButton(" Exportar Contactos", "/images/drawable-action/upload.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               CConexion conexion = new CConexion();
               conexion.exportarContactosVCF();
            }
        }));
        optionsPanel.add(createSeparator());

        optionsPanel.add(createButton(" Cambiar contraseña", "/images/drawable-indication/lock.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mostrar la ventana emergente para cambiar la contraseña
            }
        }));
        optionsPanel.add(createSeparator());

        optionsPanel.add(createButton(" Acerca de Ripple", "/images/drawable-indication/info.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutUs info = new AboutUs();
                dispose();
            }
        }));
        optionsPanel.add(createSeparator());

        optionsPanel.add(createButton(" Eliminar Cuenta", "/images/drawable-action/trash.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear una instancia del cuadro de diálogo personalizado
                DeleteDialog deletionDialog = new DeleteDialog(Settings.this);
                deletionDialog.setVisible(true);

                // Aquí puedes verificar si se confirmó la eliminación después de cerrar el cuadro de diálogo
                if (deletionDialog.isConfirmed()) {
                    // El usuario confirmó la eliminación, proceder con la lógica de eliminación
                } else {
                    // El usuario canceló la eliminación, realizar alguna acción en consecuencia
                }
            }
        }));

        int arc = 40;
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Component[] buttons = optionsPanel.getComponents();
        for (Component button : buttons) {
            if (button instanceof JButton) {
                ((JButton) button).setFont(CustomFontManager.getCustomFont(20, false));
                ((JButton) button).setFocusable(false);
            }
        }

        optionsPanel.setOpaque(false);

        int margin = 30;
        mainPanel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private JButton createButton(String buttonText, String imagePath, ActionListener actionListener) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagePath));
        Image image = imageIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(image);

        button.setIcon(scaledImageIcon);
        button.setText(buttonText);

        button.addActionListener(actionListener);

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(480, 1));
        separator.setForeground(Color.GRAY);
        return separator;
    }
    
    private void importarContactosDesdeVCF(String filePath) {
        try {
            // Crear una instancia de CConexion
            CConexion conexion = new CConexion();

            // Parsear el archivo VCF
            java.util.List<VCard> vcards = Ezvcard.parse(new File(filePath)).all();

            // Verificar si hay contactos para importar
            if (vcards.isEmpty()) {
                JOptionPane.showMessageDialog(Settings.this, "El archivo VCF no contiene contactos.");
                return;
            }

            // Importar cada VCard a la base de datos
            for (VCard vcard : vcards) {
                // Obtener los campos del VCard
                String firstName = obtenerValor(vcard.getStructuredName().getGiven());
                String lastName = obtenerValor(vcard.getStructuredName().getFamily());
                String phoneContact = obtenerValor(vcard.getTelephoneNumbers().isEmpty() ? null : vcard.getTelephoneNumbers().get(0).getText());
                String nickName = obtenerValor(vcard.getNickname() != null ? vcard.getNickname().getValues().toString() : null);
                String company = obtenerValor(vcard.getOrganization() == null ? null : vcard.getOrganization().getValues().get(0));
                String address = obtenerValor(vcard.getAddresses().isEmpty() ? null : vcard.getAddresses().get(0).getStreetAddress());
                Date birthday = obtenerFecha(vcard.getBirthday());
                String notes = obtenerValor(vcard.getNotes().isEmpty() ? null : vcard.getNotes().get(0).getValue());
                byte[] photoData = obtenerDatosFoto(vcard);

                // Guardar la información en la base de datos, incluyendo la foto
                conexion.guardarContacto(firstName, lastName, phoneContact, nickName, company, address, birthday, notes, photoData);
            }

            JOptionPane.showMessageDialog(Settings.this, "Contactos importados correctamente desde el archivo VCF.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(Settings.this, "Error al importar contactos desde el archivo VCF: " + ex.getMessage());
        }
    }
    
    private String obtenerValor(String valor) {
        return (valor != null) ? valor : "";
    }

    private Date obtenerFecha(Birthday birthday) {
        if (birthday != null && birthday.getDate() != null && birthday.getDate().toString() != null) {
            return Date.valueOf(birthday.getDate().toString());
        }
        return null;
    }
    
    private byte[] obtenerDatosFoto(VCard vcard) {
        try {
            java.util.List<Photo> photos = vcard.getPhotos();
            if (!photos.isEmpty()) {
                Photo photo = photos.get(0);
                byte[] photoData = photo.getData();
                if (photoData != null && photoData.length > 0) {
                    return photoData;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
