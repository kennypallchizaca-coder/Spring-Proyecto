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

// Servicio para la gestión integral de la lógica de negocio de proyectos
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    // Recupera una página de proyectos convirtiendo las entidades de BD a modelos
    // de respuesta
    public Page<Project> obtenerTodosLosProyectos(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toModel);
    }

    // Retorna la lista completa de proyectos (sin paginación)
    public List<Project> obtenerTodosLosProyectos() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toModel)
                .toList();
    }

    // Busca un proyecto específico; lanza excepción si no existe (404)
    public Project obtenerProyectoPorId(Long id) {
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", id));
        return projectMapper.toModel(entity);
    }

    // Filtra proyectos que pertenecen a un programador específico (identificado por
    // UID)
    public Page<Project> obtenerProyectosPorPropietario(String uid, Pageable pageable) {
        return projectRepository.findByOwner_Uid(uid, pageable)
                .map(projectMapper::toModel);
    }

    // Filtra proyectos por su categoría (ej: WEB, MOBILE, etc.)
    public Page<Project> obtenerProyectosPorCategoria(ProjectEntity.Category category, Pageable pageable) {
        return projectRepository.findByCategory(category, pageable)
                .map(projectMapper::toModel);
    }

    // Filtra proyectos según el rol que desempeñó el autor (ej: FRONTEND, BACKEND)
    public Page<Project> obtenerProyectosPorRol(ProjectEntity.ProjectRole role, Pageable pageable) {
        return projectRepository.findByRole(role, pageable)
                .map(projectMapper::toModel);
    }

    // Crea un nuevo proyecto en la base de datos
    @Transactional
    public Project crearProyecto(Project projectModel) {
        // Convertir el modelo recibido a una entidad JPA para persistencia
        ProjectEntity entity = projectMapper.toEntity(projectModel);

        // Sincronizar el nombre del programador desde el propietario si está disponible
        if (projectModel.getOwner() != null) {
            entity.setProgrammerName(projectModel.getOwner().getDisplayName());
        }

        ProjectEntity saved = projectRepository.save(entity);
        return projectMapper.toModel(saved);
    }

    /**
     * Valida si el usuario actual tiene permiso para modificar o eliminar un
     * proyecto.
     * Permite la acción si:
     * 1. El usuario es administrador (ROLE_ADMIN).
     * 2. El usuario es el dueño legítimo del proyecto.
     */
    private void validarPropiedad(ProjectEntity project, String appUserUid) {
        // Verificar si el usuario tiene el rol de administrador en el contexto de
        // seguridad
        boolean isAdmin = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return; // Acceso total para administradores
        }

        // Verificar si el UID del solicitante coincide con el UID del dueño del
        // proyecto
        if (project.getOwner() != null && project.getOwner().getUid().equals(appUserUid)) {
            return; // Acceso permitido por ser el propietario
        }

        // Si no cumple ninguna, denegar la operación (403 Forbidden)
        throw new org.springframework.security.access.AccessDeniedException(
                "No tienes permisos para modificar este proyecto");
    }

    // Actualiza campos específicos de un proyecto existente
    @Transactional
    public Project actualizarProyecto(Long id, Project projectUpdateModel, String requestUserUid) {
        // Buscar el proyecto actual en la base de datos
        ProjectEntity existingEntity = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", id));

        // Aplicar reglas de seguridad antes de proceder con el cambio
        validarPropiedad(existingEntity, requestUserUid);

        // Actualizar solo los campos que vienen con valor en la petición (Patch
        // parcial)
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

        // Guardar los cambios y retornar el objeto actualizado
        ProjectEntity saved = projectRepository.save(existingEntity);
        return projectMapper.toModel(saved);
    }

    // Elimina un proyecto de forma permanente tras validar permisos
    @Transactional
    public void eliminarProyecto(Long id, String requestUserUid) {
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", id));

        // Asegurar que quien borra es el dueño o un admin
        validarPropiedad(entity, requestUserUid);

        projectRepository.delete(entity);
    }
}
