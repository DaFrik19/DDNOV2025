package org.amerike.amerikebank.dao.impl;

import org.amerike.amerikebank.dao.MovimientoRepository;
import org.amerike.amerikebank.model.Movimiento;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoRepositoryImpl implements MovimientoRepository {

    private final Connection connection;

    public MovimientoRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void registrarMovimiento(Movimiento movimiento) {
        String sql = "INSERT INTO movimientos (cuenta_id, tipo_movimiento, monto, descripcion, " +
                "cuenta_remitente, cuenta_receptora, fecha_movimiento) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, movimiento.getCuentaId());
            stmt.setString(2, movimiento.getTipoMovimiento().name());
            stmt.setBigDecimal(3, movimiento.getMonto());
            stmt.setString(4, movimiento.getDescripcion());
            stmt.setString(5, movimiento.getCuentaRemitente());
            stmt.setString(6, movimiento.getCuentaReceptora());
            stmt.setTimestamp(7, Timestamp.valueOf(movimiento.getFechaMovimiento()));

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("No se pudo registrar el movimiento");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    movimiento.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar movimiento en la base de datos", e);
        }
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorCuenta(int cuentaId) {
        String sql = "SELECT * FROM movimientos WHERE cuenta_id = ? ORDER BY fecha_movimiento DESC";
        List<Movimiento> movimientos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cuentaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    movimientos.add(mapResultSetToMovimiento(rs));
                }
            }

            return movimientos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener movimientos de la cuenta: " + cuentaId, e);
        }
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorCliente(int clienteId) {
        String sql = "SELECT m.* FROM movimientos m " +
                "JOIN cuentas c ON m.cuenta_id = c.id " +
                "WHERE c.cliente_id = ? ORDER BY m.fecha_movimiento DESC";
        List<Movimiento> movimientos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    movimientos.add(mapResultSetToMovimiento(rs));
                }
            }

            return movimientos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener movimientos del cliente: " + clienteId, e);
        }
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorRangoFechas(int cuentaId, String fechaInicio, String fechaFin) {
        String sql = "SELECT * FROM movimientos WHERE cuenta_id = ? AND fecha_movimiento BETWEEN ? AND ? " +
                "ORDER BY fecha_movimiento DESC";
        List<Movimiento> movimientos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cuentaId);
            stmt.setString(2, fechaInicio);
            stmt.setString(3, fechaFin + " 23:59:59");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    movimientos.add(mapResultSetToMovimiento(rs));
                }
            }

            return movimientos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener movimientos por rango de fechas", e);
        }
    }

    @Override
    public BigDecimal obtenerSaldoActual(int cuentaId) {
        String sql = "SELECT saldo FROM cuentas WHERE id = ? AND estado = 'ACTIVO'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cuentaId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("saldo");
                } else {
                    throw new RuntimeException("Cuenta no encontrada o inactiva: " + cuentaId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener saldo de la cuenta: " + cuentaId, e);
        }
    }

    @Override
    public boolean actualizarSaldoCuenta(int cuentaId, BigDecimal nuevoSaldo) {
        String sql = "UPDATE cuentas SET saldo = ? WHERE id = ? AND estado = 'ACTIVO'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, nuevoSaldo);
            stmt.setInt(2, cuentaId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar saldo de la cuenta: " + cuentaId, e);
        }
    }

    private Movimiento mapResultSetToMovimiento(ResultSet rs) throws SQLException {
        Movimiento movimiento = new Movimiento();
        movimiento.setId(rs.getInt("id"));
        movimiento.setCuentaId(rs.getInt("cuenta_id"));
        movimiento.setTipoMovimiento(Movimiento.TipoMovimiento.valueOf(rs.getString("tipo_movimiento")));
        movimiento.setMonto(rs.getBigDecimal("monto"));
        movimiento.setDescripcion(rs.getString("descripcion"));
        movimiento.setCuentaRemitente(rs.getString("cuenta_remitente"));
        movimiento.setCuentaReceptora(rs.getString("cuenta_receptora"));

        Timestamp timestamp = rs.getTimestamp("fecha_movimiento");
        if (timestamp != null) {
            movimiento.setFechaMovimiento(timestamp.toLocalDateTime());
        }

        return movimiento;
    }
}