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

// controlador rest para la gestión de asesorías
@RestController
@RequestMapping("/api/advisories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdvisoryController {

    private final AdvisoryService advisoryService;
    private final AdvisoryMapper advisoryMapper;

    // obtener todas las asesorías paginadas - solo admin
    @GetMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerTodasLasAsesorias(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerTodasLasAsesorias(pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // obtener asesoría por id
    @GetMapping("/{id}")
    public ResponseEntity<AdvisoryResponseDto> obtenerAsesoriaPorId(@PathVariable Long id) {
        Advisory advisory = advisoryService.obtenerAsesoriaPorId(id);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(advisory));
    }

    // obtener asesorías del programador actual
    @GetMapping("/my-advisories")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerMisAsesorias(
            @AuthenticationPrincipal String uid,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorProgramador(uid, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // obtener asesorías de un programador específico
    @GetMapping("/programmer/{programmerId}")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerAsesoriasPorProgramador(
            @PathVariable String programmerId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorProgramador(programmerId, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // obtener asesorías por email del solicitante
    @GetMapping("/requester/{email}")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerAsesoriasPorSolicitante(
            @PathVariable String email,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorSolicitante(email, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // obtener asesorías por estado
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AdvisoryResponseDto>> obtenerAsesoriasPorEstado(
            @PathVariable AdvisoryEntity.Status status,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Advisory> advisories = advisoryService.obtenerAsesoriasPorEstado(status, pageable);
        return ResponseEntity.ok(advisories.map(advisoryMapper::toResponseDto));
    }

    // crear solicitud de asesoría
    @PostMapping
    public ResponseEntity<AdvisoryResponseDto> crearAsesoria(@Valid @RequestBody AdvisoryRequestDto request) {
        Advisory advisoryModel = advisoryMapper.toModel(request);
        Advisory created = advisoryService.crearAsesoria(advisoryModel);
        return new ResponseEntity<>(advisoryMapper.toResponseDto(created), HttpStatus.CREATED);
    }

    // actualizar estado de asesoría
    @PatchMapping("/{id}/status")
    public ResponseEntity<AdvisoryResponseDto> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal String uid) {
        String statusStr = body.get("status");
        Advisory.Status status = Advisory.Status.valueOf(statusStr);
        Advisory updated = advisoryService.actualizarEstadoAsesoria(id, status, uid);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(updated));
    }

    // aprobar asesoría
    @PatchMapping("/{id}/approve")
    public ResponseEntity<AdvisoryResponseDto> aprobarAsesoria(
            @PathVariable Long id,
            @AuthenticationPrincipal String uid) {
        Advisory approved = advisoryService.aprobarAsesoria(id, uid);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(approved));
    }

    // rechazar asesoría
    @PatchMapping("/{id}/reject")
    public ResponseEntity<AdvisoryResponseDto> rechazarAsesoria(
            @PathVariable Long id,
            @AuthenticationPrincipal String uid) {
        Advisory rejected = advisoryService.rechazarAsesoria(id, uid);
        return ResponseEntity.ok(advisoryMapper.toResponseDto(rejected));
    }

    // eliminar asesoría
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsesoria(
            @PathVariable Long id,
            @AuthenticationPrincipal String uid) {
        advisoryService.eliminarAsesoria(id, uid);
        return ResponseEntity.noContent().build();
    }
}
