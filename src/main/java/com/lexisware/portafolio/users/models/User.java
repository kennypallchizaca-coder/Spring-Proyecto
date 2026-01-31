package com.lexisware.portafolio.users.models;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

// Modelo de Dominio de Usuario (Agnóstico de persistencia)
@Data
public class User {
    private String uid;
    private String email;
    private String password; // Solo para uso interno del servicio
    private String displayName;
    private Role role;
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

    public enum Role {
        PROGRAMMER,
        ADMIN,
        EXTERNAL
    }
}
