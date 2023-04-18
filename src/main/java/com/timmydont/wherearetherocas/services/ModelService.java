package com.timmydont.wherearetherocas.services;

import com.timmydont.wherearetherocas.lib.model.Model;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.info;

public interface ModelService<T extends Model> {

    Logger logger = Logger.getLogger(ModelService.class);

    boolean save(List<T> items);

    List<T> get(@NonNull Date start, @NonNull Date end);

    List<T> get(@NonNull String account, @NonNull Date start, @NonNull Date end);

    List<T> get(@NonNull String property, @NonNull Object value);

    List<T> get(@NonNull String account, @NonNull String property, @NonNull Object value);

    List<T> all();

    List<T> all(@NonNull String account);

    T withId(@NonNull String id);

    default T first(@NonNull String property, @NonNull Object value) {
        List<T> items = get(property, value);
        if (CollectionUtils.isEmpty(items)) {
            info(logger, "unable to get items from DB for property '%s', value '%s'", property, value);
            return null;
        }
        return items.get(0);
    }

    default T first(@NonNull String account, @NonNull String property, @NonNull Object value) {
        List<T> items = get(account, property, value);
        if (CollectionUtils.isEmpty(items)) {
            info(logger, "unable to get items from DB for property '%s', value '%s', account '%s'", property, value, account);
            return null;
        }
        return items.get(0);
    }
}
