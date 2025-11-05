package org.amerike.ameribank.controller;

import org.amerike.ameribank.model.Movimiento;
import org.amerike.ameribank.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @PostMapping("/deposito")
    public ResponseEntity<?> realizarDeposito(@RequestBody Map<String, Object> request) {
        try {
            Long cuentaId = Long.valueOf(request.get("cuentaId").toString());
            BigDecimal monto = new BigDecimal(request.get("monto").toString());
            String descripcion = (String) request.get("descripcion");
            String cuentaRemitente = (String) request.get("cuentaRemitente");

            movimientoService.realizarDeposito(cuentaId, monto, descripcion, cuentaRemitente);

            return ResponseEntity.ok().body("Depósito realizado exitosamente");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al realizar depósito: " + e.getMessage());
        }
    }

    @PostMapping("/retiro")
    public ResponseEntity<?> realizarRetiro(@RequestBody Map<String, Object> request) {
        try {
            Long cuentaId = Long.valueOf(request.get("cuentaId").toString());
            BigDecimal monto = new BigDecimal(request.get("monto").toString());
            String descripcion = (String) request.get("descripcion");

            movimientoService.realizarRetiro(cuentaId, monto, descripcion);

            return ResponseEntity.ok().body("Retiro realizado exitosamente");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al realizar retiro: " + e.getMessage());
        }
    }

    @PostMapping("/transferencia")
    public ResponseEntity<?> realizarTransferencia(@RequestBody Map<String, Object> request) {
        try {
            Long cuentaOrigenId = Long.valueOf(request.get("cuentaOrigenId").toString());
            String cuentaDestino = (String) request.get("cuentaDestino");
            BigDecimal monto = new BigDecimal(request.get("monto").toString());
            String descripcion = (String) request.get("descripcion");

            movimientoService.realizarTransferencia(cuentaOrigenId, cuentaDestino, monto, descripcion);

            return ResponseEntity.ok().body("Transferencia realizada exitosamente");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al realizar transferencia: " + e.getMessage());
        }
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<?> obtenerMovimientosPorCuenta(
            @PathVariable Long cuentaId,
            @RequestParam(defaultValue = "10") int limite) {
        try {
            List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorCuenta(cuentaId, limite);
            return ResponseEntity.ok(movimientos);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener movimientos: " + e.getMessage());
        }
    }
}