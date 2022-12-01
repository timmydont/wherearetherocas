package com.timmydont.wherearetherocas.lib.db;

import com.timmydont.wherearetherocas.lib.model.Model;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;

public interface DBService {

    <T extends Model> void add(T model);

    <T extends Model> void add(Collection<T> model);

    <T extends Model> T find(@NonNull String id, Class<T> clazz);

    <T extends Model> List<T> find(@NonNull String property, @NonNull Object value, Class<T> clazz);

    <T extends Model> List<T> list(Class<T> clazz);
}
