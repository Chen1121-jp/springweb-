package com.digital.mall.common.utils;

import cn.hutool.core.bean.BeanUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BeanUtils {

    public static <T> T copyBean(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }
        T t;
        try {
            t = target.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        BeanUtil.copyProperties(source, t);
        return t;
    }

    public static <T> List<T> copyList(List<?> sources, Class<T> target) {
        if (sources == null || sources.isEmpty()) {
            return Collections.emptyList();
        }
        return sources.stream()
                .map(s -> copyBean(s, target))
                .collect(Collectors.toList());
    }
}
