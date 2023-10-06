package com.flashpath.controller;

import com.flashpath.dto.UrlShorterRequestDto;
import com.flashpath.dto.UrlShorterResponseDto;
import com.flashpath.service.UrlCacheService;
import com.flashpath.service.UrlShorterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/url")
@RequiredArgsConstructor
public class FlashPathController {
    private final UrlShorterService urlShorterService;
    private final UrlCacheService urlCacheService;

    @PostMapping("/flash")
    public UrlShorterResponseDto getFlashUrl(@RequestBody @Valid UrlShorterRequestDto originalUrl) {
        return urlShorterService.getByOriginalUrl(originalUrl);
    }

    @GetMapping("/original/{flashUrl}")
    public RedirectView findOriginalUrl(@PathVariable String flashUrl) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(urlCacheService.findByFlashUrl(flashUrl));
        return redirectView;
    }
}

