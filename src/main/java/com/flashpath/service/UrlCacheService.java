package com.flashpath.service;

import com.flashpath.model.UrlCache;

public interface UrlCacheService {
    UrlCache save(UrlCache urlCache);

    String findByFlashUrl(String flashUrl);
}

