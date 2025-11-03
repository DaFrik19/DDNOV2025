package org.amerike.amerikebank.controller;

import org.amerike.amerikebank.model.Movimiento;
import org.amerike.amerikebank.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    public List<Movimiento> obtenerTodosMovimientos() {
        return movimientoService.obtenerTodosMovimientos();
    }

    @GetMapping("/{id}")
    public Movimiento obtenerMovimientoPorId(@PathVariable int id) {
        return movimientoService.obtenerMovimientoPorId(id);
    }

    @GetMapping("/cuenta/{cuentaId}")
    public List<Movimiento> obtenerMovimientosPorCuenta(@PathVariable int cuentaId) {
        return movimientoService.obtenerMovimientosPorCuenta(cuentaId);
    }

    @GetMapping("/tipo/{tipo}")
    public List<Movimiento> obtenerMovimientosPorTipo(@PathVariable String tipo) {
        return movimientoService.obtenerMovimientosPorTipo(Movimiento.TipoMovimiento.valueOf(tipo.toUpperCase()));
    }

    @PostMapping
    public Movimiento crearMovimiento(@RequestBody Movimiento movimiento) {
        return movimientoService.crearMovimiento(movimiento);
    }

    @DeleteMapping("/{id}")
    public void eliminarMovimiento(@PathVariable int id) {
        movimientoService.eliminarMovimiento(id);
    }
}