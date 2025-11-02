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

        Movimiento movimiento = new Movimiento(
                cuentaId,
                Movimiento.TipoMovimiento.DEPOSITO,
                monto,
                descripcion != null ? descripcion : "Depósito en cuenta",
                null,
                null
        );

        movimientoRepository.registrarMovimiento(movimiento);

        BigDecimal saldoActual = movimientoRepository.obtenerSaldoActual(cuentaId);
        BigDecimal nuevoSaldo = saldoActual.add(monto);
        movimientoRepository.actualizarSaldoCuenta(cuentaId, nuevoSaldo);
    }

    public void realizarRetiro(int cuentaId, BigDecimal monto, String descripcion) {
        validarMonto(monto);
        validarSaldoSuficiente(cuentaId, monto);

        Movimiento movimiento = new Movimiento(
                cuentaId,
                Movimiento.TipoMovimiento.RETIRO,
                monto.negate(),
                descripcion != null ? descripcion : "Retiro de cuenta",
                null,
                null
        );

        movimientoRepository.registrarMovimiento(movimiento);

        BigDecimal saldoActual = movimientoRepository.obtenerSaldoActual(cuentaId);
        BigDecimal nuevoSaldo = saldoActual.subtract(monto);
        movimientoRepository.actualizarSaldoCuenta(cuentaId, nuevoSaldo);
    }

    public void realizarTransferencia(int cuentaOrigenId, String cuentaDestino,
                                      BigDecimal monto, String descripcion) {
        validarMonto(monto);
        validarSaldoSuficiente(cuentaOrigenId, monto);

        String descripcionCompleta = descripcion != null ? descripcion :
                "Transferencia a cuenta " + cuentaDestino;

        Movimiento movimiento = new Movimiento(
                cuentaOrigenId,
                Movimiento.TipoMovimiento.TRANSFERENCIA,
                monto.negate(),
                descripcionCompleta,
                obtenerNumeroCuenta(cuentaOrigenId),
                cuentaDestino
        );

        movimientoRepository.registrarMovimiento(movimiento);

        BigDecimal saldoActual = movimientoRepository.obtenerSaldoActual(cuentaOrigenId);
        BigDecimal nuevoSaldo = saldoActual.subtract(monto);
        movimientoRepository.actualizarSaldoCuenta(cuentaOrigenId, nuevoSaldo);
    }

    public List<Movimiento> consultarMovimientosCuenta(int cuentaId) {
        return movimientoRepository.obtenerMovimientosPorCuenta(cuentaId);
    }

    public List<Movimiento> consultarMovimientosCliente(int clienteId) {
        return movimientoRepository.obtenerMovimientosPorCliente(clienteId);
    }

    public List<Movimiento> consultarMovimientosPorFecha(int cuentaId, String fechaInicio, String fechaFin) {
        validarFechas(fechaInicio, fechaFin);
        return movimientoRepository.obtenerMovimientosPorRangoFechas(cuentaId, fechaInicio, fechaFin);
    }

    public BigDecimal consultarSaldo(int cuentaId) {
        return movimientoRepository.obtenerSaldoActual(cuentaId);
    }

    public String obtenerEstadoCuenta(int cuentaId) {
        BigDecimal saldo = consultarSaldo(cuentaId);
        List<Movimiento> movimientos = consultarMovimientosCuenta(cuentaId);

        StringBuilder estadoCuenta = new StringBuilder();
        estadoCuenta.append("=== ESTADO DE CUENTA ===\n");
        estadoCuenta.append("Cuenta ID: ").append(cuentaId).append("\n");
        estadoCuenta.append("Saldo Actual: $").append(saldo).append("\n");
        estadoCuenta.append("Últimos movimientos:\n");

        for (Movimiento movimiento : movimientos) {
            estadoCuenta.append("- ").append(movimiento.getFechaMovimiento().toLocalDate())
                    .append(" | ").append(movimiento.getTipoMovimiento())
                    .append(" | $").append(movimiento.getMonto())
                    .append(" | ").append(movimiento.getDescripcion())
                    .append("\n");
        }

        return estadoCuenta.toString();
    }

    private void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        if (monto.compareTo(new BigDecimal("1000000")) > 0) {
            throw new IllegalArgumentException("El monto excede el límite permitido");
        }
    }

    private void validarSaldoSuficiente(int cuentaId, BigDecimal monto) {
        BigDecimal saldoActual = movimientoRepository.obtenerSaldoActual(cuentaId);
        if (saldoActual.compareTo(monto) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar la operación. Saldo actual: $" + saldoActual);
        }
    }

    private void validarFechas(String fechaInicio, String fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son requeridas");
        }
    }

    private String obtenerNumeroCuenta(int cuentaId) {
        return "CTA-" + cuentaId;
    }
}