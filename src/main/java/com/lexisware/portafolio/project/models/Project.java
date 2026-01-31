package com.lexisware.portafolio.project.models;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.lexisware.portafolio.users.models.User;
import com.lexisware.portafolio.portfolio.models.Portfolio;

// Modelo de Dominio de Proyecto
@Data
public class Project {
    private Long id;
    private User owner;
    private Portfolio portfolio;
    private String title;
    private String description;
    private Category category;
    private ProjectRole role;
    private List<String> techStack;
    private String repoUrl;
    private String demoUrl;
    private String imageUrl;
    private String programmerName;
    private LocalDateTime createdAt;

    public enum Category {
        academico,
        laboral
    }

    public enum ProjectRole {
        frontend,
        backend,
        fullstack,
        db
    }
}
