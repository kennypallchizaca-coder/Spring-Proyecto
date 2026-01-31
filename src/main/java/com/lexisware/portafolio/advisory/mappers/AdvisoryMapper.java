package com.lexisware.portafolio.advisory.mappers;

import com.lexisware.portafolio.advisory.dtos.AdvisoryRequestDto;
import com.lexisware.portafolio.advisory.dtos.AdvisoryResponseDto;
import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import com.lexisware.portafolio.advisory.models.Advisory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

// Mapper para convertir entre Entidad, Modelo y DTOs
@Component
public class AdvisoryMapper {

    // Entidad -> Modelo
    public Advisory toModel(AdvisoryEntity entity) {
        if (entity == null)
            return null;
        Advisory model = new Advisory();
        model.setId(entity.getId());
        model.setProgrammerId(entity.getProgrammerId());
        model.setProgrammerEmail(entity.getProgrammerEmail());
        model.setProgrammerName(entity.getProgrammerName());
        model.setRequesterName(entity.getRequesterName());
        model.setRequesterEmail(entity.getRequesterEmail());
        model.setDate(entity.getDate());
        model.setTime(entity.getTime());
        model.setNote(entity.getNote());

        if (entity.getStatus() != null)
            model.setStatus(Advisory.Status.valueOf(entity.getStatus().name()));

        model.setCreatedAt(entity.getCreatedAt());
        return model;
    }

    // Modelo -> Entidad
    public AdvisoryEntity toEntity(Advisory model) {
        if (model == null)
            return null;
        AdvisoryEntity entity = new AdvisoryEntity();
        if (model.getId() != null)
            entity.setId(model.getId());

        entity.setProgrammerId(model.getProgrammerId());
        entity.setProgrammerEmail(model.getProgrammerEmail());
        entity.setProgrammerName(model.getProgrammerName());
        entity.setRequesterName(model.getRequesterName());
        entity.setRequesterEmail(model.getRequesterEmail());
        entity.setDate(model.getDate());
        entity.setTime(model.getTime());
        entity.setNote(model.getNote());

        if (model.getStatus() != null)
            entity.setStatus(AdvisoryEntity.Status.valueOf(model.getStatus().name()));

        entity.setCreatedAt(model.getCreatedAt());
        return entity;
    }

    // DTO -> Modelo
    public Advisory toModel(AdvisoryRequestDto dto) {
        if (dto == null)
            return null;
        Advisory model = new Advisory();
        model.setProgrammerId(dto.getProgrammerId());
        model.setProgrammerEmail(dto.getProgrammerEmail());
        model.setProgrammerName(dto.getProgrammerName());
        model.setRequesterName(dto.getRequesterName());
        model.setRequesterEmail(dto.getRequesterEmail());
        model.setDate(dto.getDate());
        model.setTime(dto.getTime());
        model.setNote(dto.getNote());
        return model;
    }

    // Modelo -> DTO
    public AdvisoryResponseDto toResponseDto(Advisory model) {
        if (model == null)
            return null;
        AdvisoryResponseDto dto = new AdvisoryResponseDto();
        dto.setId(model.getId());
        dto.setProgrammerId(model.getProgrammerId());
        dto.setProgrammerEmail(model.getProgrammerEmail());
        dto.setProgrammerName(model.getProgrammerName());
        dto.setRequesterName(model.getRequesterName());
        dto.setRequesterEmail(model.getRequesterEmail());
        dto.setDate(model.getDate());
        dto.setTime(model.getTime());
        dto.setNote(model.getNote());

        if (model.getStatus() != null)
            dto.setStatus(AdvisoryEntity.Status.valueOf(model.getStatus().name())); // DTO usa Enum de Entidad por ahora

        dto.setCreatedAt(model.getCreatedAt());
        return dto;
    }

    public List<AdvisoryResponseDto> toResponseDtoList(List<Advisory> models) {
        return models.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<Advisory> toModelList(List<AdvisoryEntity> entities) {
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
