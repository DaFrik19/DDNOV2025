package org.amerike.ameribank.config;

import org.amerike.ameribank.config.security.sec;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = sec.obtenerUrl();
    private static final String USER = sec.obtenerUsuario();
    private static final String PASS = sec.obtenerPassword();

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

