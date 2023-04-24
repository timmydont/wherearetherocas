package com.timmydont.wherearetherocas.utils;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataStructureUtils {

    public static <T> List<T> initialize(int size, T value) {
        return value != null ?
                IntStream.range(0, size).mapToObj(i -> value).collect(Collectors.toList()) :
                new ArrayList<>();
    }

    public static Map<String, Object> initialize(ImmutablePair<String, Object>... properties) {
        return Arrays.stream(properties).collect(Collectors.toMap(Pair::getKey, Pair::getValue, (a, b) -> b));
    }
}
