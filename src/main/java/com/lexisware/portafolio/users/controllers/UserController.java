package com.lexisware.portafolio.users.controllers;

import com.lexisware.portafolio.users.services.UserService;
import com.lexisware.portafolio.users.mappers.UserMapper;
import com.lexisware.portafolio.users.dtos.UserResponseDto;
import com.lexisware.portafolio.users.dtos.UserUpdateRequestDto;
import com.lexisware.portafolio.users.models.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

// Controlador REST para la gestión de perfiles de usuario y programadores
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    // Retorna una lista de todos los usuarios registrados con el rol de programador
    @GetMapping("/programmers")
    public ResponseEntity<List<UserResponseDto>> obtenerTodosLosProgramadores() {
        List<User> users = userService.obtenerProgramadores();
        return ResponseEntity.ok(userMapper.toResponseDtoList(users));
    }

    // Retorna los programadores que han marcado su perfil como disponible para
    // asesorías
    @GetMapping("/programmers/available")
    public ResponseEntity<List<UserResponseDto>> obtenerProgramadoresDisponibles() {
        List<User> users = userService.obtenerProgramadoresDisponibles();
        return ResponseEntity.ok(userMapper.toResponseDtoList(users));
    }

    // Obtiene el perfil completo del usuario autenticado actualmente
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> obtenerUsuarioActual(@AuthenticationPrincipal String uid) {
        User user = userService.obtenerUsuarioPorId(uid);
        return ResponseEntity.ok(userMapper.toResponseDto(user));
    }

    // Busca un perfil de usuario específico por su identificador único (UID)
    @GetMapping("/{uid}")
    public ResponseEntity<UserResponseDto> obtenerUsuarioPorId(@PathVariable("uid") String uid) {
        User user = userService.obtenerUsuarioPorId(uid);
        return ResponseEntity.ok(userMapper.toResponseDto(user));
    }

    // Crea un nuevo registro de usuario o actualiza completamente uno existente
    @PostMapping
    public ResponseEntity<UserResponseDto> crearOActualizarUsuario(@RequestBody User user) {
        User saved = userService.crearOActualizarUsuario(user);
        return ResponseEntity.ok(userMapper.toResponseDto(saved));
    }

    // Permite la actualización parcial de campos del perfil de usuario
    @PatchMapping("/{uid}")
    public ResponseEntity<UserResponseDto> actualizarUsuario(
            @PathVariable("uid") String uid,
            @Valid @RequestBody UserUpdateRequestDto updateRequest) {

        User user = userService.obtenerUsuarioPorId(uid);
        userMapper.updateModel(user, updateRequest);
        User updated = userService.crearOActualizarUsuario(user);

        return ResponseEntity.ok(userMapper.toResponseDto(updated));
    }

    // Cambia el estado de disponibilidad del programador para recibir nuevas
    // asesorías
    @PatchMapping("/{uid}/availability")
    public ResponseEntity<UserResponseDto> actualizarDisponibilidad(
            @PathVariable("uid") String uid,
            @RequestBody Map<String, Boolean> body) {
        boolean available = body.getOrDefault("available", false);
        User updated = userService.actualizarDisponibilidad(uid, available);
        return ResponseEntity.ok(userMapper.toResponseDto(updated));
    }

    // Elimina la cuenta y todos los datos asociados al usuario
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable("uid") String uid) {
        userService.eliminarUsuario(uid);
        return ResponseEntity.noContent().build();
    }
}
