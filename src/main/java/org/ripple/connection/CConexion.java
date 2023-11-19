package org.ripple.connection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

public class CConexion {
    public static Connection conection = null;
    String usuario = "root";
    String contrasena = "RooT1";
    String bd = "rippleDB";
    String ip = "localhost";
    String puerto = "3306";

    String cadena = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd;

    private int userId;

    public Connection establecerConection() throws SQLException {
        try {
            conection = (Connection) DriverManager.getConnection(cadena, usuario, contrasena);
        } catch (SQLException e) {
            throw e;
        }
        return conection;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public void exportarContactosVCF() {
        try {
            Connection cn = establecerConection();
            String query = "SELECT * FROM Contacts";
            try (PreparedStatement statement = cn.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.isBeforeFirst()) {
                        JOptionPane.showMessageDialog(null, "No tiene ningún contacto registrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        return; 
                    }

                    String vcfFilePath = "C:\\Users\\MINEDUCYT\\Desktop\\Reportes\\contactos.vcf";

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(vcfFilePath))) {
                        while (resultSet.next()) {
                            Date birthday = resultSet.getDate("birthday");
                            String formattedBirthday = "";

                            if (birthday != null) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                formattedBirthday = "BDAY;value=date:" + sdf.format(birthday);
                            }

                            String contactInfo = "BEGIN:VCARD\n" +
                                    "VERSION:3.0\n" +
                                    "FN:" + resultSet.getString("firstName") + " " + resultSet.getString("lastName") + "\n" +
                                    "ORG:" + resultSet.getString("company") + "\n" +
                                    "TEL:" + resultSet.getString("phoneContact") + "\n" +
                                    "NICKNAME:" + resultSet.getString("nickName") + "\n" +
                                    "ADR:" + resultSet.getString("address") + "\n" +
                                    formattedBirthday + "\n" +
                                    "NOTE:" + resultSet.getString("notes") + "\n" +
                                    // Incluir otros campos según sea necesario
                                    "END:VCARD\n\n";
                            writer.write(contactInfo);
                        }

                        JOptionPane.showMessageDialog(null, "Contactos exportados a VCF correctamente. \n Ruta: " + vcfFilePath);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al exportar contactos a VCF: " + e.toString());
        }
    }
    
    public void guardarContacto(
            String firstName, String lastName, String phoneContact, String nickName,
            String company, String address, Date birthday, String notes, byte[] contactPhoto) {
        try {
            Connection cn = establecerConection();
            String query = "INSERT INTO Contacts (firstName, lastName, phoneContact, nickName, company, address, birthday, notes, contactPhoto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = cn.prepareStatement(query)) {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, phoneContact);
                statement.setString(4, nickName);
                statement.setString(5, company);
                statement.setString(6, address);
                statement.setDate(7, birthday);
                statement.setString(8, notes);
                statement.setBytes(9, contactPhoto);

                int filasAfectadas = statement.executeUpdate();

                if (filasAfectadas > 0) {
                    System.out.println("Contacto guardado correctamente en la base de datos.");
                } else {
                    System.out.println("Error al guardar el contacto en la base de datos.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar el contacto en la base de datos: " + ex.toString());
        }
    }
  
    public boolean consulta(Connection cn, String usuario, String contraseña) {
        String consulta = "SELECT username, password FROM UserProfile WHERE username = ? AND password = ?";

        try (PreparedStatement statement = cn.prepareStatement(consulta)) {
            statement.setString(1, usuario);
            statement.setString(2, contraseña);

            ResultSet resultado = statement.executeQuery();

            return resultado.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Si ocurre un error o no se encuentra el usuario y contraseña, devuelve false
    }
}
