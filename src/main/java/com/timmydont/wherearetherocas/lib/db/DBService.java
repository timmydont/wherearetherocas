package com.timmydont.wherearetherocas.lib.db;

import com.timmydont.wherearetherocas.lib.model.Model;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;

public interface DBService {

    <T extends Model> void add(T model);

    <T extends Model> void add(Collection<T> model);

    <T extends Model> T find(@NonNull String id, @NonNull Class<T> clazz);

    <T extends Model> List<T> find(@NonNull String property, @NonNull Object value, @NonNull Class<T> clazz);

    <T extends Model> List<T> list(@NonNull Class<T> clazz);
}
