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

// servicio de autenticación con jwt
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final UserMapper userMapper;

    // registrar nuevo usuario
    @Transactional
    public AuthResponse registrar(RegisterRequest request) {
        log.info("Registrando usuario: {}", request.getEmail());

        // verificar si el email ya existe
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // crear nuevo usuario
        UserEntity user = new UserEntity();
        user.setUid(UUID.randomUUID().toString());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDisplayName(request.getDisplayName());

        // asignar rol - si no viene, usar EXTERNAL por defecto
        String role = (request.getRole() != null && !request.getRole().isBlank())
                ? request.getRole().toUpperCase()
                : "EXTERNAL";
        user.setRole(UserEntity.Role.valueOf(role));
        user.setAvailable(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // guardar en base de datos
        user = userRepository.save(user);

        // enviar email de bienvenida
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getDisplayName());
        } catch (Exception e) {
            log.error("Error enviando email de bienvenida: {}", e.getMessage());
        }

        // generar token jwt
        String token = jwtTokenProvider.generarToken(
                user.getUid(),
                user.getEmail(),
                user.getRole().name());

        log.info("Usuario registrado exitosamente: {}", user.getEmail());

        return new AuthResponse(token, AuthResponse.UserDTO.fromEntity(user));
    }

    // iniciar sesión
    public AuthResponse iniciarSesion(LoginRequest request) {
        log.info("Iniciando sesión: {}", request.getEmail());

        // buscar usuario por email
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        // verificar contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        // actualizar última conexión
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // generar token jwt
        String token = jwtTokenProvider.generarToken(
                user.getUid(),
                user.getEmail(),
                user.getRole().name());

        log.info("Login exitoso: {}", user.getEmail());

        return new AuthResponse(token, AuthResponse.UserDTO.fromEntity(user));
    }

    // obtener usuario actual
    public User obtenerUsuarioActual(String uid) {
        UserEntity entity = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toModel(entity);
    }
}
