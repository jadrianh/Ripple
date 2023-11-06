package org.ripple;

import org.ripple.core.main.Home;
import org.ripple.core.Settings;
import org.ripple.core.start.LogIn;

public class Main {
    public static void main(String[] args) {
        LogIn login = new LogIn();
        
        CConexion conexion = new CConexion();
        conexion.establecerConection();
    }
}