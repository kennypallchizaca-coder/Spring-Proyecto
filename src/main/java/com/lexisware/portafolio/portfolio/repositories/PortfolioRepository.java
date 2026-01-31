package com.lexisware.portafolio.portfolio.repositories;

import com.lexisware.portafolio.portfolio.entities.PortfolioEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

// Repositorio para gestión de portafolios
@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Long> {

    // Buscar portafolio por usuario
    Optional<PortfolioEntity> findByUserId(String userId);

    // Buscar portafolios públicos
    List<PortfolioEntity> findByIsPublicTrue();

    // Verificar si existe portafolio para un usuario
    boolean existsByUserId(String userId);
}
