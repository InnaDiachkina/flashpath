package com.flashpath.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FlashUrlGeneratorTest {
    private static final int LENGTH_FLASH_URL = 7;

    @Test
    @DisplayName("""
            Check generateFlashUrl
            """)
    public void generateFlashUrl_shouldReturnFlashUrl() {
        FlashUrlGenerator flashUrlGenerator = new FlashUrlGenerator(new Random());
        String flashUrl = flashUrlGenerator.generateFlashUrl();
        assertNotNull(flashUrl);
        assertEquals(LENGTH_FLASH_URL, flashUrl.length());
    }
}
