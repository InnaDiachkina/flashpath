package com.flashpath.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;

import com.flashpath.dto.UrlShorterRequestDto;
import com.flashpath.dto.UrlShorterResponseDto;
import com.flashpath.mapper.UrlShorterMapper;
import com.flashpath.model.UrlCache;
import com.flashpath.model.UrlShorter;
import com.flashpath.repository.UrlShorterRepository;
import com.flashpath.service.UrlCacheService;
import com.flashpath.util.FlashUrlGenerator;
import java.util.Optional;
import com.flashpath.util.FlashUrlModifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UrlShorterServiceImplTest {
    private static final String ORIGINAL_URL = "http:/originalUrl.test";
    private static final String FLASH_URL_PREFIX = "http://localhost:8080/api/url/original/";
    private static final String FLASH_URL = "abcdefj";
    @InjectMocks
    private UrlShorterServiceImpl urlShorterService;
    @Mock
    private UrlShorterRepository urlShorterRepository;
    @Mock
    private UrlShorterMapper urlShorterMapper;
    @Mock
    private UrlCacheService urlCacheService;
    @Mock
    private FlashUrlGenerator flashUrlGenerator;
    @Mock
    private FlashUrlModifier flashUrlModifier;

    @Test
    @DisplayName("""
            Check getByOriginalUrl when originalUrl is present in the DB
            """)
    public void getByOriginalUrl_withSavedOriginalUrl_shouldReturnUrlShorterResponseDto() {
        UrlShorterRequestDto originalUrl = new UrlShorterRequestDto();
        originalUrl.setOriginalUrl(ORIGINAL_URL);
        UrlShorterResponseDto expected = new UrlShorterResponseDto();
        expected.setFlashUrl(FLASH_URL_PREFIX + FLASH_URL);
        UrlShorter urlShorter = new UrlShorter();
        urlShorter.setOriginalUrl(ORIGINAL_URL);
        urlShorter.setFlashUrl(FLASH_URL);
        Mockito.when(urlShorterRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(Optional.of(urlShorter));
        Mockito.when(urlShorterMapper.toDto(urlShorter)).thenReturn(expected);
        UrlShorterResponseDto actual = urlShorterService.getByOriginalUrl(originalUrl);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("""
            Check getByOriginalUrl when originalUrl isn't present in the DB
            """)
    public void getByOriginalUrl_withValidOriginalUrl_shouldReturnUrlShorterResponseDto() {
        UrlShorterRequestDto originalUrl = new UrlShorterRequestDto();
        originalUrl.setOriginalUrl(ORIGINAL_URL);
        UrlShorterResponseDto expected = new UrlShorterResponseDto();
        expected.setFlashUrl(FLASH_URL_PREFIX + FLASH_URL);
        UrlShorter urlShorter = new UrlShorter();
        urlShorter.setOriginalUrl(ORIGINAL_URL);
        urlShorter.setFlashUrl(FLASH_URL);
        UrlCache urlCache = new UrlCache();
        urlCache.setFlashUrl(FLASH_URL);
        urlCache.setOriginalUrl(ORIGINAL_URL);
        Mockito.when(urlShorterRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(Optional.empty());
        Mockito.when(flashUrlGenerator.generateFlashUrl()).thenReturn(FLASH_URL);
        Mockito.when(urlShorterRepository.existsByFlashUrl(FLASH_URL)).thenReturn(false);
        Mockito.when(urlShorterRepository.save(urlShorter)).thenReturn(urlShorter);
        Mockito.when(urlCacheService.save(urlCache)).thenReturn(urlCache);
        Mockito.when(urlShorterMapper.toModel(originalUrl)).thenReturn(urlShorter);
        Mockito.when(urlShorterMapper.toDto(urlShorter)).thenReturn(expected);
        UrlShorterResponseDto actual = urlShorterService.getByOriginalUrl(originalUrl);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("""
            Check createFlashUrl
            """)
    public void createFlashUrl_shouldGenerateUniqueUrl() {
        Mockito.when(flashUrlGenerator.generateFlashUrl()).thenReturn("flashUrl1", "flashUrl2", "flashUrl3");
        Mockito.when(urlShorterRepository.existsByFlashUrl("flashUrl1")).thenReturn(false);
        Mockito.when(urlShorterRepository.existsByFlashUrl("flashUrl2")).thenReturn(true);
        Mockito.when(urlShorterRepository.existsByFlashUrl("flashUrl3")).thenReturn(false);
        String url1 = urlShorterService.createFlashUrl();
        String url2 = urlShorterService.createFlashUrl();
        Mockito.verify(flashUrlGenerator, times(3)).generateFlashUrl();
        Mockito.verify(urlShorterRepository).existsByFlashUrl("flashUrl1");
        Mockito.verify(urlShorterRepository).existsByFlashUrl("flashUrl2");
        Mockito.verify(urlShorterRepository).existsByFlashUrl("flashUrl3");
        assertNotNull(url1);
        assertNotNull(url2);
    }
}

