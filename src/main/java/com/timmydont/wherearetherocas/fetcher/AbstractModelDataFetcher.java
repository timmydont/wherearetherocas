package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.factory.ChartFactory;
import com.timmydont.wherearetherocas.lib.model.Model;
import com.timmydont.wherearetherocas.services.ModelService;
import graphql.schema.DataFetcher;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

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
}
