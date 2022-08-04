package com.saving.metadata.utils;

import java.util.List;


/**
 * @author: 陈志强
 * @date: 二〇二〇年五月十五日 14:27:52
 * @Description: List转String
 */
public class ListToStringUtil {

    public static String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.substring(0, sb.toString().length() - 1);
    }
}
