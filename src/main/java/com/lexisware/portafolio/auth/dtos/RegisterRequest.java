package com.lexisware.portafolio.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// DTO para la captura de datos durante el registro de nuevos usuarios
@Data
public class RegisterRequest {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "El password es obligatorio")
    @Size(min = 6, message = "El password debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    private String displayName;

    // Rol asignado al usuario (ROLE_USER, ROLE_ADMIN, ROLE_PROGRAMMER)
    private String role;
}
