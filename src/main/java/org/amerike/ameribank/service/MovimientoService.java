// src/main/java/org/amerike/ameribank/service/MovimientoService.java
package org.amerike.ameribank.service;

import org.amerike.ameribank.dao.MovimientoDAO;
import org.amerike.ameribank.model.Movimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoDAO movimientoDAO;

    public void realizarDeposito(Long cuentaId, BigDecimal monto, String descripcion, String cuentaRemitente) throws Exception {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        movimientoDAO.registrarDeposito(cuentaId, monto, descripcion, cuentaRemitente);
    }

    public void realizarRetiro(Long cuentaId, BigDecimal monto, String descripcion) throws Exception {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        movimientoDAO.registrarRetiro(cuentaId, monto, descripcion);
    }

    public List<Movimiento> obtenerMovimientosPorCuenta(Long cuentaId, int limite) throws Exception {
        return movimientoDAO.obtenerMovimientosPorCuenta(cuentaId, limite);
    }
}