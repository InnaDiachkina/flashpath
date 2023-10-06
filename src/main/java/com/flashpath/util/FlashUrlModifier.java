package com.flashpath.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FlashUrlModifier {
    private static final String MIDDLE_PART_RESPONSE_URL = "/api/url/original/";
    private final String apiUrl;

    public FlashUrlModifier(@Value("${myapp.url}") String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String modifyFlashUrl(String flashUrl) {
        return apiUrl + MIDDLE_PART_RESPONSE_URL + flashUrl;
    }
}
