package com.saving.metadata.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * List<map<String,String>> to  map<String,String>
 *
 * @author: Mr.Saving
 * @date: 2019年4月3日14:57:36
 **/
public class MapUtil {


    private MapUtil() {
    }

    /**
     * @param list
     * @return map<String, String>
     * @Author Mr.Saving
     * @Description List<map < String, String>> to  map<String,String>
     * @Date 2019/4/3 14:58
     * @Param List<map < String, String>>
     */
    public static Map<Object, Object> listMapTransformMap(List<Map<String, Object>> list) {
        Map<Object, Object> maps = new LinkedHashMap<>();
        String key = "";
        String value = "";
        int num = 0;
        for (Map<String, Object> map : list) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (num == 2) {
                    break;
                }
                if (num == 0) {
                    key = entry.getKey();
                }
                if (num == 1) {
                    value = entry.getKey();
                }
                num++;
            }
            if (!list.isEmpty() && !(key.toLowerCase().contains("ID".toLowerCase()))) {
                String ls = key;
                key = value;
                value = ls;
            }
            maps.put(map.get(key), map.get(value));
        }
        return maps;
    }
}
