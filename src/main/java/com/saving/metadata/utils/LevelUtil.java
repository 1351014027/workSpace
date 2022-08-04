package com.saving.metadata.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 获取当前层级
 *
 * @author Mr.Saving
 * @date 2019年4月3日14:58:59
 **/
public class LevelUtil {

    public static final String ROOT = "0";
    private static final String SEPARATOR = ".";

    private LevelUtil() {
        super();
    }

    public static String calculateLevel(String parentLevel, Integer parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
