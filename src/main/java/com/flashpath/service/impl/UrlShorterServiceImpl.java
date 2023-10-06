package com.flashpath.service.impl;

import com.flashpath.dto.UrlShorterRequestDto;
import com.flashpath.dto.UrlShorterResponseDto;
import com.flashpath.mapper.UrlShorterMapper;
import com.flashpath.model.UrlCache;
import com.flashpath.model.UrlShorter;
import com.flashpath.repository.UrlShorterRepository;
import com.flashpath.service.UrlCacheService;
import com.flashpath.service.UrlShorterService;
import com.flashpath.util.FlashUrlGenerator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShorterServiceImpl implements UrlShorterService {
    private static final Logger logger = LogManager.getLogger(UrlShorterServiceImpl.class);
    private static final String MIDDLE_PART_RESPONSE_URL = "original/";
    private static final int LENGTH_PART_REQUEST_URL = 5;
    private final UrlShorterRepository urlShorterRepository;
    private final FlashUrlGenerator flashUrlGenerator;
    private final UrlCacheService urlCacheService;
    private final UrlShorterMapper urlShorterMapper;

    @Override
    public UrlShorterResponseDto getByOriginalUrl(UrlShorterRequestDto requestDto,
                                                  String requestUrl) {
        String originalUrl = requestDto.getOriginalUrl();
        String prefixUrl = modifyFlashUrlPrefix(requestUrl);
        logger.info("getByOriginalUrl was called with url " + originalUrl);
        Optional<UrlShorter> optional =
                urlShorterRepository.findByOriginalUrl(originalUrl);
        if (optional.isEmpty()) {
            UrlShorter urlShorter = urlShorterMapper.toModel(requestDto);
            String flashUrl = createFlashUrl();
            urlShorter.setFlashUrl(flashUrl);
            urlShorterRepository.save(urlShorter);
            UrlCache urlCache = new UrlCache();
            urlCache.setFlashUrl(flashUrl);
            urlCache.setOriginalUrl(originalUrl);
            urlCacheService.save(urlCache);
            UrlShorterResponseDto responseDto = urlShorterMapper.toDto(urlShorter);
            responseDto.setFlashUrl(prefixUrl + responseDto.getFlashUrl());
            return responseDto;
        }
        UrlShorterResponseDto responseDto = urlShorterMapper.toDto(optional.get());
        responseDto.setFlashUrl(prefixUrl + responseDto.getFlashUrl());
        return responseDto;
    }

    @Override
    public String createFlashUrl() {
        String flashUrl;
        do {
            flashUrl = flashUrlGenerator.generateFlashUrl();
        } while (urlShorterRepository.existsByFlashUrl(flashUrl));
        return flashUrl;
    }

    @Override
    public String modifyFlashUrlPrefix(String requestUrl) {
        return requestUrl.substring(0, requestUrl.length() - LENGTH_PART_REQUEST_URL)
                + MIDDLE_PART_RESPONSE_URL;
    }
}
