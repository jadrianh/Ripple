package org.ripple;

import org.ripple.core.LogIn;
import org.ripple.core.Home;

public class Main {
    public static void main(String[] args) {
        
        LogIn login = new LogIn();
        
        CConexion conexion = new CConexion();
        conexion.establecerConection();
    }
}