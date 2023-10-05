package com.flashpath.service.impl;

import com.flashpath.exception.EntityNotFoundException;
import com.flashpath.model.UrlCache;
import com.flashpath.repository.UrlCacheRepository;
import com.flashpath.service.UrlCacheService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlCacheServiceImpl implements UrlCacheService {
    private static final Logger logger = LogManager.getLogger(UrlCacheServiceImpl.class);
    private static final int LENGTH_FLASH_URL = 7;
    private final UrlCacheRepository urlCacheRepository;

    @Override
    public UrlCache save(UrlCache urlCache) {
        return urlCacheRepository.save(urlCache);
    }

    @Override
    public String findByFlashUrl(String flashUrl) {
        logger.info("findByFlashUrl was called with url " + flashUrl);
        if (flashUrl == null || flashUrl.length() != LENGTH_FLASH_URL) {
            throw new IllegalArgumentException("Invalid flashUrl format");
        }
        UrlCache urlCache = urlCacheRepository.findById(flashUrl).orElseThrow(() ->
                new EntityNotFoundException("Cannot find original url by this flash url "
                        + flashUrl));
        return urlCache.getOriginalUrl();
    }
}
