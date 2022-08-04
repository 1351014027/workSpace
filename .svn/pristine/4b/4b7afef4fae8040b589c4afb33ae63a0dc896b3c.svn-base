package com.saving.metadata.utils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author: 陈志强
 * @date: 2019/12/24 19:06
 * @Description:
 */
public class LambdaUtils {

    // 工具方法
    public static <T> Consumer<T> consumerWithIndex(BiConsumer<T, Integer> consumer) {
        class Obj {
            int i;
        }
        Obj obj = new Obj();
        return t -> {
            int index = obj.i++;
            consumer.accept(t, index);
        };
    }
}
