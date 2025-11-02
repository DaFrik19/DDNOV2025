package org.amerike.amerikebank.dao;

import org.amerike.amerikebank.model.Movimiento;
import java.math.BigDecimal;
import java.util.List;

public interface MovimientoRepository {
    void registrarMovimiento(Movimiento movimiento);
    List<Movimiento> obtenerMovimientosPorCuenta(int cuentaId);
    List<Movimiento> obtenerMovimientosPorCliente(int clienteId);
    List<Movimiento> obtenerMovimientosPorRangoFechas(int cuentaId, String fechaInicio, String fechaFin);
    BigDecimal obtenerSaldoActual(int cuentaId);
    boolean actualizarSaldoCuenta(int cuentaId, BigDecimal nuevoSaldo);
}