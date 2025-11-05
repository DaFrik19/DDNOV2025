package org.amerike.ameribank.dao;

import org.amerike.ameribank.config.ConexionDB;
import org.amerike.ameribank.model.Movimiento;
import org.amerike.ameribank.model.TipoMovimiento;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MovimientoDAO {

    public void registrarDeposito(Long cuentaId, BigDecimal monto, String descripcion, String cuentaRemitente) throws Exception {
        String sql = "CALL RegistrarDeposito(?, ?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setLong(1, cuentaId);
            stmt.setBigDecimal(2, monto);
            stmt.setString(3, descripcion);
            stmt.setString(4, cuentaRemitente);

            stmt.executeUpdate();
        }
    }

    public void registrarRetiro(Long cuentaId, BigDecimal monto, String descripcion) throws Exception {
        String sql = "CALL RegistrarRetiro(?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setLong(1, cuentaId);
            stmt.setBigDecimal(2, monto);
            stmt.setString(3, descripcion);

            stmt.executeUpdate();
        }
    }

    public List<Movimiento> obtenerMovimientosPorCuenta(Long cuentaId, int limite) throws Exception {
        String sql = "CALL ObtenerMovimientosPorCuenta(?, ?)";
        List<Movimiento> movimientos = new ArrayList<>();

        try (Connection conn = ConexionDB.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setLong(1, cuentaId);
            stmt.setInt(2, limite);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Movimiento movimiento = new Movimiento();
                movimiento.setId(rs.getLong("id"));
                movimiento.setCuentaId(cuentaId);
                movimiento.setTipoMovimiento(TipoMovimiento.valueOf(rs.getString("tipo_movimiento")));
                movimiento.setMonto(rs.getBigDecimal("monto"));
                movimiento.setDescripcion(rs.getString("descripcion"));
                movimiento.setFechaMovimiento(rs.getTimestamp("fecha_movimiento").toLocalDateTime());
                movimiento.setCuentaRemitente(rs.getString("cuenta_remitente"));
                movimiento.setCuentaReceptora(rs.getString("cuenta_receptora"));

                movimientos.add(movimiento);
            }
        }

        return movimientos;
    }
}