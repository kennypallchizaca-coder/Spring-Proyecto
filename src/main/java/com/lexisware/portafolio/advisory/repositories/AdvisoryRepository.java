package com.lexisware.portafolio.advisory.repositories;

import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio para gestión de asesorías
@Repository
public interface AdvisoryRepository extends JpaRepository<AdvisoryEntity, Long> {

    // Buscar asesorías por programador
    Page<AdvisoryEntity> findByProgrammerId(String programmerId, Pageable pageable);

    // Buscar asesorías por email del solicitante
    Page<AdvisoryEntity> findByRequesterEmail(String email, Pageable pageable);

    // Buscar asesorías por estado
    Page<AdvisoryEntity> findByStatus(AdvisoryEntity.Status status, Pageable pageable);

    // Buscar asesorías pendientes de un programador
    Page<AdvisoryEntity> findByProgrammerIdAndStatus(String programmerId, AdvisoryEntity.Status status,
            Pageable pageable);
}
