package org.amerike.ameribank.dao;

import org.amerike.ameribank.model.cuenta;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class CuentaDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/ameribank";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public void registrarCuenta(cuenta c) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo, estado, cliente_id) " +
                    "VALUES (?, ?, ?, ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, c.getNumeroCuenta());
            ps.setString(2, c.getTipoCuenta());
            ps.setBigDecimal(3, c.getSaldo());
            ps.setString(4, c.getEstado());
            ps.setInt(5, c.getClienteId());

            ps.executeUpdate();
            System.out.println("Cuenta registrada correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al registrar cuenta: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }
}
