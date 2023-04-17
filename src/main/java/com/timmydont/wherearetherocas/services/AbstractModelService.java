package com.timmydont.wherearetherocas.services;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.lib.model.Model;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;
import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.info;

public abstract class AbstractModelService<T extends Model> implements ModelService<T> {

    private final Logger logger = Logger.getLogger(getClass());

    protected final Class<T> clazz;
    protected final DBService dbService;

    public AbstractModelService(DBService dbService, Class<T> clazz) {
        this.clazz = clazz;
        this.dbService = dbService;
    }

    @Override
    public T withId(String account, @NonNull String id) { return dbService.find(id, clazz); }

    @Override
    public List<T> all(String account) {
        return dbService.list(clazz);
    }

    @Override
    public List<T> get(String account, @NonNull String property, @NonNull Object value) {
        List<T> items = dbService.find(property, value, clazz);
        if (CollectionUtils.isEmpty(items)) {
            info(logger, "unable to find items in db, with property: '%s', value: '%s'", property, value);
            return null;
        }
        return items;
    }

    @Override
    public boolean save(List<T> items) {
        if (CollectionUtils.isEmpty(items)) {
            error(logger, "attempting to save an empty list of items.");
            return false;
        }
        try {
            dbService.add(items);
            return true;
        } catch (Exception e) {
            error(logger, "unable to store '%s' items in db, check previous errors.", e, items.size());
            return false;
        }
    }
}
