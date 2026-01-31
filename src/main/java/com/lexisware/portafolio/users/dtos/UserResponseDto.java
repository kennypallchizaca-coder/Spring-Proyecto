package com.lexisware.portafolio.users.dtos;

import com.lexisware.portafolio.users.entities.UserEntity;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

// DTO para respuesta de usuario (NO expone password)
@Data
public class UserResponseDto {

    private String uid;
    private String email;
    private String displayName;
    private UserEntity.Role role;
    private String specialty;
    private String bio;
    private String photoURL;
    private Boolean available;
    private List<String> skills;
    private List<String> schedule;

    // Redes sociales
    private String github;
    private String instagram;
    private String whatsapp;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
