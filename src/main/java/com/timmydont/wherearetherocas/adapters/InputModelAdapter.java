package com.timmydont.wherearetherocas.adapters;

import com.timmydont.wherearetherocas.lib.model.Model;

import java.util.Map;

public interface InputModelAdapter<T extends Model> {

    T adapt(Map<String, Object> input);
}
