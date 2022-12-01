package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.lib.model.Model;
import com.timmydont.wherearetherocas.services.ModelService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;
import static com.timmydont.wherearetherocas.utils.DateUtils.toDate;

public abstract class AbstractModelDataFetcher<T extends Model> implements ModelDataFetcher<T> {

    private final Logger logger = Logger.getLogger(getClass());

    protected final ModelService<T> modelService;

    public AbstractModelDataFetcher(ModelService<T> modelService) {
        this.modelService = modelService;
    }

    @Override
    public DataFetcher<List<T>> fetchAll() {
        return dataFetchingEnvironment -> {
            List<T> items = modelService.all();
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve items from db.");
                return null;
            }
            return items;
        };
    }

    @Override
    public DataFetcher<List<T>> fetchByPeriod() {
        return dataFetchingEnvironment -> {
            // get the start and end dates (the period)
            Date end = getArgument(dataFetchingEnvironment, "end", Date.class);
            Date start = getArgument(dataFetchingEnvironment, "start", Date.class);
            if (!ObjectUtils.allNotNull(start, end)) return null;
            // get all items of a given period of time
            List<T> items = modelService.get(start, end);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "there are no items between dates, start: '%s', end: '%s'", start, end);
                return null;
            }
            return items;
        };
    }

    @Override
    public DataFetcher<T> fetchById() {
        return dataFetchingEnvironment -> {
            String id = getArgument(dataFetchingEnvironment, "id", String.class);
            // check if request argument is valid
            if(StringUtils.isBlank(id)) return null;
            // get item by id
            T item = modelService.first("id", id);
            if(item == null) {
                error(logger, "unable to find item with id '%s'", id);
                return null;
            }
            return item;
        };
    }

    protected <E extends Object> E getArgument(DataFetchingEnvironment environment, String name, Class<E> clazz) {
        Object argument = environment.getArgument(name);
        if(argument == null) {
            error(logger, "request has no argument with name '%s'", name);
            return null;
        }
        // in case clazz provided is Date, attempto to create a Date from the argument
        if(clazz.equals(Date.class)) return (E) toDate(argument.toString());
        return (E) argument;
    }
}
