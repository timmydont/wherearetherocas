package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.lib.model.Model;
import com.timmydont.wherearetherocas.services.ModelService;
import graphql.schema.DataFetcher;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
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
        return dfe -> {
            // get the start and end dates (the period)
            Date end = toDate(Optional.ofNullable(dfe.getArgument("end"))
                    .map(i -> i.toString())
                    .orElse(null));
            Date start = toDate(Optional.ofNullable(dfe.getArgument("start"))
                    .map(i -> i.toString())
                    .orElse(null));
            if (!ObjectUtils.allNotNull(start, end)) {
                error(logger, "invalid parameters, start: '%s', end: '%s'",
                        dfe.getArgument("start"), dfe.getArgument("end"));
                return null;
            }
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
        return null;
    }
}
