package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.exceptions.ArgumentException;
import com.timmydont.wherearetherocas.factory.ChartFactory;
import com.timmydont.wherearetherocas.lib.model.Model;
import com.timmydont.wherearetherocas.models.enums.DataType;
import com.timmydont.wherearetherocas.models.enums.Period;
import com.timmydont.wherearetherocas.services.ModelService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;
import static com.timmydont.wherearetherocas.utils.DateUtils.toDate;

public abstract class AbstractModelDataFetcher<T extends Model> implements ModelDataFetcher<T> {

    private final Logger logger = Logger.getLogger(getClass());

    protected final ChartFactory chartFactory;
    protected final ModelService<T> modelService;

    public AbstractModelDataFetcher(ModelService<T> modelService, ChartFactory chartFactory) {
        this.chartFactory = chartFactory;
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
    protected <E extends Object> E getArgument(DataFetchingEnvironment environment, String name, Class<E> clazz) throws ArgumentException {
        Object argument = environment.getArgument(name);
        if (argument == null) {
            error(logger, "request has no argument with name '%s'", name);
            throw new ArgumentException("request has no argument with name '%s'", name);
        }
        // in case clazz provided is Date, attempt to create a Date from the argument
        if (clazz.equals(Date.class)) return (E) toDate(argument.toString());
        // in case clazz provided is Period, attempt to get a valid Period from the argument
        if (clazz.equals(Period.class)) return (E) Period.valueOf(argument.toString());
        // in case clazz provided is DataType, attempt to get a valid DataType from the argument
        if (clazz.equals(DataType.class)) return (E) DataType.valueOf(argument.toString());
        return (E) argument;
    }

    /**
     * Get a map of arguments from the data fetching environment, and try to cast it to a given classes
     *
     * @param environment the graphql environment
     * @param properties  the properties map
     * @return a map of arguments
     */
    protected Map<String, Object> getArgumentsMap(DataFetchingEnvironment environment, ImmutablePair<String, Class>... properties) throws ArgumentException {
        Map<String, Object> argumentsMap = new HashMap<>();
        for (ImmutablePair<String, Class> property : properties) {
            Object value = getArgument(environment, property.getKey(), property.getValue());
            argumentsMap.put(property.getKey(), value);
        }
        return argumentsMap;
    }
}
