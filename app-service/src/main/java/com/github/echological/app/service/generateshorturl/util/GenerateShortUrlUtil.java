package com.github.echological.app.service.generateshorturl.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Random;

@UtilityClass
public class GenerateShortUrlUtil {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new SecureRandom();

    // Test seam: allows BDD tests to inject deterministic codes without external libraries
    private static final ThreadLocal<java.util.Queue<String>> TEST_CODES = new ThreadLocal<>();

    /**
     * Test-only: set a queue of codes to be returned sequentially by generateRandomString.
     * Passing null clears the hook.
     */
    public static void __setTestCodes(java.util.Queue<String> codes) {
        if (codes == null || codes.isEmpty()) {
            TEST_CODES.remove();
        } else {
            TEST_CODES.set(codes);
        }
    }

    public static String generateRandomString(int length) {
        var q = TEST_CODES.get();
        if (q != null && !q.isEmpty()) {
            return q.poll();
        }

        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return stringBuilder.toString();
    }

}
