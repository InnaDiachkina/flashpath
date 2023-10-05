package com.flashpath.util;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlashUrlGenerator {
    private static final String ALLOWED_CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int LENGTH_FLASH_URL = 7;
    private static final int LENGTH_ALLOWED_CHARACTERS = 52;
    private final Random random;

    public String generateFlashUrl() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < LENGTH_FLASH_URL; i++) {
            int randomIndex = random.nextInt(LENGTH_ALLOWED_CHARACTERS);
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            builder.append(randomChar);
        }
        return builder.toString();
    }
}
