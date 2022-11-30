package com.timmydont.wherearetherocas.factory;

import com.timmydont.wherearetherocas.lib.model.Model;
import com.timmydont.wherearetherocas.services.ModelService;

public interface ModelServiceFactory {

    <T extends Model> ModelService<T> getService(Class<T> clazz);
}
