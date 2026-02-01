package com.lexisware.portafolio.auth.services;

import com.lexisware.portafolio.utils.EmailService;
import com.lexisware.portafolio.auth.dtos.AuthResponse;
import com.lexisware.portafolio.auth.dtos.LoginRequest;
import com.lexisware.portafolio.auth.dtos.RegisterRequest;
import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.users.models.User;
import com.lexisware.portafolio.users.mappers.UserMapper;
import com.lexisware.portafolio.utils.ResourceNotFoundException;
import com.lexisware.portafolio.utils.UnauthorizedException;
import com.lexisware.portafolio.users.repositories.UserRepository;
import com.lexisware.portafolio.config.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

// Servicio de autenticación y gestión de sesiones
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final UserMapper userMapper;

    // Procesa el registro de nuevos usuarios en el sistema
    @Transactional
    public AuthResponse registrar(RegisterRequest request) {
        log.info("Registrando usuario: {}", request.getEmail());

        // Validar unicidad del correo electrónico
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Mapear datos del DTO a la entidad de persistencia
        UserEntity user = new UserEntity();
        user.setUid(UUID.randomUUID().toString());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encriptar contraseña
        user.setDisplayName(request.getDisplayName());

        // Determinar rol del usuario (EXTERNAL por defecto si no se especifica)
        String roleStr = (request.getRole() == null || request.getRole().isBlank()) ? "EXTERNAL"
                : request.getRole().toUpperCase();
        user.setRole(UserEntity.Role.valueOf(roleStr));
        user.setAvailable(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Persistir el nuevo usuario en la base de datos
        user = userRepository.save(user);

        // Notificar al usuario vía correo electrónico
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getDisplayName());
        } catch (Exception e) {
            log.error("Error enviando email de bienvenida: {}", e.getMessage());
        }

        // Generar token JWT para autenticación inmediata
        String token = jwtTokenProvider.generarToken(
                user.getUid(),
                user.getEmail(),
                user.getRole().name());

        log.info("Usuario registrado exitosamente: {}", user.getEmail());

        return new AuthResponse(token, AuthResponse.UserDTO.fromEntity(user));
    }

    // Valida credenciales y genera sesión para el usuario
    public AuthResponse iniciarSesion(LoginRequest request) {
        log.info("Iniciando sesión: {}", request.getEmail());

        // Verificar existencia del usuario
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        // Comparar contraseña hash con la proporcionada
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        // Registrar marca de tiempo de la última actividad
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Generar nuevo token de acceso
        String token = jwtTokenProvider.generarToken(
                user.getUid(),
                user.getEmail(),
                user.getRole().name());

        log.info("Login exitoso: {}", user.getEmail());

        return new AuthResponse(token, AuthResponse.UserDTO.fromEntity(user));
    }

    // Recupera la información del usuario basada en su UID
    public User obtenerUsuarioActual(String uid) {
        UserEntity entity = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toModel(entity);
    }
}
