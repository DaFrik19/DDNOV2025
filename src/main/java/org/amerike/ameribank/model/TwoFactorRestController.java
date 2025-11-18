package org.amerike.ameribank.model;

import org.amerike.ameribank.controller.TwoFactorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/2fa")
public class TwoFactorRestController {
    private final TwoFactorService svc = new TwoFactorService();

    // /api/2fa/generate
    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestBody Map<String, Object> body) {
        Integer usuarioId = body.get("usuarioId") instanceof Number ? ((Number) body.get("usuarioId")).intValue() : 1;
        try {
            // almacena key y llama a la blacklist
            String code = svc.generateAndStore(usuarioId, "APP", true, false, false);
            Map<String, Object> resp = new HashMap<>();
            resp.put("code", code);
            resp.put("expiresIn", 30);
            return ResponseEntity.ok(resp);
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Error generando 2FA: " + e.getMessage());
        }
    }

    // /api/2fa/verify
    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody Map<String, Object> body) {
        Integer usuarioId = body.get("usuarioId") instanceof Number ? ((Number) body.get("usuarioId")).intValue() : 1;
        String code = body.get("code") == null ? "" : body.get("code").toString();
        try {
            boolean ok = svc.verify(usuarioId, code);
            if (ok) return ResponseEntity.ok("Sesión Iniciada.");
            else return ResponseEntity.status(401).body("Verificación fallida o código expirado.");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Error verificando 2FA: " + e.getMessage());
        }
    }

}


