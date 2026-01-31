package com.lexisware.portafolio.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Controlador para endpoints públicos y verificación de salud
@RestController
@RequestMapping("/api/public")
public class PublicController {

    // Endpoint de verificación de salud
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "LEXISWARE Portfolio Backend");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    // Endpoint de información
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {
        Map<String, String> info = new HashMap<>();
        info.put("name", "LEXISWARE Portfolio API");
        info.put("description", "Backend para sistema de portafolios");
        info.put("version", "1.0.0");
        info.put("author", "LEXIS-TEAM");
        return ResponseEntity.ok(info);
    }
}
