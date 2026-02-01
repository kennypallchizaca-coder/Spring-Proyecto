package com.lexisware.portafolio.advisory.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

// DTO encargado de validar la información de una reserva de asesoría
@Data
public class AdvisoryRequestDto {

    @NotBlank(message = "El ID del programador es obligatorio")
    private String programmerId;

    @NotBlank(message = "El email del programador es obligatorio")
    @Email(message = "El email del programador debe ser válido")
    private String programmerEmail;

    @NotBlank(message = "El nombre del programador es obligatorio")
    private String programmerName;

    @NotBlank(message = "El nombre del solicitante es obligatorio")
    private String requesterName;

    @NotBlank(message = "El email del solicitante es obligatorio")
    @Email(message = "El email del solicitante debe ser válido")
    private String requesterEmail;

    @NotBlank(message = "La fecha de la asesoría es obligatoria")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha debe tener formato YYYY-MM-DD")
    private String date;

    @NotBlank(message = "La hora de la asesoría es obligatoria")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "La hora debe tener formato HH:MM")
    private String time;

    @Size(max = 1000, message = "La nota no puede exceder 1000 caracteres")
    private String note;
}
