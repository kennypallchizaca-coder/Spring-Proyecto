package com.lexisware.portafolio.users.repositories;

import com.lexisware.portafolio.users.entities.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// Repositorio para gestión de usuarios
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    // Buscar usuario por email (para login)
    Optional<UserEntity> findByEmail(String email);

    // Buscar usuarios por rol
    List<UserEntity> findByRole(UserEntity.Role role);

    // Buscar programadores disponibles
    List<UserEntity> findByAvailableTrue();

    // Buscar programadores disponibles por rol
    List<UserEntity> findByRoleAndAvailableTrue(UserEntity.Role role);
}
