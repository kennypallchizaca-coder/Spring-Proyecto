package com.lexisware.portafolio.project.dtos;

import com.lexisware.portafolio.project.entities.ProjectEntity;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

// DTO para la captura y validación de datos en la creación o actualización de proyectos
@Data
public class ProjectRequestDto {

    @NotBlank(message = "El título del proyecto es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String title;

    @NotBlank(message = "La descripción del proyecto es obligatoria")
    @Size(min = 10, max = 1000, message = "La descripción debe tener entre 10 y 1000 caracteres")
    private String description;

    @NotNull(message = "La categoría del proyecto es obligatoria")
    private ProjectEntity.Category category;

    @NotNull(message = "El rol en el proyecto es obligatorio")
    private ProjectEntity.ProjectRole role;

    private List<String> techStack;

    @Pattern(regexp = "^(https?://).*", message = "La URL del repositorio debe ser válida")
    private String repoUrl;

    @Pattern(regexp = "^(https?://).*", message = "La URL de la demo debe ser válida")
    private String demoUrl;

    private String imageUrl;

    private String ownerUid;

    private Long portfolioId;
}
