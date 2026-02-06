package com.lexisware.portafolio.users.services;

import com.lexisware.portafolio.users.repositories.UserRepository;
import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.users.mappers.UserMapper;
import com.lexisware.portafolio.users.models.User;
import com.lexisware.portafolio.utils.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

// Servicio para la gestión de usuarios y perfiles de programadores
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> obtenerProgramadores() {
        List<UserEntity> entities = userRepository.findByRole(UserEntity.Role.PROGRAMMER);
        return userMapper.toModelList(entities);
    }

    // Retorna todos los programadores (la lógica de filtros adicionales se aplica
    // en capas superiores)
    @Transactional(readOnly = true)
    public List<User> obtenerProgramadoresDisponibles() {
        List<UserEntity> entities = userRepository.findByRole(UserEntity.Role.PROGRAMMER);
        return userMapper.toModelList(entities);
    }

    // Busca un usuario específico por su identificador único (UID)
    @Transactional(readOnly = true)
    public User obtenerUsuarioPorId(String uid) {
        UserEntity entity = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "uid", uid));
        return userMapper.toModel(entity);
    }

    // Crea un nuevo registro o actualiza uno existente manejando la persistencia de
    // datos sensibles
    @Transactional
    public User crearOActualizarUsuario(User userModel) {
        // Verificar existencia previa para preservar campos que no deben sobrescribirse
        userRepository.findById(userModel.getUid()).ifPresentOrElse(
                existing -> {
                    // Si se proporciona una nueva contraseña, encriptarla
                    if (userModel.getPassword() != null && !userModel.getPassword().isEmpty()) {
                        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
                    } else {
                        // Si no, mantener la existente
                        userModel.setPassword(existing.getPassword());
                    }

                    // Mantener la fecha de creación original
                    if (userModel.getCreatedAt() == null) {
                        userModel.setCreatedAt(existing.getCreatedAt());
                    }
                    userModel.setUpdatedAt(LocalDateTime.now());
                },
                () -> {
                    // Configuración inicial para nuevos registros
                    if (userModel.getPassword() == null || userModel.getPassword().isEmpty()) {
                        userModel.setPassword(passwordEncoder.encode("123456")); // Contraseña temporal por defecto
                                                                                 // encriptada
                    } else {
                        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
                    }
                    userModel.setCreatedAt(LocalDateTime.now());
                });

        UserEntity entity = userMapper.toEntity(userModel);
        UserEntity saved = userRepository.save(entity);
        return userMapper.toModel(saved);
    }

    // Actualización parcial de campos del perfil de usuario
    @Transactional
    public User actualizarUsuario(String uid, User userModelUpdate) {
        // Recuperar entidad actual de la base de datos
        UserEntity existingEntity = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "uid", uid));

        // Convertir a modelo para manipulación segura
        User existingModel = userMapper.toModel(existingEntity);

        // Actualización condicional de campos (solo si se proporcionan nuevos valores)
        if (userModelUpdate.getDisplayName() != null)
            existingModel.setDisplayName(userModelUpdate.getDisplayName());
        if (userModelUpdate.getBio() != null)
            existingModel.setBio(userModelUpdate.getBio());
        if (userModelUpdate.getSpecialty() != null)
            existingModel.setSpecialty(userModelUpdate.getSpecialty());
        if (userModelUpdate.getPhotoURL() != null)
            existingModel.setPhotoURL(userModelUpdate.getPhotoURL());
        if (userModelUpdate.getSkills() != null)
            existingModel.setSkills(userModelUpdate.getSkills());
        if (userModelUpdate.getSchedule() != null)
            existingModel.setSchedule(userModelUpdate.getSchedule());
        if (userModelUpdate.getAvailable() != null)
            existingModel.setAvailable(userModelUpdate.getAvailable());
        if (userModelUpdate.getGithub() != null)
            existingModel.setGithub(userModelUpdate.getGithub());
        if (userModelUpdate.getInstagram() != null)
            existingModel.setInstagram(userModelUpdate.getInstagram());
        if (userModelUpdate.getWhatsapp() != null)
            existingModel.setWhatsapp(userModelUpdate.getWhatsapp());

        // Actualizar marca de tiempo de modificación
        existingModel.setUpdatedAt(LocalDateTime.now());

        // Mapear de vuelta a entidad y persistir cambios
        UserEntity toSave = userMapper.toEntity(existingModel);
        UserEntity saved = userRepository.save(toSave);
        return userMapper.toModel(saved);
    }

    // Cambia el estado de disponibilidad de un programador
    @Transactional
    public User actualizarDisponibilidad(String uid, boolean available) {
        UserEntity entity = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "uid", uid));
        entity.setAvailable(available);
        entity.setUpdatedAt(LocalDateTime.now());
        UserEntity saved = userRepository.save(entity);
        return userMapper.toModel(saved);
    }

    // Elimina permanentemente el registro de un usuario
    @Transactional
    public void eliminarUsuario(String uid) {
        if (!userRepository.existsById(uid)) {
            throw new ResourceNotFoundException("Usuario", "uid", uid);
        }
        userRepository.deleteById(uid);
    }
}
