package org.ripple;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class CConexion {
    public static Connection conection = null;
    String usuario = "root";
    String contrasena = "Ragnar2105";
    String bd = "agendaDB";
    String ip = "localhost";
    String puerto = "3306";

    String cadena = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd;

    private int userId;

    public Connection establecerConection(){
        try{
            conection = (Connection) DriverManager.getConnection(cadena, usuario, contrasena);
            JOptionPane.showMessageDialog(null, "Se conectó correctamente a la Base de Datos");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al conectar a la Base de Datos");
        }

        return conection;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
