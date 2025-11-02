package org.amerike.ameribank.dao;

import org.amerike.ameribank.config.ConexionDB;
import org.amerike.ameribank.model.cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ClienteDAO {
    public void registrarCliente(cliente c) throws Exception {
        String sql = "INSERT INTO clientes (numero_cliente, nombre, apellido_pat, apellido_mat, fecha_nac, rfc, curp, email, celular, direccion, ciudad, estado, cp, estatus) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNumeroCliente());
            stmt.setString(2, c.getNombre());
            stmt.setString(3, c.getApellidoPat());
            stmt.setString(4, c.getApellidoMat());
            stmt.setDate(5, java.sql.Date.valueOf(c.getFechaNac()));
            stmt.setString(6, c.getRfc());
            stmt.setString(7, c.getCurp());
            stmt.setString(8, c.getEmail());
            stmt.setString(9, c.getCelular());
            stmt.setString(10, c.getDireccion());
            stmt.setString(11, c.getCiudad());
            stmt.setString(12, c.getEstado());
            stmt.setString(13, c.getCp());
            stmt.setString(14, c.getEstatus());

            stmt.executeUpdate();
        }
    }
}
