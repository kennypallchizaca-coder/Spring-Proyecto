package com.lexisware.portafolio.users.mappers;

import com.lexisware.portafolio.users.dtos.UserResponseDto;
import com.lexisware.portafolio.users.dtos.UserUpdateRequestDto;
import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.users.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

// Clase encargada de la transformación de datos entre Entidades JPA, Modelos de Negocio y DTOs de Usuario
@Component
public class UserMapper {

    // Transforma una entidad de base de datos a un modelo de negocio
    public User toModel(UserEntity entity) {
        // Validar nulidad para evitar excepciones de puntero nulo
        if (entity == null)
            return null;

        User model = new User();
        model.setUid(entity.getUid());
        model.setEmail(entity.getEmail());
        model.setPassword(entity.getPassword());
        model.setDisplayName(entity.getDisplayName());
        // Mapeo seguro de enumeraciones de roles
        if (entity.getRole() != null) {
            model.setRole(User.Role.valueOf(entity.getRole().name()));
        }
        model.setSpecialty(entity.getSpecialty());
        model.setBio(entity.getBio());
        model.setPhotoURL(entity.getPhotoURL());
        model.setAvailable(entity.getAvailable());
        model.setSkills(entity.getSkills() != null ? new ArrayList<>(entity.getSkills()) : new ArrayList<>());
        model.setSchedule(entity.getSchedule() != null ? new ArrayList<>(entity.getSchedule()) : new ArrayList<>());
        model.setGithub(entity.getGithub());
        model.setInstagram(entity.getInstagram());
        model.setWhatsapp(entity.getWhatsapp());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    // Transforma un modelo de negocio a una entidad JPA para persistencia
    public UserEntity toEntity(User model) {
        // Validar nulidad antes de la conversión
        if (model == null)
            return null;

        UserEntity entity = new UserEntity();
        entity.setUid(model.getUid());
        entity.setEmail(model.getEmail());
        entity.setPassword(model.getPassword());
        entity.setDisplayName(model.getDisplayName());
        // Mapeo de roles para compatibilidad con la base de datos
        if (model.getRole() != null) {
            entity.setRole(UserEntity.Role.valueOf(model.getRole().name()));
        }
        entity.setSpecialty(model.getSpecialty());
        entity.setBio(model.getBio());
        entity.setPhotoURL(model.getPhotoURL());
        entity.setAvailable(model.getAvailable());
        entity.setSkills(model.getSkills() != null ? new ArrayList<>(model.getSkills()) : new ArrayList<>());
        entity.setSchedule(model.getSchedule() != null ? new ArrayList<>(model.getSchedule()) : new ArrayList<>());
        entity.setGithub(model.getGithub());
        entity.setInstagram(model.getInstagram());
        entity.setWhatsapp(model.getWhatsapp());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        return entity;
    }

    // Aplica actualizaciones parciales desde un DTO de actualización al modelo de
    // usuario existente
    public void updateModel(User user, UserUpdateRequestDto dto) {
        // Actualizar nombre si se proporciona en la petición
        if (dto.getDisplayName() != null) {
            user.setDisplayName(dto.getDisplayName());
        }
        // Actualizar biografía si se proporciona
        if (dto.getBio() != null) {
            user.setBio(dto.getBio());
        }
        // Actualizar especialidad del programador
        if (dto.getSpecialty() != null) {
            user.setSpecialty(dto.getSpecialty());
        }
        // Actualizar URL de fotografía de perfil o portafolio
        if (dto.getPhotoURL() != null) {
            user.setPhotoURL(dto.getPhotoURL());
        }
        // Sincronizar lista de habilidades técnicas
        if (dto.getSkills() != null) {
            user.setSkills(dto.getSkills());
        }
        // Sincronizar horario de disponibilidad
        if (dto.getSchedule() != null) {
            user.setSchedule(dto.getSchedule());
        }
        // Actualizar estado de disponibilidad general
        if (dto.getAvailable() != null) {
            user.setAvailable(dto.getAvailable());
        }
        // Actualizar enlaces de redes sociales si están presentes
        if (dto.getGithub() != null) {
            user.setGithub(dto.getGithub());
        }
        if (dto.getInstagram() != null) {
            user.setInstagram(dto.getInstagram());
        }
        if (dto.getWhatsapp() != null) {
            user.setWhatsapp(dto.getWhatsapp());
        }
    }

    // Convierte un modelo de usuario a un DTO de respuesta para la API
    public UserResponseDto toResponseDto(User user) {
        // Validar nulidad para evitar errores de serialización
        if (user == null)
            return null;

        UserResponseDto dto = new UserResponseDto();
        dto.setUid(user.getUid());
        dto.setEmail(user.getEmail());
        dto.setDisplayName(user.getDisplayName());

        // Mapear rol de usuario al formato de la respuesta
        if (user.getRole() != null) {
            dto.setRole(com.lexisware.portafolio.users.entities.UserEntity.Role.valueOf(user.getRole().name()));
        }
        dto.setSpecialty(user.getSpecialty());
        dto.setBio(user.getBio());
        dto.setPhotoURL(user.getPhotoURL());
        dto.setAvailable(user.getAvailable());
        dto.setSkills(user.getSkills() != null ? new ArrayList<>(user.getSkills()) : new ArrayList<>());
        dto.setSchedule(user.getSchedule() != null ? new ArrayList<>(user.getSchedule()) : new ArrayList<>());
        dto.setGithub(user.getGithub());
        dto.setInstagram(user.getInstagram());
        dto.setWhatsapp(user.getWhatsapp());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    // Convierte una lista de modelos a una lista de DTOs de respuesta
    public List<UserResponseDto> toResponseDtoList(List<User> users) {
        return users.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // Convierte una lista de entidades JPA a una lista de modelos de negocio
    public List<User> toModelList(List<UserEntity> entities) {
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
