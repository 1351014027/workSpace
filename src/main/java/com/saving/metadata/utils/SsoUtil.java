package com.saving.metadata.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Saving
 */
@Component
public class SsoUtil {


    private static String ticketKeys;


    public SsoUtil() {
    }

    /**
     * 用于单点登陆。单点成功，返回账号；单点失败，返回null
     *
     * @param userId    用户账号
     * @param ticketStr 秘钥
     * @return 单点成功，返回账号；单点失败，返回null
     * @throws Exception
     */
    public static String singleSignOn(String userId, String ticketStr) throws Exception {
        System.out.println(ticketKeys);
        String encryptedCode = getToken(userId, ticketKeys).toLowerCase();
        ticketStr = ticketStr.toLowerCase();
        if (encryptedCode.equals(ticketStr)) {
            return userId;
        }
        return null;
    }

    /**
     * 加密方法
     *
     * @param userId
     * @param ticketKey
     * @return
     * @throws IOException
     */
    public static String getToken(String userId, String ticketKey) throws IOException {
        Date currDate = Calendar.getInstance().getTime();
        DateFormat dateFormatter = new SimpleDateFormat("yyyyMd");
        String dateFormatterStr = dateFormatter.format(currDate);
        DateFormat timeFormatter = new SimpleDateFormat("H:mm");
        String timeFormatterStr = timeFormatter.format(currDate).substring(0, 2);
        //加密格式
        //读配置文件ticket,双方约定密钥
        String code = dateFormatterStr + userId + timeFormatterStr + ticketKey;
        String encryptedCode = getDigestStr(code);
        return encryptedCode;
    }

    public static String getDigestStr(String info) throws IOException {
        try {
            byte[] res = info.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(res);
            for (int i = 0; i < result.length; i++) {
                md.update(result[i]);
            }
            byte[] hash = md.digest();
            StringBuffer d = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                int v = hash[i] & 0xFF;
                if (v < 16) {
                    d.append("0");
                }
                d.append(Integer.toString(v, 16).toUpperCase());
            }
            return d.toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Value("${application.ticketKey}")
    public void setTicketKey(String ticketKey) {
        ticketKeys = ticketKey;
    }
}
