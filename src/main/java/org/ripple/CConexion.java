package org.ripple;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

public class CConexion {
   private Connection cn = null;

    // Método para obtener la conexión existente o crear una nueva si no existe
    public Connection obtenerConexion() {
        if (cn == null) {
            conectar(); // Llama al método conectar si la conexión no existe
        }
        return cn;
    }

    private void conectar() {
        try {
            // Carga el controlador JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Configura las propiedades para la conexión a MySQL
            Properties props = new Properties();
            props.setProperty("user", "root"); 
            props.setProperty("password", "Ragnar2105"); 
            props.setProperty("useSSL", "false"); 
            props.setProperty("serverTimezone", "UTC"); 

            // Establece la conexión utilizando las propiedades
            cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/rippleDB", props);
            JOptionPane.showMessageDialog(null, "Se conectó correctamente a la Base de Datos");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la Base de Datos" + e.toString());   
        }
    }
    
    public boolean consulta(Connection cn, String usuario, String contraseña) {
        String consulta = "SELECT username, password FROM UserProfile WHERE username = ? AND password = ?";

        try (PreparedStatement statement = cn.prepareStatement(consulta)) {
            statement.setString(1, usuario);
            statement.setString(2, contraseña);

            ResultSet resultado = statement.executeQuery();

            return resultado.next(); // Devuelve true si hay un resultado, es decir, usuario y contraseña válidos
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Si ocurre un error o no se encuentra el usuario y contraseña, devuelve false
    }
}
