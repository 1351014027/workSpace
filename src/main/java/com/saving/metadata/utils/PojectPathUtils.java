package com.saving.metadata.utils;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;

/**
 * @author: 陈志强
 * @date: 2020/5/6 10:38
 * @Description:
 */
public class PojectPathUtils {
    /**
     * 如果已打成jar包，则返回jar包所在目录
     * 如果未打成jar，则返回target所在目录
     *
     * @return
     */
    public static String getRootPath() {
        ApplicationHome h = new ApplicationHome(PojectPathUtils.class);
        File jarF = h.getSource();
        return jarF.getParentFile().toString();
    }
}
