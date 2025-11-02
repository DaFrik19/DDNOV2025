package org.amerike.amerikebank.service;

import org.amerike.amerikebank.dao.MovimientoRepository;
import org.amerike.amerikebank.model.Movimiento;
import java.math.BigDecimal;
import java.util.List;

public class MovimientoService {

    private final MovimientoRepository movimientoRepository;

    public MovimientoService(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    public void realizarDeposito(int cuentaId, BigDecimal monto, String descripcion) {
        validarMonto(monto);
        Movimiento movimiento = new Movimiento(cuentaId, Movimiento.TipoMovimiento.DEPOSITO, monto, descripcion != null ? descripcion : "Depósito en cuenta", null, null);
        movimientoRepository.registrarMovimiento(movimiento);
        BigDecimal saldoActual = movimientoRepository.obtenerSaldoActual(cuentaId);
        BigDecimal nuevoSaldo = saldoActual.add(monto);
        movimientoRepository.actualizarSaldoCuenta(cuentaId, nuevoSaldo);
    }

    public void realizarRetiro(int cuentaId, BigDecimal monto, String descripcion) {
        validarMonto(monto);
        validarSaldoSuficiente(cuentaId, monto);
        Movimiento movimiento = new Movimiento(cuentaId, Movimiento.TipoMovimiento.RETIRO, monto.negate(), descripcion != null ? descripcion : "Retiro de cuenta", null, null);
        movimientoRepository.registrarMovimiento(movimiento);
        BigDecimal saldoActual = movimientoRepository.obtenerSaldoActual(cuentaId);
        BigDecimal nuevoSaldo = saldoActual.subtract(monto);
        movimientoRepository.actualizarSaldoCuenta(cuentaId, nuevoSaldo);
    }

    private void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
    }

    private void validarSaldoSuficiente(int cuentaId, BigDecimal monto) {
        BigDecimal saldoActual = movimientoRepository.obtenerSaldoActual(cuentaId);
        if (saldoActual.compareTo(monto) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
    }
}