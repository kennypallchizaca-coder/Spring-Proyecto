package com.lexisware.portafolio.users.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

// DTO para actualización de perfil de usuario
@Data
public class UserUpdateRequestDto {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String displayName;

    @Size(max = 1000, message = "La biografía no puede exceder 1000 caracteres")
    private String bio;

    private String specialty;

    @Pattern(regexp = "^(https?://).*", message = "La URL de la foto debe ser válida")
    private String photoURL;

    private List<String> skills;

    private List<String> schedule;

    private Boolean available;

    // Redes sociales
    @Pattern(regexp = "^(https?://)?.*", message = "La URL debe ser válida")
    private String github;

    @Pattern(regexp = "^(https?://)?.*", message = "La URL debe ser válida")
    private String instagram;

    @Pattern(regexp = "^(https?://)?.*", message = "La URL debe ser válida")
    private String whatsapp;
}
