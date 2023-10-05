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
    private final UrlShorterRepository urlShorterRepository;
    private final FlashUrlGenerator flashUrlGenerator;
    private final UrlCacheService urlCacheService;
    private final UrlShorterMapper urlShorterMapper;

    @Override
    public UrlShorterResponseDto getByOriginalUrl(UrlShorterRequestDto requestDto) {
        String originalUrl = requestDto.getOriginalUrl();
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
            return urlShorterMapper.toDto(urlShorter);
        }
        return urlShorterMapper.toDto(optional.get());
    }

    @Override
    public String createFlashUrl() {
        String flashUrl;
        do {
            flashUrl = flashUrlGenerator.generateFlashUrl();
        } while (urlShorterRepository.existsByFlashUrl(flashUrl));
        return flashUrl;
    }
}
