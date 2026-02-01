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
            // Verificar si el archivo está vacío para evitar errores de procesamiento
            if (file.isEmpty()) {
                throw new ApplicationException(
                        "El archivo está vacío",
                        HttpStatus.BAD_REQUEST);
            }

            // Validar que el tipo de archivo sea una imagen mediante el Content-Type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new ApplicationException(
                        "El archivo debe ser una imagen",
                        HttpStatus.BAD_REQUEST);
            }

            // Subir el archivo transformado en bytes a Cloudinary
            // Se define la carpeta de destino concatenando el prefijo 'portafolio/'
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "portafolio/" + folder,
                            "resource_type", "image"));

            // Obtener la URL segura de la respuesta de Cloudinary
            String imageUrl = (String) uploadResult.get("secure_url");
            log.info("Imagen subida exitosamente: {}", imageUrl);
            return imageUrl;

        } catch (IOException e) {
            // Registrar el error en logs y lanzar una excepción personalizada
            log.error("Error al subir imagen: {}", e.getMessage());
            throw new ApplicationException(
                    "Error al subir imagen a Cloudinary",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // eliminar imagen de cloudinary
    public void eliminarImagen(String publicId) {
        try {
            // Llamar a la API de Cloudinary para destruir el recurso usando su public_id
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Imagen eliminada: {}", publicId);
        } catch (IOException e) {
            // Errores de red o de API se capturan aquí para no detener el flujo principal
            log.error("Error al eliminar imagen: {}", e.getMessage());
        }
    }

    // extraer public id de una url de cloudinary
    // ejemplo:
    // https://res.cloudinary.com/cloud-name/image/upload/v123/portafolio/profiles/abc123.jpg
    // id público: portafolio/profiles/abc123
    public String extraerIdPublico(String imageUrl) {
        // Validar que la URL no sea nula y pertenezca efectivamente a Cloudinary
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return null;
        }

        try {
            // Dividir la URL tomando como referencia el segmento '/upload/'
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2)
                return null;

            // La parte que nos interesa es lo que está después de '/upload/'
            String afterUpload = parts[1];

            // Eliminar la versión (ejemplo: 'v12345/') que suele aparecer después de upload
            afterUpload = afterUpload.replaceFirst("v\\d+/", "");

            // Retornar la cadena desde el inicio hasta antes del último punto (extensión)
            return afterUpload.substring(0, afterUpload.lastIndexOf('.'));
        } catch (Exception e) {
            // Si el formato de la URL es inesperado, se captura el error y se devuelve null
            log.error("Error al extraer publicId: {}", e.getMessage());
            return null;
        }
    }
}
