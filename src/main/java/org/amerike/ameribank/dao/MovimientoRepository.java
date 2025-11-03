package org.amerike.amerikebank.dao;

import org.amerike.amerikebank.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {
    List<Movimiento> findByCuentaId(int cuentaId);
    List<Movimiento> findByTipoMovimiento(Movimiento.TipoMovimiento tipoMovimiento);
}