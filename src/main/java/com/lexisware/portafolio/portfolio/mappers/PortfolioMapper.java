package com.lexisware.portafolio.portfolio.mappers;

import com.lexisware.portafolio.portfolio.dtos.PortfolioRequestDto;
import com.lexisware.portafolio.portfolio.dtos.PortfolioResponseDto;
import com.lexisware.portafolio.portfolio.entities.PortfolioEntity;
import com.lexisware.portafolio.portfolio.models.Portfolio;
import com.lexisware.portafolio.project.mappers.ProjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Mapper para convertir entre Entidad, Modelo y DTOs
@Component
public class PortfolioMapper {

    @Autowired
    @Lazy // Evitar dependencia circular con ProjectMapper
    private ProjectMapper projectMapper;

    // Entidad -> Modelo
    public Portfolio toModel(PortfolioEntity entity) {
        if (entity == null)
            return null;
        Portfolio model = new Portfolio();
        model.setId(entity.getId());
        model.setUserId(entity.getUserId());
        model.setTitle(entity.getTitle());
        model.setDescription(entity.getDescription());
        model.setTheme(entity.getTheme());
        model.setIsPublic(entity.getIsPublic());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());

        // Mapear proyectos si existen, evitando recursión infinita
        // Una estrategia es no mapear "hacia arriba" (referencia inversa) en el hijo.
        // Aquí Portfolio es padre.
        if (entity.getProjects() != null) {
            // model.setProjects(projectMapper.toModelList(entity.getProjects()));
            // Por simplicidad, podemos dejarlo null o mapearlo si ProjectMapper soporta
            // evitar ciclos
            model.setProjects(Collections.emptyList()); // Evitamos ciclo por ahora
        }

        return model;
    }

    // Modelo -> Entidad
    public PortfolioEntity toEntity(Portfolio model) {
        if (model == null)
            return null;
        PortfolioEntity entity = new PortfolioEntity();
        if (model.getId() != null) {
            entity.setId(model.getId());
        }
        entity.setUserId(model.getUserId());
        entity.setTitle(model.getTitle());
        entity.setDescription(model.getDescription());
        entity.setTheme(model.getTheme());
        entity.setIsPublic(model.getIsPublic());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        return entity;
    }

    // DTO -> Modelo
    public Portfolio toModel(PortfolioRequestDto dto) {
        if (dto == null)
            return null;
        Portfolio model = new Portfolio();
        model.setUserId(dto.getUserId());
        model.setTitle(dto.getTitle());
        model.setDescription(dto.getDescription());
        model.setTheme(dto.getTheme());
        model.setIsPublic(dto.getIsPublic());
        return model;
    }

    public void updateModel(Portfolio model, PortfolioRequestDto dto) {
        if (dto.getTitle() != null)
            model.setTitle(dto.getTitle());
        if (dto.getDescription() != null)
            model.setDescription(dto.getDescription());
        if (dto.getTheme() != null)
            model.setTheme(dto.getTheme());
        if (dto.getIsPublic() != null)
            model.setIsPublic(dto.getIsPublic());
    }

    // Modelo -> DTO
    public PortfolioResponseDto toResponseDto(Portfolio model) {
        if (model == null)
            return null;
        PortfolioResponseDto dto = new PortfolioResponseDto();
        dto.setId(model.getId());
        dto.setUserId(model.getUserId());
        dto.setTitle(model.getTitle());
        dto.setDescription(model.getDescription());
        dto.setTheme(model.getTheme());
        dto.setIsPublic(model.getIsPublic());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setUpdatedAt(model.getUpdatedAt());
        return dto;
    }

    public List<PortfolioResponseDto> toResponseDtoList(List<Portfolio> models) {
        return models.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<Portfolio> toModelList(List<PortfolioEntity> entities) {
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
