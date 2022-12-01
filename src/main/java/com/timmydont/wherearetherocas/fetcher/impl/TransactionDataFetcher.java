package com.timmydont.wherearetherocas.fetcher.impl;

import com.timmydont.wherearetherocas.fetcher.AbstractModelDataFetcher;
import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.Transaction;
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
import static com.timmydont.wherearetherocas.utils.DateUtils.toDate;

public class TransactionDataFetcher extends AbstractModelDataFetcher<Transaction> {

    private final Logger logger = Logger.getLogger(getClass());

    private final DBService dbService;

    private final ModelService<Transaction> transactionService;

    public TransactionDataFetcher(DBService dbService, ModelService<Transaction> transactionService) {
        super(transactionService);
        this.dbService = dbService;
        this.transactionService = transactionService;
    }

    /**
     * Return a list of all transactions that match a given text
     *
     * @return a list of transactions
     */
    public DataFetcher<List<Transaction>> fetchByText() {
        return dataFetchingEnvironment -> {
            // get item
            String text = dataFetchingEnvironment.getArgument("text");
            if (StringUtils.isBlank(text)) {
                error(logger, "invalid parameter, text: %s", text);
                return null;
            }
            // get all transactions
            List<Transaction> items = transactionService.all();
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "no transactions where retrieved, is there no data? or an error, check logs");
                return null;
            }
            return items.stream()
                    .filter(item -> item.getItem().equalsIgnoreCase(text))
                    .collect(Collectors.toList());
        };
    }

    /**
     * @return
     */
    public DataFetcher<List<Transaction>> fetchByItem() {
        return dataFetchingEnvironment -> {
            // get item
            String item = dataFetchingEnvironment.getArgument("item");
            if (StringUtils.isBlank(item)) {
                error(logger, "invalid parameter, item: %s", item);
                return null;
            }
            // get transactions by specific item
            TransactionByItem transactionByItem = dbService.find(item, TransactionByItem.class);
            if (transactionByItem == null) {
                error(logger, "unable to retrieve transaction by item %s", item);
                return null;
            }
            return transactionByItem.getTransactions();
        };
    }

    /**
     * @return
     */
    public DataFetcher<List<Transaction>> fetchByPeriodByItem() {
        return dfe -> {
            // get item start and end date from request
            String item = dfe.getArgument("item");
            Date end = toDate((String) dfe.getArgument("end"));
            Date start = toDate((String) dfe.getArgument("start"));
            if (!ObjectUtils.allNotNull(item, start, end)) {
                error(logger, "invalid parameters, item: %s, start: %s, end: %s", item, dfe.getArgument("start"), dfe.getArgument("end"));
                return null;
            }
            // get a specific transaction by item
            TransactionByItem transactionByItem = dbService.find(item, TransactionByItem.class);
            if (transactionByItem == null) {
                error(logger, "unable to retrieve transaction by item %s", item);
                return null;
            }
            // filter transactions that are not in date range
            return transactionByItem.getTransactions()
                    .stream()
                    .filter(transaction -> inRange(transaction.getDate(), start, end))
                    .collect(Collectors.toList());
        };
    }

    /**
     * @return
     */
    public DataFetcher<List<TransactionByItem>> fetchByPeriodByItems() {
        return dfe -> {
            // get start and end date from request
            Date end = toDate((String) dfe.getArgument("end"));
            Date start = toDate((String) dfe.getArgument("start"));
            if (!ObjectUtils.allNotNull(start, end)) {
                error(logger, "invalid parameters, start: %s, end: %s", dfe.getArgument("start"), dfe.getArgument("end"));
                return null;
            }
            // get all transactions by item
            List<TransactionByItem> items = dbService.list(TransactionByItem.class);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve transactions by period by items.");
                return null;
            }
            // remove transactions that are not in rage
            items.forEach(item -> {
                item.removeAll(item.getTransactions()
                        .stream()
                        .filter(t -> !inRange(t.getDate(), start, end))
                        .collect(Collectors.toList()));
            });
            return items;
        };
    }
}
