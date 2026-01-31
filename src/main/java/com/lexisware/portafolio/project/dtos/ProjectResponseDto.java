package com.lexisware.portafolio.project.dtos;

import com.lexisware.portafolio.project.entities.ProjectEntity;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

// DTO para respuesta de proyecto
@Data
public class ProjectResponseDto {

    private Long id;
    private String title;
    private String description;
    private ProjectEntity.Category category;
    private ProjectEntity.ProjectRole role;
    private List<String> techStack;
    private String repoUrl;
    private String demoUrl;
    private String imageUrl;
    private String programmerName;
    private LocalDateTime createdAt;

    // Información del propietario (sin datos sensibles)
    private OwnerDto owner;

    @Data
    public static class OwnerDto {
        private String uid;
        private String displayName;
        private String photoURL;
    }
}
