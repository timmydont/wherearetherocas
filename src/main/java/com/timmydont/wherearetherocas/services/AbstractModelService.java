package com.timmydont.wherearetherocas.services;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.lib.model.Model;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public abstract class AbstractModelService<T extends Model> implements ModelService<T> {

    private final Logger logger = Logger.getLogger(getClass());

    protected final DBService dbService;

    public AbstractModelService(DBService dbService) {
        this.dbService = dbService;
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
            error(logger, "unable to store %s items in db, check previous errors.", items.size());
            return false;
        }
    }
}
