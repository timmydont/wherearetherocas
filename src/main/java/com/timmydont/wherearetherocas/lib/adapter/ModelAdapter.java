package com.timmydont.wherearetherocas.lib.adapter;

import com.timmydont.wherearetherocas.lib.model.Model;

public interface ModelAdapter<T,E extends Model> {

    T adapt(E e);
}
