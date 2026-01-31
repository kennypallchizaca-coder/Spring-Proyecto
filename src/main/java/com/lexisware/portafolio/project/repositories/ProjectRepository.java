package com.lexisware.portafolio.project.repositories;

import com.lexisware.portafolio.project.entities.ProjectEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio para gestión de proyectos
@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    // Buscar proyectos por dueño (usando UID del User)
    Page<ProjectEntity> findByOwner_Uid(String uid, Pageable pageable);

    // Buscar proyectos por categoría
    Page<ProjectEntity> findByCategory(ProjectEntity.Category category, Pageable pageable);

    // Buscar proyectos por rol
    Page<ProjectEntity> findByRole(ProjectEntity.ProjectRole role, Pageable pageable);

    // Buscar proyectos por dueño y categoría
    Page<ProjectEntity> findByOwner_UidAndCategory(String uid, ProjectEntity.Category category, Pageable pageable);
}
