package com.lexisware.portafolio.advisory.controllers;

import com.lexisware.portafolio.advisory.services.AdvisoryService;
import com.lexisware.portafolio.advisory.mappers.AdvisoryMapper;
import com.lexisware.portafolio.advisory.dtos.AdvisoryRequestDto;
import com.lexisware.portafolio.advisory.dtos.AdvisoryResponseDto;
import com.lexisware.portafolio.advisory.models.Advisory;
import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

// Controlador REST para la gestión de solicitudes de asesoría entre programadores y solicitantes
@RestController
@RequestMapping("/api/advisories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdvisoryController {

    private final AdvisoryService advisoryService;
    private final AdvisoryMapper advisoryMapper;

    // Obtiene una página con todas las asesorías registradas (Solo accesible por
    // Administradores)
    @GetMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerTodasLasAsesorias(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerTodasLasAsesorias(pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Retorna la información detallada de una asesoría específica por su ID
    @GetMapping("/{id}")
    public ResponseEntity<AdvisoryResponseDto> obtenerAsesoriaPorId(@PathVariable("id") Long id) {
        Advisory advisory = advisoryService.obtenerAsesoriaPorId(id);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(advisory));
    }

    // Lista las asesorías asignadas al programador autenticado actualmente
    @GetMapping("/my-advisories")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerMisAsesorias(
            @AuthenticationPrincipal String uid,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorProgramador(uid, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Lista todas las asesorías asignadas a un programador específico
    @GetMapping("/programmer/{programmerId}")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerAsesoriasPorProgramador(
            @PathVariable("programmerId") String programmerId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorProgramador(programmerId, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Lista las asesorías solicitadas por un email de solicitante específico
    @GetMapping("/requester/{email}")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerAsesoriasPorSolicitante(
            @PathVariable("email") String email,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorSolicitante(email, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Filtra las asesorías según su estado actual (pending, approved, rejected)
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerAsesoriasPorEstado(
            @PathVariable("status") AdvisoryEntity.Status status,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorEstado(status, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // Registra una nueva solicitud de asesoría y notifica a las partes involucradas
    @PostMapping
    public ResponseEntity<AdvisoryResponseDto> crearAsesoria(@Valid @RequestBody AdvisoryRequestDto request) {
        Advisory advisoryModel = advisoryMapper.toModel(request);
        Advisory created = advisoryService.crearAsesoria(advisoryModel);
        return new ResponseEntity<>(advisoryMapper.toResponseDto(created), HttpStatus.CREATED);
    }

    // Cambia manualmente el estado de una asesoría (Requiere ser el programador
    // asignado o Admin)
    @PatchMapping("/{id}/status")
    public ResponseEntity<AdvisoryResponseDto> actualizarEstado(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal String uid) {
        String statusStr = body.get("status");
        Advisory.Status status = Advisory.Status.valueOf(statusStr);
        Advisory updated = advisoryService.actualizarEstadoAsesoria(id, status, uid);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(updated));
    }

    // Marca una asesoría como aprobada y notifica al solicitante
    @PatchMapping("/{id}/approve")
    public ResponseEntity<AdvisoryResponseDto> aprobarAsesoria(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String uid) {
        Advisory approved = advisoryService.aprobarAsesoria(id, uid);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(approved));
    }

    // Marca una asesoría como rechazada y notifica al solicitante
    @PatchMapping("/{id}/reject")
    public ResponseEntity<AdvisoryResponseDto> rechazarAsesoria(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String uid) {
        Advisory rejected = advisoryService.rechazarAsesoria(id, uid);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(rejected));
    }

    // Elimina el registro de una asesoría del sistema
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsesoria(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal String uid) {
        advisoryService.eliminarAsesoria(id, uid);
        return ResponseEntity.noContent().build();
    }
}
