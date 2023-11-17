package org.ripple;

import org.ripple.connection.CConexion;
import org.ripple.ui.AddContact;
import org.ripple.ui.ContactProfile;
import org.ripple.ui.LogIn;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.zip.CheckedInputStream;

public class Main {
    public static void main(String[] args) {
        LogIn login = new LogIn();

        CConexion conexion = new CConexion();
        try {
            conexion.establecerConection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la Base de Datos");
        }
    }
}