package com.flashpath.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SpringBootTest
public class FlashUrlModifierTest {
    private static final String FLASH_URL = "abcdefj";
    private static final String MODIFIED_FLASH_URL = "http://localhost:8080/api/url/original/abcdefj";
    private final FlashUrlModifier flashUrlModifier;

    @Autowired
    public FlashUrlModifierTest(FlashUrlModifier flashUrlModifier) {
        this.flashUrlModifier = flashUrlModifier;
    }

    @Test
    @DisplayName("""
            Check modifyFlashUrl
            """)
    void modifyFlashUrl_shouldModifiedFlashUrl() {
        String actual = flashUrlModifier.modifyFlashUrl(FLASH_URL);
        assertNotNull(actual);
        assertEquals(MODIFIED_FLASH_URL, actual);
    }
}
