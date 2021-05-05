package com.ifugle.rap.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 20:17
 */
public class ListUtil {

    /**
     * 将list分为多个list，每个list的最大值为divisor 拆分
     *
     * @param list
     * @param divisor
     * 		每个list的最大值
     * @param <T>
     *
     * @return
     */
    public static <T> List<List<T>> split(List<T> list, int divisor) {
        List<List<T>> result = new ArrayList<>();
        if (list == null) {
            return result;
        }
        int size = list.size();
        if (size <= divisor || divisor <= 0) {
            result.add(list);
            return result;
        }
        int resultSize = size / divisor;
        for (int i = 0; i < resultSize; i++) {
            List<T> ts = list.subList(i * divisor, (i + 1) * divisor);
            result.add(ts);
        }
        int last = size % divisor;
        if (last != 0) {
            result.add(list.subList(resultSize * divisor, size));
        }
        return result;
    }
}
