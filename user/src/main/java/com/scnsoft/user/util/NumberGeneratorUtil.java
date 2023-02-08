package com.scnsoft.user.util;

import java.security.SecureRandom;

/**
 * Util number generator class, that has one static method for generating random number.
 *
 * @author Maxim Semenko
 * @version 0.0.1
 */
public class NumberGeneratorUtil {

    /**
     * The private constructor, therefore it's util class.
     */
    private NumberGeneratorUtil() {
    }

    /**
     * Method that returns random number from min to max.
     *
     * @param min min number
     * @param max max number
     * @return random number
     */
    public static Integer generateCode(int min, int max) {
        SecureRandom random = new SecureRandom();
        return random.nextInt(max - min + 1) + min;
    }

}
