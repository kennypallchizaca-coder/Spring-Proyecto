package com.lexisware.portafolio.auth.controllers;

import com.lexisware.portafolio.auth.dtos.AuthResponse;
import com.lexisware.portafolio.auth.dtos.LoginRequest;
import com.lexisware.portafolio.auth.dtos.RegisterRequest;
import com.lexisware.portafolio.users.models.User;
import com.lexisware.portafolio.auth.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// controlador de autenticación
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    // Registrar una nueva cuenta de usuario
    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Crea una nueva cuenta de usuario y retorna el token de acceso")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Autenticar usuario existente
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica usuario y retorna token JWT")
    public ResponseEntity<AuthResponse> iniciarSesion(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.iniciarSesion(request);
        return ResponseEntity.ok(response);
    }

    // Obtener información del perfil del usuario autenticado
    @GetMapping("/me")
    @Operation(summary = "Obtener usuario actual", description = "Retorna información del usuario autenticado")
    public ResponseEntity<User> obtenerUsuarioActual(@AuthenticationPrincipal String uid) {
        User user = authService.obtenerUsuarioActual(uid);
        return ResponseEntity.ok(user);
    }
}
