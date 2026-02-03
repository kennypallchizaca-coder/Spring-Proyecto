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

    // Contar asesorías por estado
    long countByStatus(AdvisoryEntity.Status status);

    // Buscar asesorías por fecha
    java.util.List<AdvisoryEntity> findByDate(String date);

    // Agrupar asesorías por mes (Historial) - Postgres format
    @org.springframework.data.jpa.repository.Query("SELECT new com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto(CONCAT(TO_CHAR(a.createdAt, 'Mon'), ' ', TO_CHAR(a.createdAt, 'YYYY')), COUNT(a)) FROM AdvisoryEntity a GROUP BY TO_CHAR(a.createdAt, 'Mon'), TO_CHAR(a.createdAt, 'YYYY'), EXTRACT(YEAR FROM a.createdAt), EXTRACT(MONTH FROM a.createdAt) ORDER BY EXTRACT(YEAR FROM a.createdAt), EXTRACT(MONTH FROM a.createdAt)")
    java.util.List<com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto> countAdvisoriesByMonth();

    // Agrupar asesorías por programador
    @org.springframework.data.jpa.repository.Query("SELECT new com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto(a.programmerName, COUNT(a)) FROM AdvisoryEntity a GROUP BY a.programmerName ORDER BY COUNT(a) DESC")
    java.util.List<com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto> countAdvisoriesByProgrammer();
}
