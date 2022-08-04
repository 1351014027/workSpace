package com.saving.metadata.utils;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author: 陈志强
 * @date: 2020/1/2 11:10
 * @Description:
 */
public class CreateTableUtils {

    public static Map<String, String> FILESYSTEMS = Maps.newLinkedHashMap();
    public static Map<String, String> FILESTAUTS = Maps.newLinkedHashMap();

    static {
        FILESYSTEMS.put("N", "DECIMAL");
        FILESYSTEMS.put("C", "VARCHAR");
        FILESYSTEMS.put("M", "DECIMAL");
        FILESYSTEMS.put("B", "BINARY");
        FILESYSTEMS.put("T", "TEXT");
        FILESTAUTS.put("O", "NULL");
        FILESTAUTS.put("M", "NOT NULL");
    }

    private CreateTableUtils() {
    }
}
