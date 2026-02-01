package com.lexisware.portafolio.users.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import com.lexisware.portafolio.project.entities.ProjectEntity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Entidad Usuario - Representa programadores, administradores y usuarios externos
@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    private String uid; // ID único del usuario

    @Column(nullable = false, unique = true)
    private String email;

    // Password hasheado con BCrypt
    @Column(nullable = false)
    private String password;

    @Column(name = "display_name")
    private String displayName;

    @Enumerated(EnumType.STRING)
    private Role role; // PROGRAMMER, ADMIN, EXTERNAL

    private String specialty; // Especialidad del programador

    @Column(length = 1000)
    private String bio; // Biografía

    private String photoURL; // URL de foto de perfil

    private Boolean available = false; // Disponible para asesorías

    // Redes sociales
    private String github; // URL de GitHub
    private String instagram; // URL de Instagram
    private String whatsapp; // URL de WhatsApp

    // Habilidades técnicas del usuario
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_skills", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "skill")
    private List<String> skills;

    // Horarios disponibles para asesorías
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_schedules", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "time_slot")
    private List<String> schedule;

    // Relación OneToMany con Projects (proyectos que pertenecen al usuario)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("owner")
    private List<ProjectEntity> projects = new ArrayList<>();

    // Relación OneToMany con Advisories (asesorías donde el usuario es programador)
    @OneToMany(mappedBy = "programmer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("programmer")
    private List<AdvisoryEntity> advisoriesAsProgrammer = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // roles de usuario
    public enum Role {
        PROGRAMMER, // programador que puede recibir asesorías
        ADMIN, // administrador del sistema
        EXTERNAL, // usuario externo que solicita asesorías
    }
}
