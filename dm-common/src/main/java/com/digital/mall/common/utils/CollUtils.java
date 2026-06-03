package com.digital.mall.common.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }
}
