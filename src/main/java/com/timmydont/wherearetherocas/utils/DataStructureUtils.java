package com.timmydont.wherearetherocas.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataStructureUtils {

    public static <T> List<T> initialize(int size, T value) {
        return value != null ?
                IntStream.range(0, size).mapToObj(i -> value).collect(Collectors.toList()) :
                new ArrayList<>();
    }
}
