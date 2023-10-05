package com.flashpath.service;

import com.flashpath.dto.UrlShorterRequestDto;
import com.flashpath.dto.UrlShorterResponseDto;

public interface UrlShorterService {
    UrlShorterResponseDto getByOriginalUrl(UrlShorterRequestDto requestDto);

    String createFlashUrl();
}
