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

// servicio de gestión de portafolios
@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper;

    // Obtiene todos los portafolios públicos
    public List<Portfolio> obtenerPortafoliosPublicos() {
        return portfolioRepository.findByIsPublicTrue().stream()
                .map(portfolioMapper::toModel)
                .toList();
    }

    // Obtiene un portafolio por su ID
    public Portfolio obtenerPortafolioPorId(Long id) {
        PortfolioEntity entity = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portafolio", "id", id));
        return portfolioMapper.toModel(entity);
    }

    // Obtiene un portafolio por el ID de usuario
    public Portfolio obtenerPortafolioPorUsuario(String uid) {
        PortfolioEntity entity = portfolioRepository.findByUserId(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Portafolio", "usuario", uid));
        return portfolioMapper.toModel(entity);
    }

    // Crea un nuevo portafolio
    @Transactional
    public Portfolio crearPortafolio(Portfolio portfolioModel) {
        // verificar si ya existe
        if (portfolioRepository.existsByUserId(portfolioModel.getUserId())) {
            throw new IllegalStateException("El usuario ya tiene un portafolio");
        }

        PortfolioEntity entity = portfolioMapper.toEntity(portfolioModel);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        PortfolioEntity saved = portfolioRepository.save(entity);
        return portfolioMapper.toModel(saved);
    }

    // Valida la propiedad del portafolio
    private void validarPropiedad(PortfolioEntity portfolio, String appUserUid) {
        // verificar si es admin o moderador
        boolean isAdminOrMod = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MODERATOR"));

        if (isAdminOrMod) {
            return;
        }

        // verificar si es el dueño
        if (portfolio.getUserId() != null && portfolio.getUserId().equals(appUserUid)) {
            return;
        }

        // denegar acceso
        throw new org.springframework.security.access.AccessDeniedException(
                "No tienes permisos para modificar este portafolio");
    }

    // Actualiza un portafolio existente
    @Transactional
    public Portfolio actualizarPortafolio(Long id, Portfolio portfolioUpdateModel, String requestUserUid) {
        PortfolioEntity existingEntity = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portafolio", "id", id));

        // verificar permisos
        validarPropiedad(existingEntity, requestUserUid);

        // actualizar campos
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

    // Elimina un portafolio
    @Transactional
    public void eliminarPortafolio(Long id, String requestUserUid) {
        PortfolioEntity entity = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portafolio", "id", id));

        // verificar permisos
        validarPropiedad(entity, requestUserUid);

        portfolioRepository.delete(entity);
    }
}
