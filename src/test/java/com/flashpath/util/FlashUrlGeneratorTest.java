package com.flashpath.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
public class FlashUrlGeneratorTest {
    private static final int LENGTH_FLASH_URL = 7;
    private final FlashUrlGenerator flashUrlGenerator;

    @Autowired
    public FlashUrlGeneratorTest(FlashUrlGenerator flashUrlGenerator) {
        this.flashUrlGenerator = flashUrlGenerator;
    }

    @Test
    @DisplayName("""
            Check generateFlashUrl
            """)
    public void generateFlashUrl_shouldReturnFlashUrl() {
        String flashUrl = flashUrlGenerator.generateFlashUrl();
        assertNotNull(flashUrl);
        assertEquals(LENGTH_FLASH_URL, flashUrl.length());
    }
}
