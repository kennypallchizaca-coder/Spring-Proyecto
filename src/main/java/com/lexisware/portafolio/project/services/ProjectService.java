package com.lexisware.portafolio.project.services;

import com.lexisware.portafolio.project.repositories.ProjectRepository;
import com.lexisware.portafolio.project.entities.ProjectEntity;
import com.lexisware.portafolio.project.mappers.ProjectMapper;
import com.lexisware.portafolio.project.models.Project;
import com.lexisware.portafolio.utils.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// servicio de gestión de proyectos
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    // obtener todos los proyectos paginados
    public Page<Project> obtenerTodosLosProyectos(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toModel);
    }

    // obtener todos los proyectos - soporte legado
    public List<Project> obtenerTodosLosProyectos() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toModel)
                .toList();
    }

    // obtener proyecto por id
    public Project obtenerProyectoPorId(Long id) {
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", id));
        return projectMapper.toModel(entity);
    }

    // obtener proyectos de un usuario
    public Page<Project> obtenerProyectosPorPropietario(String uid, Pageable pageable) {
        return projectRepository.findByOwner_Uid(uid, pageable)
                .map(projectMapper::toModel);
    }

    // obtener proyectos por categoría
    public Page<Project> obtenerProyectosPorCategoria(ProjectEntity.Category category, Pageable pageable) {
        return projectRepository.findByCategory(category, pageable)
                .map(projectMapper::toModel);
    }

    // obtener proyectos por rol
    public Page<Project> obtenerProyectosPorRol(ProjectEntity.ProjectRole role, Pageable pageable) {
        return projectRepository.findByRole(role, pageable)
                .map(projectMapper::toModel);
    }

    // crear proyecto
    @Transactional
    public Project crearProyecto(Project projectModel) {
        ProjectEntity entity = projectMapper.toEntity(projectModel);
        // asegurar que el nombre del programador se establezca si existe
        if (projectModel.getOwner() != null) {
            entity.setProgrammerName(projectModel.getOwner().getDisplayName());
        }
        ProjectEntity saved = projectRepository.save(entity);
        return projectMapper.toModel(saved);
    }

    // validar propiedad del proyecto
    private void validarPropiedad(ProjectEntity project, String appUserUid) {
        // verificar si es admin o moderador
        boolean isAdminOrMod = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MODERATOR"));

        if (isAdminOrMod) {
            return;
        }

        // verificar si es el dueño
        if (project.getOwner() != null && project.getOwner().getUid().equals(appUserUid)) {
            return;
        }

        // denegar acceso
        throw new org.springframework.security.access.AccessDeniedException(
                "No tienes permisos para modificar este proyecto");
    }

    // actualizar proyecto
    @Transactional
    public Project actualizarProyecto(Long id, Project projectUpdateModel, String requestUserUid) {
        ProjectEntity existingEntity = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", id));

        // validar permisos antes de modificar
        validarPropiedad(existingEntity, requestUserUid);

        // aplicar actualizaciones manualmente
        if (projectUpdateModel.getTitle() != null)
            existingEntity.setTitle(projectUpdateModel.getTitle());
        if (projectUpdateModel.getDescription() != null)
            existingEntity.setDescription(projectUpdateModel.getDescription());
        if (projectUpdateModel.getCategory() != null)
            existingEntity.setCategory(ProjectEntity.Category.valueOf(projectUpdateModel.getCategory().name()));
        if (projectUpdateModel.getRole() != null)
            existingEntity.setRole(ProjectEntity.ProjectRole.valueOf(projectUpdateModel.getRole().name()));
        if (projectUpdateModel.getTechStack() != null)
            existingEntity.setTechStack(projectUpdateModel.getTechStack());
        if (projectUpdateModel.getRepoUrl() != null)
            existingEntity.setRepoUrl(projectUpdateModel.getRepoUrl());
        if (projectUpdateModel.getDemoUrl() != null)
            existingEntity.setDemoUrl(projectUpdateModel.getDemoUrl());
        if (projectUpdateModel.getImageUrl() != null)
            existingEntity.setImageUrl(projectUpdateModel.getImageUrl());

        ProjectEntity saved = projectRepository.save(existingEntity);
        return projectMapper.toModel(saved);
    }

    // eliminar proyecto
    @Transactional
    public void eliminarProyecto(Long id, String requestUserUid) {
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", id));

        // validar permisos antes de eliminar
        validarPropiedad(entity, requestUserUid);

        projectRepository.delete(entity);
    }
}
