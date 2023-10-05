package com.flashpath.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.flashpath.exception.EntityNotFoundException;
import com.flashpath.model.UrlCache;
import com.flashpath.repository.UrlCacheRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UrlCacheServiceImplTest {
    private static final String ORIGINAL_URL = "http://originalUrl.test";
    private static final String FLASH_URL = "abcdefj";
    @InjectMocks
    private UrlCacheServiceImpl urlCacheService;
    @Mock
    private UrlCacheRepository urlCacheRepository;

    @Test
    @DisplayName("""
            Check findByFlashUrl when flashUrl is present in the DB
            """)
    public void findByFlashUrl_withValidFlashUrl_shouldReturnOriginalUrl() {
        String expected = ORIGINAL_URL;
        String flashUrl = FLASH_URL;
        UrlCache urlCache = new UrlCache();
        urlCache.setFlashUrl(flashUrl);
        urlCache.setOriginalUrl(expected);
        Mockito.when(urlCacheRepository.findById(flashUrl)).thenReturn(Optional.of(urlCache));
        String actual = urlCacheService.findByFlashUrl(flashUrl);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Check findByFlashUrl when flashUrl isn't present in the DB
            """)
    public void findByFlashUrl_withNonExistingFlashUrl_shouldThrowException() {
        String flashUrl = FLASH_URL;
        Mockito.when(urlCacheRepository.findById(flashUrl)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            urlCacheService.findByFlashUrl(flashUrl);
        });
        String expected = "Cannot find original url by this flash url " + flashUrl;
        String actual = exception.getMessage();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Check findByFlashUrl when flashUrl is null
            """)
    public void findByFlashUrl_withNullFlashUrl_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            urlCacheService.findByFlashUrl(null);
        });
        String expected = "Invalid flashUrl format";
        String actual = exception.getMessage();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Check findByFlashUrl when flashUrl has a wrong format
            """)
    public void findByFlashUrl_withInvalidFormatFlashUrl_shouldThrowException() {
        String flashUrl = "wrongFormatUrl";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            urlCacheService.findByFlashUrl(flashUrl);
        });
        String expected = "Invalid flashUrl format";
        String actual = exception.getMessage();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Check save urlCache
            """)
    public void save_urlCache_shouldReturnUrlCache() {
        String flashUrl = FLASH_URL;
        UrlCache expected = new UrlCache();
        expected.setOriginalUrl(ORIGINAL_URL);
        expected.setFlashUrl(flashUrl);
        Mockito.when(urlCacheRepository.save(expected)).thenReturn(expected);
        UrlCache actual = urlCacheService.save(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}