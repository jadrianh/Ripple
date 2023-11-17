

package org.ripple.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AgregarContacto extends JFrame {
    private DefaultListModel<String> contactListModel;

    public AgregarContacto(DefaultListModel<String> contactListModel) {
        this.contactListModel = contactListModel;
        initializeUI();
    }

    private void initializeUI() {
        //--------------Configuracion principal--------------//
        setTitle("Agregar Contacto");
        setSize(500, 700);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#00AAFF"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel nombreLabel = new JLabel("Nombre:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(nombreLabel, constraints);

        JTextField nombreField = new JTextField();
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        nombreField.setPreferredSize(new Dimension(300, 30));
        formPanel.add(nombreField, constraints);

        JLabel apellidoLabel = new JLabel("Apellido:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        formPanel.add(apellidoLabel, constraints);

        JTextField apellidoField = new JTextField();
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        apellidoField.setPreferredSize(new Dimension(300, 30));
        formPanel.add(apellidoField, constraints);

        JLabel numeroLabel = new JLabel("Número:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        formPanel.add(numeroLabel, constraints);

        JTextField numeroField = new JTextField();
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        numeroField.setPreferredSize(new Dimension(300, 30));
        formPanel.add(numeroField, constraints);

        JButton guardarButton = new JButton("Guardar");
        guardarButton.setBackground(Color.WHITE);
        guardarButton.setForeground(Color.BLACK);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        formPanel.add(guardarButton, constraints);

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText();
                String apellido = apellidoField.getText();
                String numero = numeroField.getText();

                if (!nombre.isEmpty() && !apellido.isEmpty() && !numero.isEmpty()) {
                    String contacto = nombre + " " + apellido + " (" + numero + ")";
                    contactListModel.addElement(contacto);

                    nombreField.setText("");
                    apellidoField.setText("");
                    numeroField.setText("");

                    // Cerrar la ventana AgregarContacto automáticamente
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(AgregarContacto.this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        getContentPane().add(formPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }
}
