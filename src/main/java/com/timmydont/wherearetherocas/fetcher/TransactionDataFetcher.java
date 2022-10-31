package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.models.TransactionByItem;
import graphql.schema.DataFetcher;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class TransactionDataFetcher {

    private final Logger logger = Logger.getLogger(getClass());

    private final DBService dbService;

    public TransactionDataFetcher(DBService dbService) {
        this.dbService = dbService;
    }

    public DataFetcher<List<Transaction>> fetchAll() {
        return dataFetchingEnvironment -> dbService.list(Transaction.class);
    }

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
}
