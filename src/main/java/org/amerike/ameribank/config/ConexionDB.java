package org.amerike.ameribank.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = security.obtenerUrl();
    private static final String USER = security.obtenerUsuario();
    private static final String PASS = security.obtenerPassword();

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

