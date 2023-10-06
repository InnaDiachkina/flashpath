package com.flashpath.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.flashpath.dto.UrlShorterRequestDto;
import com.flashpath.dto.UrlShorterResponseDto;
import com.flashpath.service.UrlCacheService;
import com.flashpath.service.UrlShorterService;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.web.servlet.MvcResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
public class FlashPathControllerTest {
    private static final String ORIGINAL_URL = "http:/originalUrl.test";
    private static final String HTTP_REQUEST_URL = "http://localhost:8080/api/url/flash";
    private static final String HTTP_RESPONSE_URL = "http://localhost:8080/api/url/abcdefj";
    private static final String FLASH_URL = "abcdefj";
    private static final String GET_FLASH_URL = "/api/url/flash";
    private static final String FIND_ORIGINAL_URL = "/api/url/original/{flashUrl}";
    private static final String RESPONSE_JSON_AS_STRING =
            "{\"flashUrl\":\"http://localhost:8080/api/url/abcdefj\"}";
    @Autowired
    protected  MockMvc mockMvc;
    @MockBean
    private UrlShorterService urlShorterService;
    @MockBean
    private UrlCacheService urlCacheService;

    @Test
    @DisplayName("""
            Check getFlashUrl with valid request should return flashUrl
            """)
    public void getFlashUrl_withValidRequest_shouldReturnFlashUrl() throws Exception {
        UrlShorterRequestDto originalUrl = new UrlShorterRequestDto();
        originalUrl.setOriginalUrl(ORIGINAL_URL);
        UrlShorterResponseDto flashUrl = new UrlShorterResponseDto();
        flashUrl.setFlashUrl(HTTP_RESPONSE_URL);
        Mockito.when(urlShorterService.getByOriginalUrl(originalUrl))
                .thenReturn(flashUrl);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(HTTP_REQUEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(originalUrl)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String actual = result.getResponse().getContentAsString();
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
                        .content(new ObjectMapper().writeValueAsString(originalUrl)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("""
            Check findOriginalUrl with valid request should redirect on original URL
            """)
    public void findOriginalUrl_withValidRequest_shouldRedirectOnOriginalUrl() throws Exception {
        Mockito.when(urlCacheService.findByFlashUrl(FLASH_URL)).thenReturn(ORIGINAL_URL);
        mockMvc.perform(MockMvcRequestBuilders.get(FIND_ORIGINAL_URL, FLASH_URL))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(ORIGINAL_URL));
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