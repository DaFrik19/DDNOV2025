package org.amerike.amerikebank.service;

import org.amerike.amerikebank.dao.MovimientoRepository;
import org.amerike.amerikebank.model.Movimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    public List<Movimiento> obtenerTodosMovimientos() {
        return movimientoRepository.findAll();
    }

    public Movimiento obtenerMovimientoPorId(int id) {
        return movimientoRepository.findById(id).orElse(null);
    }

    public List<Movimiento> obtenerMovimientosPorCuenta(int cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId);
    }

    public List<Movimiento> obtenerMovimientosPorTipo(Movimiento.TipoMovimiento tipo) {
        return movimientoRepository.findByTipoMovimiento(tipo);
    }

    public Movimiento crearMovimiento(Movimiento movimiento) {
        return movimientoRepository.save(movimiento);
    }

    public void eliminarMovimiento(int id) {
        movimientoRepository.deleteById(id);
    }
}