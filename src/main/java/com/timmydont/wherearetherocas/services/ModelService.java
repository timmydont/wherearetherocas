package com.timmydont.wherearetherocas.services;

import com.timmydont.wherearetherocas.lib.model.Model;

import java.util.Date;
import java.util.List;

public interface ModelService<T extends Model> {

    boolean save(List<T> items);

    List<T> get(Date start, Date end);

    /**
     *
     * @return
     */
    List<T> all();
}
