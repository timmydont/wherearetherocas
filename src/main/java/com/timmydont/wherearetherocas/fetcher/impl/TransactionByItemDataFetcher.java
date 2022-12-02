package com.timmydont.wherearetherocas.fetcher.impl;

import com.timmydont.wherearetherocas.fetcher.AbstractModelDataFetcher;
import com.timmydont.wherearetherocas.models.BalanceFiltered;
import com.timmydont.wherearetherocas.models.TransactionByItem;
import com.timmydont.wherearetherocas.services.ModelService;
import graphql.schema.DataFetcher;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;
import static com.timmydont.wherearetherocas.utils.DateUtils.inRange;

public class TransactionByItemDataFetcher extends AbstractModelDataFetcher<TransactionByItem> {

    private final Logger logger = Logger.getLogger(getClass());

    public TransactionByItemDataFetcher(ModelService<TransactionByItem> modelService) {
        super(modelService);
    }

    public DataFetcher<TransactionByItem> fetchByItem() {
        return dataFetchingEnvironment -> {
            String item = getArgument(dataFetchingEnvironment, "item", String.class);
            // check if request argument is valid
            if (StringUtils.isBlank(item)) return null;
            // get transactions by specific item
            TransactionByItem byItem = modelService.withId(item);
            if (byItem == null) {
                error(logger, "unable to retrieve transaction by item '%s'", item);
                return null;
            }
            return byItem;
        };
    }

    public DataFetcher<TransactionByItem> fetchByItemByPeriod() {
        return dataFetchingEnvironment -> {
            // get item start and end date from request
            Date end = getArgument(dataFetchingEnvironment, "end", Date.class);
            Date start = getArgument(dataFetchingEnvironment, "start", Date.class);
            String item = getArgument(dataFetchingEnvironment, "item", String.class);
            if (!ObjectUtils.allNotNull(item, start, end)) return null;
            // get a specific transaction by item
            TransactionByItem transactionByItem = modelService.withId(item);
            if (transactionByItem == null) {
                error(logger, "unable to retrieve transaction by item '%s'", item);
                return null;
            }
            // filter transactions that are not in date range
            transactionByItem.removeAll(transactionByItem.getTransactions()
                    .stream()
                    .filter(t -> !inRange(t.getDate(), start, end))
                    .collect(Collectors.toList()));
            return transactionByItem;
        };
    }

    public DataFetcher<List<TransactionByItem>> fetchByItemsByPeriod() {
        return dataFetchingEnvironment -> {
            // get item start and end date from request
            Date end = getArgument(dataFetchingEnvironment, "end", Date.class);
            Date start = getArgument(dataFetchingEnvironment, "start", Date.class);
            if (!ObjectUtils.allNotNull(start, end)) return null;
            // get all transactions by item
            List<TransactionByItem> items = modelService.all();
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve transactions by items from db.");
                return null;
            }
            // remove transactions that are not in range
            items.forEach(item -> {
                item.removeAll(item.getTransactions()
                        .stream()
                        .filter(t -> !inRange(t.getDate(), start, end))
                        .collect(Collectors.toList()));
            });
            return items;
        };
    }

    public DataFetcher<BalanceFiltered> fetchBalanceByItem() {
        return dataFetchingEnvironment -> {
            String item = getArgument(dataFetchingEnvironment, "item", String.class);
            // check if item argument is valid
            if (!ObjectUtils.allNotNull(item)) return null;
            // get transaction by item
            TransactionByItem byItem = modelService.withId(item);
            if (byItem == null) {
                error(logger, "unable to retrieve transaction by item '%s'", item);
                return null;
            }
            // create balance filtered and populate with data
            BalanceFiltered balance = BalanceFiltered.builder()
                    .item(item)
                    .build();
            byItem.getTransactions()
                    .forEach(i -> balance.add(i.getAmount(), i.getDate()));
            return balance;
        };
    }
}
