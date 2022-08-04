package com.saving.metadata.utils;


import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Saving
 * @date 2019年4月3日14:56:16
 */
@Slf4j
public final class UUID64Utils {

    private static final int FORLENGTH = 15;

    private UUID64Utils() {
    }

    /**
     * 获取现在时间
     *
     * @return 返回字符串格式yyyyMMddHHmmssSSS（17位）
     */
    public static String getStringDate() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * 获取32位UUID字符串
     */
    public static String get32UUIDString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 返回64位业务流水号
     *
     * @return
     */
    public static String get64UUIDString() {
        StringBuilder uuid64 = new StringBuilder(64);
        uuid64.append(getStringDate());
        uuid64.append(get32UUIDString());
        // 生成15位随机数算法
        for (int i = 0; i < FORLENGTH; i++) {
            int number = (int) (Math.random() * 10 + 1);
            uuid64.append(number == 10 ? 9 : number);
        }
        return uuid64.toString();
    }

}
