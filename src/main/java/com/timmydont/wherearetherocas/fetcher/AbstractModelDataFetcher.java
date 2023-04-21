package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.lib.model.Model;
import com.timmydont.wherearetherocas.models.Period;
import com.timmydont.wherearetherocas.services.ModelService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

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
            String account = getArgument(dataFetchingEnvironment, "account", String.class);
            List<T> items = modelService.all(account);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve items from db, for account '%s'.", account);
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
            String account = getArgument(dataFetchingEnvironment, "account", String.class);
            if (!ObjectUtils.allNotNull(start, end, account)) return null;
            // get all items of a given period of time
            List<T> items = modelService.get(account, start, end);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "there are no items between dates, start: '%s', end: '%s', for account '%s'", start, end, account);
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
            if (StringUtils.isBlank(id)) return null;
            // get item by id
            T item = modelService.first("id", id);
            if (item == null) {
                error(logger, "unable to find item with id '%s'", id);
                return null;
            }
            return item;
        };
    }

    /**
     * Get a given argument from the data fetching environment, and try to cast it to a given class
     *
     * @param environment the data fetching environment
     * @param name        the property name
     * @param clazz       the class of the value
     * @param <E>         the type of the value
     * @return the value of the argument if exists
     */
    protected <E extends Object> E getArgument(DataFetchingEnvironment environment, String name, Class<E> clazz) {
        Object argument = environment.getArgument(name);
        if (argument == null) {
            error(logger, "request has no argument with name '%s'", name);
            return null;
        }
        // in case clazz provided is Date, attempt to create a Date from the argument
        if (clazz.equals(Date.class)) return (E) toDate(argument.toString());
        // in case clazz provided is Period, attempt to get a valid Period from the argument
        if (clazz.equals(Period.class)) return (E) Period.valueOf(argument.toString());
        return (E) argument;
    }
}
