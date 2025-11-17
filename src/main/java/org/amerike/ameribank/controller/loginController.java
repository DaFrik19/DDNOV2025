package org.amerike.ameribank.controller;

import org.amerike.ameribank.dao.loginDAO;
import org.amerike.ameribank.model.login;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class loginController {
    private final loginDAO lDAO;

    public loginController(loginDAO lDAO) {
        this.lDAO = lDAO;
    }

    @PostMapping("/loggear")
    public ResponseEntity<String> iniciarSesion(@RequestBody login l) {

        try {
            lDAO.validarCredenciales(l);
            return ResponseEntity.ok("Inicio de sesión exitoso");

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

/*
El controller existe y eso es algo. Se pone a si mismo la ruta '/api/login' y manda a llamar al DAO.
Crea un nuevo directorio '/loggear' dónde va a estar la respuesta al inicio de sesión, si accede se muestra
el inicio de sesión exitoso, si falla en alguna parte va a devolver los errores.

Esta parte se conecta con el login.HTML para dar el resultado en la página.
*/