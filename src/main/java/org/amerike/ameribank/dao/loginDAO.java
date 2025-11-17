package org.amerike.ameribank.dao;

import org.amerike.ameribank.config.ConexionDB;
import org.amerike.ameribank.model.login;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class loginDAO {

    public boolean validarCredenciales(login l) {

        String sql = "call login (?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, l.getUsr());
            stmt.setString(2, l.getPwd());
            stmt.execute();

            return true;

        } catch (SQLException e) {

            String mensajeMySQL = e.getMessage();

            System.err.println("Error de BD (SQLState: " + e.getSQLState() + "): " + mensajeMySQL);
            throw new RuntimeException(
                    "Fallo en la validación de credenciales: " + mensajeMySQL, e
            );
        }
    }
}

/*
El DAO se comunica la base de datos, aquí  es donde por medio del procedimiento que cree en el video
anterior llama al procedimiento login y usa los valores que le dio el modelo para compararlo con los de la
base de datos.

También si el error es por culpa de la base de datos mostrará el error que da MySQL, y si es por credenciales o
que la cuenta esté bloqueada dará los mensajes que puse en el procedimiento
*/