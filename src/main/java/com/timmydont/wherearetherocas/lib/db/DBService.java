package com.timmydont.wherearetherocas.lib.db;

import com.timmydont.wherearetherocas.lib.model.Model;

import java.util.Collection;
import java.util.List;

public interface DBService {

    <T extends Model> void add(T model);

    <T extends Model> void add(Collection<T> model);

    <T extends Model> T find(String id, Class<T> clazz);

    <T extends Model> List<T> list(Class<T> clazz);
}
