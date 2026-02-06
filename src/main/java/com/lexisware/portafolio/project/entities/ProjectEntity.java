package com.lexisware.portafolio.project.entities;

import com.lexisware.portafolio.portfolio.entities.PortfolioEntity;
import com.lexisware.portafolio.users.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

// Entidad JPA que representa un proyecto en la base de datos
@Entity
@Table(name = "projects")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario propietario del proyecto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties({ "password", "projects", "advisoriesAsProgrammer" })
    private UserEntity owner;

    // Portafolio al que pertenece este proyecto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    @JsonIgnoreProperties({ "projects", "user" })
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

    public ProjectEntity() {
    }

    public ProjectEntity(Long id, UserEntity owner, PortfolioEntity portfolio, String title, String description,
            Category category, ProjectRole role, List<String> techStack, String repoUrl, String demoUrl,
            String imageUrl, String programmerName, LocalDateTime createdAt) {
        this.id = id;
        this.owner = owner;
        this.portfolio = portfolio;
        this.title = title;
        this.description = description;
        this.category = category;
        this.role = role;
        this.techStack = techStack;
        this.repoUrl = repoUrl;
        this.demoUrl = demoUrl;
        this.imageUrl = imageUrl;
        this.programmerName = programmerName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public PortfolioEntity getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfolioEntity portfolio) {
        this.portfolio = portfolio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ProjectRole getRole() {
        return role;
    }

    public void setRole(ProjectRole role) {
        this.role = role;
    }

    public List<String> getTechStack() {
        return techStack;
    }

    public void setTechStack(List<String> techStack) {
        this.techStack = techStack;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getDemoUrl() {
        return demoUrl;
    }

    public void setDemoUrl(String demoUrl) {
        this.demoUrl = demoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProgrammerName() {
        return programmerName;
    }

    public void setProgrammerName(String programmerName) {
        this.programmerName = programmerName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProjectEntity that = (ProjectEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", role=" + role +
                ", techStack=" + techStack +
                ", repoUrl='" + repoUrl + '\'' +
                ", demoUrl='" + demoUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", programmerName='" + programmerName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
