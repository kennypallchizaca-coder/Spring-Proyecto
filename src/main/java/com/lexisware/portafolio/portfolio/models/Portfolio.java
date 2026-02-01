package com.lexisware.portafolio.portfolio.models;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.lexisware.portafolio.project.models.Project;

// Modelo de dominio para representar un Portafolio de programador
@Data
public class Portfolio {
    private Long id;
    private String userId;
    private List<Project> projects;
    private String title;
    private String description;
    private String theme;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
