package com.lexisware.portafolio.advisory.services;

import com.lexisware.portafolio.utils.EmailService;
import com.lexisware.portafolio.advisory.repositories.AdvisoryRepository;
import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;
import com.lexisware.portafolio.advisory.mappers.AdvisoryMapper;
import com.lexisware.portafolio.advisory.models.Advisory;
import com.lexisware.portafolio.utils.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Servicio para la gestión de la lógica de negocio y flujos de notificación de asesorías
@Service
@RequiredArgsConstructor
@Slf4j
public class AdvisoryService {

    private final AdvisoryRepository advisoryRepository;
    private final AdvisoryMapper advisoryMapper;
    private final EmailService emailService;

    // Obtiene una página con todas las asesorías registradas en el sistema
    public Page<Advisory> obtenerTodasLasAsesorias(Pageable pageable) {
        return advisoryRepository.findAll(pageable)
                .map(advisoryMapper::toModel);
    }

    // Busca una asesoría individual por su identificador único
    public Advisory obtenerAsesoriaPorId(Long id) {
        AdvisoryEntity entity = advisoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asesoría", "id", id));
        return advisoryMapper.toModel(entity);
    }

    // Obtiene todas las asesorías asignadas a un programador específico
    public Page<Advisory> obtenerAsesoriasPorProgramador(String programmerId, Pageable pageable) {
        return advisoryRepository.findByProgrammerId(programmerId, pageable)
                .map(advisoryMapper::toModel);
    }

    // Busca las solicitudes de asesoría realizadas por un correo electrónico
    // informada
    public Page<Advisory> obtenerAsesoriasPorSolicitante(String email, Pageable pageable) {
        return advisoryRepository.findByRequesterEmail(email, pageable)
                .map(advisoryMapper::toModel);
    }

    // Filtra las asesorías según su estado de tramitación
    public Page<Advisory> obtenerAsesoriasPorEstado(AdvisoryEntity.Status status, Pageable pageable) {
        return advisoryRepository.findByStatus(status, pageable)
                .map(advisoryMapper::toModel);
    }

    // Procesa la creación de una nueva asesoría y dispara notificaciones
    // automáticas
    @Transactional
    public Advisory crearAsesoria(Advisory advisoryModel) {
        // Establecer estado inicial predeterminado
        advisoryModel.setStatus(Advisory.Status.pending);

        AdvisoryEntity entity = advisoryMapper.toEntity(advisoryModel);
        AdvisoryEntity savedAdvisory = advisoryRepository.save(entity);

        // Notificar al programador sobre la nueva solicitud recibida
        try {
            emailService.sendAdvisoryNotificationToProgrammer(
                    savedAdvisory.getProgrammerEmail(),
                    savedAdvisory.getProgrammerName(),
                    savedAdvisory.getRequesterName(),
                    savedAdvisory.getDate(),
                    savedAdvisory.getTime(),
                    savedAdvisory.getNote());
        } catch (Exception e) {
            log.error("Error al enviar email informativo al programador: {}", e.getMessage());
        }

        // Enviar confirmación de envío al solicitante
        try {
            emailService.sendAdvisoryConfirmationToRequester(
                    savedAdvisory.getRequesterEmail(),
                    savedAdvisory.getRequesterName(),
                    savedAdvisory.getProgrammerName(),
                    savedAdvisory.getDate(),
                    savedAdvisory.getTime());
        } catch (Exception e) {
            log.error("Error al enviar email de confirmación al solicitante: {}", e.getMessage());
        }

        return advisoryMapper.toModel(savedAdvisory);
    }

    // Valida si el usuario que intenta gestionar la asesoría tiene los permisos
    // necesarios
    private void validarPropiedad(AdvisoryEntity advisory, String appUserUid) {
        boolean isAdmin = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin || (advisory.getProgrammerId() != null && advisory.getProgrammerId().equals(appUserUid))) {
            return;
        }

        throw new org.springframework.security.access.AccessDeniedException(
                "No tienes permisos para gestionar esta asesoría");
    }

    // Actualiza el estado de una asesoría y notifica el cambio al solicitante
    @Transactional
    public Advisory actualizarEstadoAsesoria(Long id, Advisory.Status status, String requestUserUid) {
        AdvisoryEntity entity = advisoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asesoría", "id", id));

        // Verificar permisos de gestión
        validarPropiedad(entity, requestUserUid);

        // Actualizar estado en la entidad de persistencia
        entity.setStatus(AdvisoryEntity.Status.valueOf(status.name()));

        AdvisoryEntity updatedAdvisory = advisoryRepository.save(entity);

        // Notificar al solicitante sobre la resolución de su solicitud
        try {
            emailService.sendAdvisoryStatusUpdate(
                    updatedAdvisory.getRequesterEmail(),
                    updatedAdvisory.getRequesterName(),
                    status.name().toLowerCase(),
                    updatedAdvisory.getProgrammerName());
        } catch (Exception e) {
            log.error("Error al enviar email de actualización de estado: {}", e.getMessage());
        }

        return advisoryMapper.toModel(updatedAdvisory);
    }

    // Aprueba formalmente una solicitud de asesoría
    @Transactional
    public Advisory aprobarAsesoria(Long id, String requestUserUid) {
        return actualizarEstadoAsesoria(id, Advisory.Status.approved, requestUserUid);
    }

    // Rechaza formalmente una solicitud de asesoría
    @Transactional
    public Advisory rechazarAsesoria(Long id, String requestUserUid) {
        return actualizarEstadoAsesoria(id, Advisory.Status.rejected, requestUserUid);
    }

    // Elimina permanentemente el registro de una asesoría
    @Transactional
    public void eliminarAsesoria(Long id, String requestUserUid) {
        AdvisoryEntity entity = advisoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asesoría", "id", id));

        // Validar permisos de eliminación
        validarPropiedad(entity, requestUserUid);

        advisoryRepository.deleteById(id);
    }
}
