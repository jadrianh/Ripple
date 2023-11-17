package org.ripple.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CConexion {
    public static Connection conection = null;
    String usuario = "root";
    String contrasena = "Ragnar2105";
    String bd = "rippledb";
    String ip = "localhost";
    String puerto = "3307";

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
}
