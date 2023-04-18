package com.timmydont.wherearetherocas.lib.model;

import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.Optional;

public interface Model {

    /**
     * Return true if model state is valid, false otherwise
     *
     * @return true if model state is valid, false otherwise
     */
    boolean isValid();

    default boolean contains(@NonNull String property, @NonNull Object value) {
        try {
            Field field = getClass().getDeclaredField(property);
            field.setAccessible(true);
            return Optional.ofNullable(field.get(this)).map(v -> v.equals(value)).orElse(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
