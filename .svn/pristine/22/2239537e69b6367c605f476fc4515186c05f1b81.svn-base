package com.saving.metadata.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Date;

/**
 * @author saving
 */
public class MD5Util {

    private static final Logger log = LoggerFactory.getLogger(MD5Util.class);
    private static final String INVALIDSTRING;

    static {
        INVALIDSTRING = PasswordUtil.WORD[16] + PasswordUtil.WORD[0] + PasswordUtil.WORD[19] + PasswordUtil.WORD[8] + PasswordUtil.WORD[12] + PasswordUtil.WORD[6] + "#@" + "ieireirj" + "121345";
    }

    private MD5Util() {
    }

    public static String getToken(String userId, String key) {
        return encrypt(userId + DateUtil.format(new Date(), "yyyyMMddHH") + key);
    }

    public static String encrypt(String s) {
        try {
            byte[] res = s.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(res);

            for (int i = 0; i < result.length; ++i) {
                md.update(result[i]);
            }

            byte[] hash = md.digest();
            StringBuilder d = new StringBuilder();

            for (int i = 0; i < hash.length; ++i) {
                int v = hash[i] & 255;
                if (v < 16) {
                    d.append("0");
                }

                d.append(Integer.toString(v, 16).toUpperCase());
            }

            return d.toString();
        } catch (Exception var8) {
            log.error("generate md5 error, {}", s, var8);
            return null;
        }
    }

    public static String createMd5(String inStr) {
        if (inStr != null) {
            String fStr = inStr.substring(0, 1);
            String lStr = inStr.substring(1);
            return encrypt(fStr + INVALIDSTRING + lStr);
        } else {
            return encrypt(INVALIDSTRING);
        }
    }
}
