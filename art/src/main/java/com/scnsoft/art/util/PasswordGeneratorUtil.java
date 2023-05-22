package com.scnsoft.art.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class PasswordGeneratorUtil {

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String PUNCTUATION = "!@#$%&*?";
    private static final List<String> charCategories = new ArrayList<>(List.of(LOWER, UPPER, DIGITS, PUNCTUATION));

    private PasswordGeneratorUtil() {
        throw new UnsupportedOperationException("Empty constructor is not supported.");
    }

    public static String generate(int length) {
        if (length <= 0) {
            return "";
        }

        StringBuilder password = new StringBuilder(length);
        Random random = new Random(System.nanoTime());

        for (int i = 0; i < length; i++) {
            String charCategory = charCategories.get(random.nextInt(charCategories.size()));
            int position = random.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));
        }
        return new String(password);
    }
}
