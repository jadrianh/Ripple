/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ripple;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author cwald
 */
public class CConexion {
    Connection conection = null;
    String usuario = "Ripple";
    String contrasena = "RippleCode";
    String bd = "agendaDB";
    String ip = "localhost";
    String puerto = "1433";
    
    String cadena = "jdbc:sqlserver://" + ip + ":" + puerto + "/" + bd;
    
    public Connection establecerConection(){
        try{
            String cadena = "jdbc:sqlserver://localhost:" + puerto + ":" + "databaseName = " + bd;
            conection = (Connection) DriverManager.getConnection(cadena, usuario, contrasena);
            JOptionPane.showMessageDialog(null, "Se conectó correctamente a la Base de Datos");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al conectar a la Base de Datos");
        }
        
        return conection;
    }
    
}