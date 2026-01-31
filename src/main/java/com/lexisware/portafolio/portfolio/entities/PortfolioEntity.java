package com.lexisware.portafolio.portfolio.entities;

import com.lexisware.portafolio.project.entities.ProjectEntity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Entidad Portafolio - Representa el portafolio de un usuario
@Entity
@Table(name = "portfolios")
@Data
public class PortfolioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "user_id")
    private String userId; // UID del propietario

    // Relación OneToMany con Projects
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectEntity> projects = new ArrayList<>();

    @Column(nullable = false)
    private String title; // Título del portafolio

    @Column(length = 2000)
    private String description; // Descripción del portafolio

    private String theme; // Tema visual del portafolio

    private Boolean isPublic = true; // Portafolio público o privado

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
