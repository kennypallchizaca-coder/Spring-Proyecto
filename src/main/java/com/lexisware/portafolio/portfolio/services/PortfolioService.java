package com.lexisware.portafolio.portfolio.services;

import com.lexisware.portafolio.portfolio.repositories.PortfolioRepository;
import com.lexisware.portafolio.portfolio.entities.PortfolioEntity;
import com.lexisware.portafolio.portfolio.mappers.PortfolioMapper;
import com.lexisware.portafolio.portfolio.models.Portfolio;
import com.lexisware.portafolio.utils.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

// Servicio para la gestión de la lógica de negocio de Portafolios
@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper;

    // Retorna todos los portafolios marcados como públicos para visualización
    // general
    public List<Portfolio> obtenerPortafoliosPublicos() {
        return portfolioRepository.findByIsPublicTrue().stream()
                .map(portfolioMapper::toModel)
                .toList();
    }

    // Busca un portafolio por su ID único de base de datos
    public Portfolio obtenerPortafolioPorId(Long id) {
        PortfolioEntity entity = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portafolio", "id", id));
        return portfolioMapper.toModel(entity);
    }

    // Busca el portafolio asociado a un ID de usuario (UID) específico
    public Portfolio obtenerPortafolioPorUsuario(String uid) {
        PortfolioEntity entity = portfolioRepository.findByUserId(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Portafolio", "usuario", uid));
        return portfolioMapper.toModel(entity);
    }

    // Crea un nuevo registro de portafolio validando que el usuario no posea uno
    // previamente
    @Transactional
    public Portfolio crearPortafolio(Portfolio portfolioModel) {
        // Restricción: Un usuario solo puede tener un portafolio activo
        if (portfolioRepository.existsByUserId(portfolioModel.getUserId())) {
            throw new IllegalStateException("El usuario ya tiene un portafolio");
        }

        PortfolioEntity entity = portfolioMapper.toEntity(portfolioModel);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        PortfolioEntity saved = portfolioRepository.save(entity);
        return portfolioMapper.toModel(saved);
    }

    // Regla de seguridad interna para validar si el usuario tiene permisos de
    // edición
    private void validarPropiedad(PortfolioEntity portfolio, String appUserUid) {
        // Los administradores tienen permiso total de gestión
        boolean isAdmin = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return;
        }

        // El propietario del portafolio tiene permiso de edición
        if (portfolio.getUserId() != null && portfolio.getUserId().equals(appUserUid)) {
            return;
        }

        // Denegar acceso si no es propietario ni administrador
        throw new org.springframework.security.access.AccessDeniedException(
                "No tienes permisos para modificar este portafolio");
    }

    // Actualiza los campos informativos de un portafolio existente
    @Transactional
    public Portfolio actualizarPortafolio(Long id, Portfolio portfolioUpdateModel, String requestUserUid) {
        PortfolioEntity existingEntity = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portafolio", "id", id));

        // Verificar permisos antes de aplicar cambios
        validarPropiedad(existingEntity, requestUserUid);

        // Actualización condicional de campos informativos
        if (portfolioUpdateModel.getTitle() != null)
            existingEntity.setTitle(portfolioUpdateModel.getTitle());
        if (portfolioUpdateModel.getDescription() != null)
            existingEntity.setDescription(portfolioUpdateModel.getDescription());
        if (portfolioUpdateModel.getTheme() != null)
            existingEntity.setTheme(portfolioUpdateModel.getTheme());
        if (portfolioUpdateModel.getIsPublic() != null)
            existingEntity.setIsPublic(portfolioUpdateModel.getIsPublic());

        existingEntity.setUpdatedAt(LocalDateTime.now());

        PortfolioEntity saved = portfolioRepository.save(existingEntity);
        return portfolioMapper.toModel(saved);
    }

    // Elimina el registro de un portafolio de la base de datos
    @Transactional
    public void eliminarPortafolio(Long id, String requestUserUid) {
        PortfolioEntity entity = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portafolio", "id", id));

        // Verificar permisos de eliminación
        validarPropiedad(entity, requestUserUid);

        portfolioRepository.delete(entity);
    }
}
