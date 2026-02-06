package com.lexisware.portafolio.project.entities;

import com.lexisware.portafolio.portfolio.entities.PortfolioEntity;
import com.lexisware.portafolio.users.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;

// Entidad JPA que representa un proyecto en la base de datos
@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // Usuario propietario del proyecto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties({ "password", "projects", "advisoriesAsProgrammer" })
    @ToString.Exclude
    private UserEntity owner;

    // Portafolio al que pertenece este proyecto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    @JsonIgnoreProperties({ "projects", "user" })
    @ToString.Exclude
    private PortfolioEntity portfolio;

    @Column(nullable = false)
    private String title; // Título descriptivo del proyecto

    @Column(length = 1000)
    private String description; // Breve explicación de las funcionalidades del proyecto

    @Enumerated(EnumType.STRING)
    private Category category; // Clasificación del proyecto (académico o laboral)

    @Enumerated(EnumType.STRING)
    private ProjectRole role; // Rol desempeñado por el autor del proyecto

    // Lista de tecnologías, lenguajes y frameworks utilizados
    @ElementCollection
    @CollectionTable(name = "project_tech_stack", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology")
    private List<String> techStack;

    private String repoUrl; // Enlace al repositorio de código fuente (GitHub, GitLab, etc.)
    private String demoUrl; // Enlace a la demostración en vivo o despliegue
    private String imageUrl; // URL de la imagen representativa del proyecto

    @Column(name = "programmer_name")
    private String programmerName; // Nombre redundante del programador para búsquedas rápidas

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Define el tipo de proyecto
    public enum Category {
        academico, // Realizado en entorno educativo
        laboral // Realizado para un cliente o empresa
    }

    // Define el rol desempeñado en el proyecto
    public enum ProjectRole {
        frontend, // Desarrollo de interfaz
        backend, // Desarrollo de lógica de servidor
        fullstack, // Desarrollo integral
        db // Enfoque en base de datos y arquitectura
    }
}
