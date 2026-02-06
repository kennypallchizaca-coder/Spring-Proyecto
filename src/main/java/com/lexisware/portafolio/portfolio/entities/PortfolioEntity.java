package com.lexisware.portafolio.portfolio.entities;

import com.lexisware.portafolio.project.entities.ProjectEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Entidad JPA que representa el portafolio de un programador
@Entity
@Table(name = "portfolios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PortfolioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true, name = "user_id")
    private String userId; // Identificador único del usuario propietario (UID)

    // Lista de proyectos vinculados a este portafolio
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ProjectEntity> projects = new ArrayList<>();

    @Column(nullable = false)
    private String title; // Título principal del portafolio

    @Column(length = 2000)
    private String description; // Descripción detallada o perfil del programador

    private String theme; // Preferencia visual o tema del portafolio
    private Boolean isPublic = true; // Define si el portafolio es visible públicamente

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
