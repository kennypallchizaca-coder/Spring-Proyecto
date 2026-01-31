package com.lexisware.portafolio.files.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadResponseDto {
    private String url;
    private String message;
}
