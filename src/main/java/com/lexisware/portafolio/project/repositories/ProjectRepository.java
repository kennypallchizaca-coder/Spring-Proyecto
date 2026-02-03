package com.lexisware.portafolio.project.repositories;

import com.lexisware.portafolio.project.entities.ProjectEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio para la gestión de persistencia de Proyectos
@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    // Recupera proyectos paginados pertenecientes a un usuario específico
    Page<ProjectEntity> findByOwner_Uid(String uid, Pageable pageable);

    // Filtra proyectos por su categoría (académico/laboral) de forma paginada
    Page<ProjectEntity> findByCategory(ProjectEntity.Category category, Pageable pageable);

    // Filtra proyectos por el rol desempeñado en el mismo
    Page<ProjectEntity> findByRole(ProjectEntity.ProjectRole role, Pageable pageable);

    // Búsqueda combinada por propietario y categoría del proyecto
    Page<ProjectEntity> findByOwner_UidAndCategory(String uid, ProjectEntity.Category category, Pageable pageable);

    // Estadísticas: Contar proyectos por usuario
    @org.springframework.data.jpa.repository.Query("SELECT new com.lexisware.portafolio.dashboard.dtos.UserProjectCount(p.owner.displayName, COUNT(p)) FROM ProjectEntity p GROUP BY p.owner.displayName")
    java.util.List<com.lexisware.portafolio.dashboard.dtos.UserProjectCount> countProjectsByUser();
}
