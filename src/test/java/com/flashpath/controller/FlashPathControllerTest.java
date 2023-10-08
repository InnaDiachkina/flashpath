package com.flashpath.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashpath.dto.UrlShorterRequestDto;
import com.flashpath.model.UrlCache;
import com.flashpath.model.UrlShorter;
import com.flashpath.repository.UrlCacheRepository;
import com.flashpath.repository.UrlShorterRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlashPathControllerTest {
    private static final String ORIGINAL_URL = "http:/originalUrl.test";
    private static final String HTTP_REQUEST_URL = "http://localhost:8080/api/url/flash";
    private static final String FLASH_URL = "abcdefj";
    private static final String GET_FLASH_URL = "/api/url/flash";
    private static final String FIND_ORIGINAL_URL = "/api/url/original/{flashUrl}";
    private static final String RESPONSE_JSON_AS_STRING =
            "{\"flashUrl\":\"http://localhost:8080/api/url/original/abcdefj\"}";
    private static final int LENGTH_RESPONSE_JSON = 61;
    @Autowired
    protected static MockMvc mockMvc;
    @Autowired
    private UrlShorterRepository urlShorterRepository;
    @Autowired
    private UrlCacheRepository urlCacheRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @AfterEach
    void tearDown() {
        urlShorterRepository.deleteAll();
        urlCacheRepository.deleteAll();
    }

    @Test
    @DisplayName("""
            Check getFlashUrl with valid request should return flashUrl
            """)
    public void getFlashUrl_withValidRequest_shouldReturnFlashUrl() throws Exception {
        UrlShorterRequestDto originalUrl = new UrlShorterRequestDto();
        originalUrl.setOriginalUrl(ORIGINAL_URL);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(HTTP_REQUEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(originalUrl)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String actual = result.getResponse().getContentAsString();
        assertEquals(LENGTH_RESPONSE_JSON, actual.length());
        assertNotNull(actual);
    }

    @Test
    @DisplayName("""
            Check getFlashUrl with valid request should return flashUrl
            """)
    public void getFlashUrl_withSavedFlashUrl_shouldReturnFlashUrl() throws Exception {
        UrlShorter urlShorter = new UrlShorter();
        urlShorter.setFlashUrl(FLASH_URL);
        urlShorter.setOriginalUrl(ORIGINAL_URL);
        urlShorterRepository.save(urlShorter);
        UrlShorterRequestDto originalUrl = new UrlShorterRequestDto();
        originalUrl.setOriginalUrl(ORIGINAL_URL);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(HTTP_REQUEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(originalUrl)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String actual = result.getResponse().getContentAsString();
        assertNotNull(actual);
        assertEquals(RESPONSE_JSON_AS_STRING, actual);
    }

    @Test
    @DisplayName("""
            Check getFlashUrl with wrong request should throw exception
            """)
    public void getFlashUrl_withWrongRequest_shouldThrowException() throws Exception {
        UrlShorterRequestDto originalUrl = new UrlShorterRequestDto();
        mockMvc.perform(MockMvcRequestBuilders.post(GET_FLASH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(originalUrl)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("""
            Check findOriginalUrl with valid request should redirect on original URL
            """)
    public void findOriginalUrl_withValidRequest_shouldRedirectOnOriginalUrl() throws Exception {
        UrlCache urlCache = new UrlCache();
        urlCache.setOriginalUrl(ORIGINAL_URL);
        urlCache.setFlashUrl(FLASH_URL);
        urlCacheRepository.save(urlCache);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(FIND_ORIGINAL_URL, FLASH_URL))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(ORIGINAL_URL))
                .andReturn();
        String redirectedUrl = result.getResponse().getRedirectedUrl();
        assertEquals(ORIGINAL_URL, redirectedUrl);
    }

    @Test
    @DisplayName("""
            Check findOriginalUrl with null request should throw exception
            """)
    public void findOriginalUrl_withWrongRequest_shouldThrowException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(FIND_ORIGINAL_URL, (String) null))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }
}