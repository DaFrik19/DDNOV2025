package org.amerike.ameribank.controller;

import org.amerike.ameribank.dao.loginDAO;
import org.amerike.ameribank.model.login;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class loginController {
    private final loginDAO lDAO;

    public loginController(loginDAO lDAO) {
        this.lDAO = lDAO;
    }

    @PostMapping("/loggear")
    // Se cambia ResponseEntity<String> a ResponseEntity<?> para permitir el cuerpo JSON (Map)
    public ResponseEntity<?> iniciarSesion(@RequestBody login l) {

        try {
            lDAO.validarCredenciales(l);

            // --- INICIO DE MODIFICACIÓN CRÍTICA PARA 2FA ---
            // 1. Crear el objeto de respuesta JSON esperado por el frontend
            Map<String, Object> resp = new HashMap<>();

            // 2. Definir el usuario ID.
            // NOTA: Tu DAO actual solo retorna boolean/excepción. En un sistema real, el DAO
            // debería retornar el ID del usuario. Asumimos 1 para propósitos de demostración.
            int usuarioId = 1;

            // 3. Setear las claves que el frontend busca para mostrar el modal 2FA
            resp.put("status", "2FA_REQUIRED");
            resp.put("usuarioId", usuarioId);
            resp.put("message", "Credenciales válidas. Requiere verificación 2FA.");

            // 4. Retornar el objeto JSON con el estado HTTP 202 ACCEPTED
            // El estado 202 es ideal para indicar que se requiere más procesamiento (2FA).
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(resp);
            // --- FIN DE MODIFICACIÓN CRÍTICA ---

        } catch (RuntimeException e) {

            String mensajeError = e.getMessage();

            if (mensajeError != null && mensajeError.contains("Fallo en la validación de credenciales")) {

                String mensajeMySQL = mensajeError.substring("Fallo en la validación de credenciales: ".length());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(mensajeMySQL);
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor. Inténtelo más tarde.");
        }
    }
}