package com.lexisware.portafolio.portfolio.controllers;

import com.lexisware.portafolio.portfolio.services.PortfolioService;
import com.lexisware.portafolio.portfolio.mappers.PortfolioMapper;
import com.lexisware.portafolio.portfolio.dtos.PortfolioRequestDto;
import com.lexisware.portafolio.portfolio.dtos.PortfolioResponseDto;
import com.lexisware.portafolio.portfolio.models.Portfolio;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// controlador rest para la gestión de portafolios
@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final PortfolioMapper portfolioMapper;

    // obtener todos los portafolios públicos
    @GetMapping("/public")
    public ResponseEntity<List<PortfolioResponseDto>> obtenerPortafoliosPublicos() {
        List<Portfolio> portfolios = portfolioService.obtenerPortafoliosPublicos();
        return ResponseEntity.ok(portfolioMapper.toResponseDtoList(portfolios));
    }

    // obtener portafolio por id
    @GetMapping("/{id}")
    public ResponseEntity<PortfolioResponseDto> obtenerPortafolioPorId(@PathVariable Long id) {
        Portfolio portfolio = portfolioService.obtenerPortafolioPorId(id);
        return ResponseEntity.ok(portfolioMapper.toResponseDto(portfolio));
    }

    // obtener portafolio del usuario actual
    @GetMapping("/me")
    public ResponseEntity<PortfolioResponseDto> obtenerMiPortafolio(@AuthenticationPrincipal String uid) {
        Portfolio portfolio = portfolioService.obtenerPortafolioPorUsuario(uid);
        return ResponseEntity.ok(portfolioMapper.toResponseDto(portfolio));
    }

    // obtener portafolio de un usuario específico
    @GetMapping("/user/{userId}")
    public ResponseEntity<PortfolioResponseDto> obtenerPortafolioPorUsuario(@PathVariable String userId) {
        Portfolio portfolio = portfolioService.obtenerPortafolioPorUsuario(userId);
        return ResponseEntity.ok(portfolioMapper.toResponseDto(portfolio));
    }

    // crear portafolio nuevo
    @PostMapping
    public ResponseEntity<PortfolioResponseDto> crearPortafolio(
            @AuthenticationPrincipal String uid,
            @Valid @RequestBody PortfolioRequestDto request) {
        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            request.setUserId(uid);
        }
        Portfolio portfolioModel = portfolioMapper.toModel(request);
        Portfolio created = portfolioService.crearPortafolio(portfolioModel);
        return new ResponseEntity<>(portfolioMapper.toResponseDto(created), HttpStatus.CREATED);
    }

    // actualizar portafolio
    @PatchMapping("/{id}")
    public ResponseEntity<PortfolioResponseDto> actualizarPortafolio(
            @PathVariable Long id,
            @Valid @RequestBody PortfolioRequestDto request,
            @AuthenticationPrincipal String uid) {

        Portfolio existingModel = portfolioService.obtenerPortafolioPorId(id);
        portfolioMapper.updateModel(existingModel, request);

        Portfolio updated = portfolioService.actualizarPortafolio(id, existingModel, uid);

        return ResponseEntity.ok(portfolioMapper.toResponseDto(updated));
    }

    // eliminar portafolio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPortafolio(
            @PathVariable Long id,
            @AuthenticationPrincipal String uid) {

        portfolioService.eliminarPortafolio(id, uid);
        return ResponseEntity.noContent().build();
    }
}
