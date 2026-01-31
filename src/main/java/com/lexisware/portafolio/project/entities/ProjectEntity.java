package com.lexisware.portafolio.project.entities;

import com.lexisware.portafolio.portfolio.entities.PortfolioEntity;
import com.lexisware.portafolio.users.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

//Entidad Proyecto - Representa proyectos académicos o laborales
@Entity
@Table(name = "projects")
@Data
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación ManyToOne con User (dueño del proyecto)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties({ "password", "projects", "advisoriesAsProgrammer" })
    private UserEntity owner;

    // Relación ManyToOne con Portfolio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    @JsonIgnoreProperties({ "projects", "user" })
    private PortfolioEntity portfolio;

    @Column(nullable = false)
    private String title; // Título del proyecto

    @Column(length = 1000)
    private String description; // Descripción detallada

    @Enumerated(EnumType.STRING)
    private Category category; // Académico o Laboral

    @Enumerated(EnumType.STRING)
    private ProjectRole role; // Rol en el proyecto

    // Stack tecnológico utilizado
    @ElementCollection
    @CollectionTable(name = "project_tech_stack", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology")
    private List<String> techStack;

    private String repoUrl; // URL del repositorio
    private String demoUrl; // URL de la demo
    private String imageUrl; // URL de imagen del proyecto

    @Column(name = "programmer_name")
    private String programmerName; // Nombre del programador

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Categoría del proyecto
    public enum Category {
        academico, // Proyecto académico
        laboral // Proyecto laboral
    }

    // Rol del programador en el proyecto
    public enum ProjectRole {
        frontend, // Desarrollo frontend
        backend, // Desarrollo backend
        fullstack, // Desarrollo fullstack
        db // Diseño de base de datos
    }
}
