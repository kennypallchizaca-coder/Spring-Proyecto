package com.lexisware.portafolio.files.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lexisware.portafolio.utils.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

// servicio para gestión de imágenes en cloudinary
@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // subir imagen a cloudinary
    @SuppressWarnings("unchecked")
    public String subirImagen(MultipartFile file, String folder) {
        try {
            // validar que sea imagen
            if (file.isEmpty()) {
                throw new ApplicationException(
                        "El archivo está vacío",
                        HttpStatus.BAD_REQUEST);
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new ApplicationException(
                        "El archivo debe ser una imagen",
                        HttpStatus.BAD_REQUEST);
            }

            // subir a cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "portafolio/" + folder,
                            "resource_type", "image"));

            String imageUrl = (String) uploadResult.get("secure_url");
            log.info("Imagen subida exitosamente: {}", imageUrl);
            return imageUrl;

        } catch (IOException e) {
            log.error("Error al subir imagen: {}", e.getMessage());
            throw new ApplicationException(
                    "Error al subir imagen a Cloudinary",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // eliminar imagen de cloudinary
    public void eliminarImagen(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Imagen eliminada: {}", publicId);
        } catch (IOException e) {
            log.error("Error al eliminar imagen: {}", e.getMessage());
        }
    }

    // extraer public id de una url de cloudinary
    // ejemplo:
    // https://res.cloudinary.com/cloud-name/image/upload/v123/portafolio/profiles/abc123.jpg
    // id público: portafolio/profiles/abc123
    public String extraerIdPublico(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return null;
        }

        try {
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2)
                return null;

            String afterUpload = parts[1];
            // remover versión (v123456/)
            afterUpload = afterUpload.replaceFirst("v\\d+/", "");
            // remover extensión
            return afterUpload.substring(0, afterUpload.lastIndexOf('.'));
        } catch (Exception e) {
            log.error("Error al extraer publicId: {}", e.getMessage());
            return null;
        }
    }
}
