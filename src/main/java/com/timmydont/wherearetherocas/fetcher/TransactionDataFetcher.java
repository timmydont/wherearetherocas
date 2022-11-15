package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.models.TransactionByItem;
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

public class TransactionDataFetcher {

    private final Logger logger = Logger.getLogger(getClass());

    private final DBService dbService;

    public TransactionDataFetcher(DBService dbService) {
        this.dbService = dbService;
    }

    /**
     * @return
     */
    public DataFetcher<List<Transaction>> fetchAll() {
        return dataFetchingEnvironment -> dbService.list(Transaction.class);
    }

    /**
     * @return
     */
    public DataFetcher<List<TransactionByItem>> fetchByItems() {
        return dataFetchingEnvironment -> {
            List<TransactionByItem> items = dbService.list(TransactionByItem.class);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve transactions by items.");
                return null;
            }
            return items;
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
    public DataFetcher<List<Transaction>> fetchByText() {
        return dataFetchingEnvironment -> {
            // get item
            String text = dataFetchingEnvironment.getArgument("text");
            if (StringUtils.isBlank(text)) {
                error(logger, "invalid parameter, text: %s", text);
                return null;
            }
            // get all transactions
            List<Transaction> items = dbService.list(Transaction.class);
            if (items == null) {
                error(logger, "unable to retrieve all transactions");
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
    public DataFetcher<List<Transaction>> fetchByPeriod() {
        return dfe -> {
            // get the start and end dates (the period)
            Date end = toDate(dfe.getArgument("end"));
            Date start = toDate(dfe.getArgument("start"));
            if (!ObjectUtils.allNotNull(start, end)) {
                error(logger, "invalid parameters, start: %s, end: %s", dfe.getArgument("start"), dfe.getArgument("end"));
                return null;
            }
            // get all transactions
            List<Transaction> items = dbService.list(Transaction.class);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve transactions by items.");
                return null;
            }
            // filter transactions that are not in date range
            return items.stream()
                    .filter(item -> inRange(item.getDate(), start, end))
                    .collect(Collectors.toList());
        };
    }

    /**
     * @return
     */
    public DataFetcher<List<Transaction>> fetchByPeriodByItem() {
        return dfe -> {
            // get item start and end date from request
            String item = dfe.getArgument("item");
            Date end = toDate(dfe.getArgument("end"));
            Date start = toDate(dfe.getArgument("start"));
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
            Date end = toDate(dfe.getArgument("end"));
            Date start = toDate(dfe.getArgument("start"));
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
