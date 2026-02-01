package com.lexisware.portafolio.portfolio.dtos;

import lombok.Data;
import java.time.LocalDateTime;

// DTO de respuesta que contiene la información pública de un portafolio
@Data
public class PortfolioResponseDto {

    private Long id;
    private String userId;
    private String title;
    private String description;
    private String theme;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
