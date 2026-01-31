package com.lexisware.portafolio.files.controllers;

import com.lexisware.portafolio.files.dtos.UploadResponseDto;
import com.lexisware.portafolio.files.services.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "Archivos", description = "Gestión de archivos e imágenes")
public class FileController {

    private final CloudinaryService cloudinaryService;

    // subir imagen de perfil
    @PostMapping("/upload/profile")
    @Operation(summary = "Subir imagen de perfil", description = "Sube una imagen de perfil a Cloudinary")
    public ResponseEntity<UploadResponseDto> subirImagenPerfil(
            @RequestParam("file") MultipartFile file) {
        String imageUrl = cloudinaryService.subirImagen(file, "profiles");
        return ResponseEntity.ok(
                UploadResponseDto.builder()
                        .url(imageUrl)
                        .message("Imagen de perfil subida exitosamente")
                        .build());
    }

    // subir imagen de proyecto
    @PostMapping("/upload/project")
    @Operation(summary = "Subir imagen de proyecto", description = "Sube una imagen de proyecto a Cloudinary")
    public ResponseEntity<UploadResponseDto> subirImagenProyecto(
            @RequestParam("file") MultipartFile file) {
        String imageUrl = cloudinaryService.subirImagen(file, "projects");
        return ResponseEntity.ok(
                UploadResponseDto.builder()
                        .url(imageUrl)
                        .message("Imagen de proyecto subida exitosamente")
                        .build());
    }

    // eliminar imagen
    @DeleteMapping
    @Operation(summary = "Eliminar imagen", description = "Elimina una imagen de Cloudinary usando su publicId")
    public ResponseEntity<Void> eliminarImagen(@RequestParam String publicId) {
        cloudinaryService.eliminarImagen(publicId);
        return ResponseEntity.noContent().build();
    }
}
