package com.lexisware.portafolio.portfolio.repositories;

import com.lexisware.portafolio.portfolio.entities.PortfolioEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

// Repositorio para gestionar el almacenamiento de Portafolios
@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Long> {

    // Busca un portafolio vinculado a un UID de usuario único
    Optional<PortfolioEntity> findByUserId(String userId);

    // Lista todos los portafolios marcados como públicos para visualización externa
    List<PortfolioEntity> findByIsPublicTrue();

    // Verifica si un usuario ya posee un portafolio registrado
    boolean existsByUserId(String userId);
}
