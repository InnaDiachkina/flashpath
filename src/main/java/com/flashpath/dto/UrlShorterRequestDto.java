package com.flashpath.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UrlShorterRequestDto {
    @NotBlank
    private String originalUrl;
}
