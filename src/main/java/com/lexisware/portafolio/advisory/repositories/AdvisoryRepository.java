package com.lexisware.portafolio.advisory.repositories;

import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio para la administración de solicitudes de Asesoría
@Repository
public interface AdvisoryRepository extends JpaRepository<AdvisoryEntity, Long> {

    // Lista las asesorías asignadas a un programador específico
    Page<AdvisoryEntity> findByProgrammerId(String programmerId, Pageable pageable);

    // Recupera asesorías iniciadas por un email de solicitante particular
    Page<AdvisoryEntity> findByRequesterEmail(String email, Pageable pageable);

    // Filtra las solicitudes de asesoría según su estado (pendiente, aprobada,
    // etc.)
    Page<AdvisoryEntity> findByStatus(AdvisoryEntity.Status status, Pageable pageable);
}
