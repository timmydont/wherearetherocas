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

    List<T> get(String account, @NonNull Date start, @NonNull Date end);

    List<T> get(String account, @NonNull String property, @NonNull Object value);

    List<T> all(String account);

    /**
     *
     * @param account
     * @param id
     * @return
     */
    T withId(String account, @NonNull String id);

    default T first(String account, @NonNull String property, @NonNull Object value) {
        List<T> items = get(account, property, value);
        if (CollectionUtils.isEmpty(items)) {
            info(logger, "unable to get items from DB for account '%s', property '%s', value '%s'",
                    account, property, value);
            return null;
        }
        return items.get(0);
    }
}
