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
import java.util.List;

// servicio de gestión de asesorías
@Service
@RequiredArgsConstructor
@Slf4j
public class AdvisoryService {

    private final AdvisoryRepository advisoryRepository;
    private final AdvisoryMapper advisoryMapper;
    private final EmailService emailService;

    // obtener todas las asesorías paginadas
    public Page<Advisory> obtenerTodasLasAsesorias(Pageable pageable) {
        return advisoryRepository.findAll(pageable)
                .map(advisoryMapper::toModel);
    }

    // soporte legado - obtener todas las asesorías
    public List<Advisory> obtenerTodasLasAsesorias() {
        return advisoryMapper.toModelList(advisoryRepository.findAll());
    }

    // obtener asesoría por id
    public Advisory obtenerAsesoriaPorId(Long id) {
        AdvisoryEntity entity = advisoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asesoría", "id", id));
        return advisoryMapper.toModel(entity);
    }

    // obtener asesorías de un programador
    public Page<Advisory> obtenerAsesoriasPorProgramador(String programmerId, Pageable pageable) {
        return advisoryRepository.findByProgrammerId(programmerId, pageable)
                .map(advisoryMapper::toModel);
    }

    // obtener asesorías por email del solicitante
    public Page<Advisory> obtenerAsesoriasPorSolicitante(String email, Pageable pageable) {
        return advisoryRepository.findByRequesterEmail(email, pageable)
                .map(advisoryMapper::toModel);
    }

    // obtener asesorías por estado
    public Page<Advisory> obtenerAsesoriasPorEstado(AdvisoryEntity.Status status, Pageable pageable) {
        return advisoryRepository.findByStatus(status, pageable)
                .map(advisoryMapper::toModel);
    }

    // crear solicitud de asesoría
    @Transactional
    public Advisory crearAsesoria(Advisory advisoryModel) {
        // nueva asesoría siempre inicia como pendiente
        advisoryModel.setStatus(Advisory.Status.pending);

        AdvisoryEntity entity = advisoryMapper.toEntity(advisoryModel);
        AdvisoryEntity savedAdvisory = advisoryRepository.save(entity);

        // enviar email al programador
        try {
            emailService.sendAdvisoryNotificationToProgrammer(
                    savedAdvisory.getProgrammerEmail(),
                    savedAdvisory.getProgrammerName(),
                    savedAdvisory.getRequesterName(),
                    savedAdvisory.getDate(),
                    savedAdvisory.getTime(),
                    savedAdvisory.getNote());
        } catch (Exception e) {
            log.error("Error al enviar email al programador: {}", e.getMessage());
        }

        // enviar confirmación al solicitante
        try {
            emailService.sendAdvisoryConfirmationToRequester(
                    savedAdvisory.getRequesterEmail(),
                    savedAdvisory.getRequesterName(),
                    savedAdvisory.getProgrammerName(),
                    savedAdvisory.getDate(),
                    savedAdvisory.getTime());
        } catch (Exception e) {
            log.error("Error al enviar confirmación al solicitante: {}", e.getMessage());
        }

        return advisoryMapper.toModel(savedAdvisory);
    }

    // validar propiedad - solo programador asignado o admin
    private void validarPropiedad(AdvisoryEntity advisory, String appUserUid) {
        // verificar si es admin o moderador
        boolean isAdminOrMod = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MODERATOR"));

        if (isAdminOrMod) {
            return;
        }

        // verificar si es el programador asignado
        if (advisory.getProgrammerId() != null && advisory.getProgrammerId().equals(appUserUid)) {
            return;
        }

        // denegar acceso
        throw new org.springframework.security.access.AccessDeniedException(
                "No tienes permisos para gestionar esta asesoría");
    }

    // actualizar estado de asesoría
    @Transactional
    public Advisory actualizarEstadoAsesoria(Long id, Advisory.Status status, String requestUserUid) {
        AdvisoryEntity entity = advisoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asesoría", "id", id));

        // validar que sea el programador o admin
        validarPropiedad(entity, requestUserUid);

        // convertir estado enum modelo -> entidad
        entity.setStatus(AdvisoryEntity.Status.valueOf(status.name()));

        AdvisoryEntity updatedAdvisory = advisoryRepository.save(entity);

        // notificar al solicitante
        try {
            emailService.sendAdvisoryStatusUpdate(
                    updatedAdvisory.getRequesterEmail(),
                    updatedAdvisory.getRequesterName(),
                    status.name().toLowerCase(),
                    updatedAdvisory.getProgrammerName());
        } catch (Exception e) {
            log.error("Error al enviar notificación de actualización: {}", e.getMessage());
        }

        return advisoryMapper.toModel(updatedAdvisory);
    }

    // aprobar asesoría
    @Transactional
    public Advisory aprobarAsesoria(Long id, String requestUserUid) {
        return actualizarEstadoAsesoria(id, Advisory.Status.approved, requestUserUid);
    }

    // rechazar asesoría
    @Transactional
    public Advisory rechazarAsesoria(Long id, String requestUserUid) {
        return actualizarEstadoAsesoria(id, Advisory.Status.rejected, requestUserUid);
    }

    // eliminar asesoría
    @Transactional
    public void eliminarAsesoria(Long id, String requestUserUid) {
        AdvisoryEntity entity = advisoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asesoría", "id", id));

        // validar propiedad
        validarPropiedad(entity, requestUserUid);

        advisoryRepository.deleteById(id);
    }
}
