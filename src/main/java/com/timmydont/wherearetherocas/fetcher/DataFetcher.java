package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.exceptions.ArgumentException;
import com.timmydont.wherearetherocas.models.enums.DataType;
import com.timmydont.wherearetherocas.models.enums.Period;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.timmydont.wherearetherocas.utils.DateUtils.toDate;

public interface DataFetcher {

    /**
     * Get a given argument from the data fetching environment, and try to cast it to a given class
     *
     * @param environment the data fetching environment
     * @param name        the property name
     * @param clazz       the class of the value
     * @param <E>         the type of the value
     * @return the value of the argument if exists
     */
    default <E extends Object> E getArgument(DataFetchingEnvironment environment, String name, Class<E> clazz) throws ArgumentException {
        Object argument = environment.getArgument(name);
        if (argument == null) {
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
    default Map<String, Object> getArgumentsMap(DataFetchingEnvironment environment, ImmutablePair<String, Class>... properties) throws ArgumentException {
        Map<String, Object> argumentsMap = new HashMap<>();
        for (ImmutablePair<String, Class> property : properties) {
            Object value = getArgument(environment, property.getKey(), property.getValue());
            argumentsMap.put(property.getKey(), value);
        }
        return argumentsMap;
    }
}
