package com.lexisware.portafolio.users.services;

import com.lexisware.portafolio.users.repositories.UserRepository;
import com.lexisware.portafolio.users.entities.UserEntity;
import com.lexisware.portafolio.users.mappers.UserMapper;
import com.lexisware.portafolio.users.models.User;
import com.lexisware.portafolio.utils.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

// servicio de gestión de usuarios
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // obtener todos los programadores
    public List<User> obtenerProgramadores() {
        List<UserEntity> entities = userRepository.findByRole(UserEntity.Role.PROGRAMMER);
        return userMapper.toModelList(entities);
    }

    // obtener programadores disponibles (devuelve todos los programadores, la
    // disponibilidad se verifica en el controlador)
    public List<User> obtenerProgramadoresDisponibles() {
        List<UserEntity> entities = userRepository.findByRole(UserEntity.Role.PROGRAMMER);
        return userMapper.toModelList(entities);
    }

    // obtener usuario por id
    public User obtenerUsuarioPorId(String uid) {
        UserEntity entity = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "uid", uid));
        return userMapper.toModel(entity);
    }

    // crear o actualizar usuario
    @Transactional
    public User crearOActualizarUsuario(User userModel) {
        UserEntity entity = userMapper.toEntity(userModel);

        if (userRepository.existsById(entity.getUid())) {
            entity.setUpdatedAt(LocalDateTime.now());
        }

        // mantener fecha de creación si existe
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }

        UserEntity saved = userRepository.save(entity);
        return userMapper.toModel(saved);
    }

    // actualizar perfil de usuario
    @Transactional
    public User actualizarUsuario(String uid, User userModelUpdate) {
        UserEntity existingEntity = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "uid", uid));

        User existingModel = userMapper.toModel(existingEntity);

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

        existingModel.setUpdatedAt(LocalDateTime.now());

        UserEntity toSave = userMapper.toEntity(existingModel);
        UserEntity saved = userRepository.save(toSave);
        return userMapper.toModel(saved);
    }

    // actualizar disponibilidad
    @Transactional
    public User actualizarDisponibilidad(String uid, boolean available) {
        UserEntity entity = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "uid", uid));
        entity.setAvailable(available);
        entity.setUpdatedAt(LocalDateTime.now());
        UserEntity saved = userRepository.save(entity);
        return userMapper.toModel(saved);
    }

    // eliminar usuario
    @Transactional
    public void eliminarUsuario(String uid) {
        if (!userRepository.existsById(uid)) {
            throw new ResourceNotFoundException("Usuario", "uid", uid);
        }
        userRepository.deleteById(uid);
    }
}
