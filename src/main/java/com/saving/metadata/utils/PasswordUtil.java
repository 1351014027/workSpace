package com.saving.metadata.utils;

import java.util.Random;

/**
 * @author Administrator
 */
public class PasswordUtil {
    public static final String[] WORD = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public static final String[] NUM = new String[]{"2", "3", "4", "5", "6", "7", "8", "9"};

    private PasswordUtil() {
    }

    public static String randomPassword() {
        StringBuilder stringBuffer = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        boolean flag = false;
        int length = random.nextInt(3) + 8;

        for (int i = 0; i < length; ++i) {
            if (flag) {
                stringBuffer.append(NUM[random.nextInt(NUM.length)]);
            } else {
                stringBuffer.append(WORD[random.nextInt(WORD.length)]);
            }

            flag = !flag;
        }

        return stringBuffer.toString();
    }
}