package com.lexisware.portafolio.portfolio.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

// DTO para recibir los parámetros de configuración de un portafolio
@Data
public class PortfolioRequestDto {

    @NotBlank(message = "El ID del usuario es obligatorio")
    private String userId;

    @NotBlank(message = "El título del portafolio es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String title;

    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    private String description;

    private String theme;

    private Boolean isPublic = true;
}
