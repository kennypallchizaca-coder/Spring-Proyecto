package com.lexisware.portafolio.project.mappers;

import com.lexisware.portafolio.project.dtos.ProjectRequestDto;
import com.lexisware.portafolio.project.dtos.ProjectResponseDto;
import com.lexisware.portafolio.project.entities.ProjectEntity;
import com.lexisware.portafolio.project.models.Project;
import com.lexisware.portafolio.portfolio.mappers.PortfolioMapper;
import com.lexisware.portafolio.users.mappers.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

// Mapper para convertir entre Entidad, Modelo y DTOs
@Component
public class ProjectMapper {

    @Autowired
    @Lazy
    private UserMapper userMapper;
    @Autowired
    @Lazy
    private PortfolioMapper portfolioMapper;

    // Entidad -> Modelo
    public Project toModel(ProjectEntity entity) {
        if (entity == null)
            return null;
        Project model = new Project();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setDescription(entity.getDescription());

        if (entity.getCategory() != null)
            model.setCategory(Project.Category.valueOf(entity.getCategory().name()));

        if (entity.getRole() != null)
            model.setRole(Project.ProjectRole.valueOf(entity.getRole().name()));

        model.setTechStack(entity.getTechStack());
        model.setRepoUrl(entity.getRepoUrl());
        model.setDemoUrl(entity.getDemoUrl());
        model.setImageUrl(entity.getImageUrl());
        model.setProgrammerName(entity.getProgrammerName());
        model.setCreatedAt(entity.getCreatedAt());

        // Mapear relaciones
        if (entity.getOwner() != null) {
            model.setOwner(userMapper.toModel(entity.getOwner()));
        }
        if (entity.getPortfolio() != null) {
            model.setPortfolio(portfolioMapper.toModel(entity.getPortfolio()));
        }

        return model;
    }

    // Modelo -> Entidad
    public ProjectEntity toEntity(Project model) {
        if (model == null)
            return null;
        ProjectEntity entity = new ProjectEntity();
        if (model.getId() != null)
            entity.setId(model.getId());

        entity.setTitle(model.getTitle());
        entity.setDescription(model.getDescription());

        if (model.getCategory() != null)
            entity.setCategory(ProjectEntity.Category.valueOf(model.getCategory().name()));

        if (model.getRole() != null)
            entity.setRole(ProjectEntity.ProjectRole.valueOf(model.getRole().name()));

        entity.setTechStack(model.getTechStack());
        entity.setRepoUrl(model.getRepoUrl());
        entity.setDemoUrl(model.getDemoUrl());
        entity.setImageUrl(model.getImageUrl());
        entity.setProgrammerName(model.getProgrammerName());
        entity.setCreatedAt(model.getCreatedAt());

        if (model.getOwner() != null) {
            entity.setOwner(userMapper.toEntity(model.getOwner()));
        }
        if (model.getPortfolio() != null) {
            entity.setPortfolio(portfolioMapper.toEntity(model.getPortfolio()));
        }

        return entity;
    }

    // DTO -> Modelo
    public Project toModel(ProjectRequestDto dto) {
        if (dto == null)
            return null;
        Project model = new Project();
        model.setTitle(dto.getTitle());
        model.setDescription(dto.getDescription());

        // Mapeo de Enums (Los Enums de Entidad usados en DTO coinciden con los del
        // Modelo)
        if (dto.getCategory() != null)
            model.setCategory(Project.Category.valueOf(dto.getCategory().name()));

        if (dto.getRole() != null)
            model.setRole(Project.ProjectRole.valueOf(dto.getRole().name()));

        model.setTechStack(dto.getTechStack());
        model.setRepoUrl(dto.getRepoUrl());
        model.setDemoUrl(dto.getDemoUrl());
        model.setImageUrl(dto.getImageUrl());

        // Los IDs de Owner y Portfolio son manejados en el Controlador o Servicio para
        // obtener el Modelo
        // Generalmente, el Servicio busca las dependencias por ID y las establece en el
        // Modelo.

        return model;
    }

    public void updateModel(Project model, ProjectRequestDto dto) {
        if (dto.getTitle() != null)
            model.setTitle(dto.getTitle());
        if (dto.getDescription() != null)
            model.setDescription(dto.getDescription());

        if (dto.getCategory() != null)
            model.setCategory(Project.Category.valueOf(dto.getCategory().name()));

        if (dto.getRole() != null)
            model.setRole(Project.ProjectRole.valueOf(dto.getRole().name()));

        if (dto.getTechStack() != null)
            model.setTechStack(dto.getTechStack());
        if (dto.getRepoUrl() != null)
            model.setRepoUrl(dto.getRepoUrl());
        if (dto.getDemoUrl() != null)
            model.setDemoUrl(dto.getDemoUrl());
        if (dto.getImageUrl() != null)
            model.setImageUrl(dto.getImageUrl());
    }

    // Modelo -> DTO
    public ProjectResponseDto toResponseDto(Project model) {
        if (model == null)
            return null;
        ProjectResponseDto dto = new ProjectResponseDto();
        dto.setId(model.getId());
        dto.setTitle(model.getTitle());
        dto.setDescription(model.getDescription());

        if (model.getCategory() != null)
            dto.setCategory(ProjectEntity.Category.valueOf(model.getCategory().name())); // DTO usa tipo Entidad por
                                                                                         // ahora

        if (model.getRole() != null)
            dto.setRole(ProjectEntity.ProjectRole.valueOf(model.getRole().name()));

        dto.setTechStack(model.getTechStack());
        dto.setRepoUrl(model.getRepoUrl());
        dto.setDemoUrl(model.getDemoUrl());
        dto.setImageUrl(model.getImageUrl());
        dto.setProgrammerName(model.getProgrammerName());
        dto.setCreatedAt(model.getCreatedAt());

        if (model.getOwner() != null) {
            ProjectResponseDto.OwnerDto ownerDto = new ProjectResponseDto.OwnerDto();
            ownerDto.setUid(model.getOwner().getUid());
            ownerDto.setDisplayName(model.getOwner().getDisplayName());
            ownerDto.setPhotoURL(model.getOwner().getPhotoURL());
            dto.setOwner(ownerDto);
        }

        return dto;
    }

    public List<ProjectResponseDto> toResponseDtoList(List<Project> models) {
        return models.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
