package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.services.ModelService;
import graphql.schema.DataFetcher;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class TaggingDataFetcher {

    private final Logger logger = Logger.getLogger(getClass());

    private final ModelService<Transaction> transactionService;

    public TaggingDataFetcher(ModelService<Transaction> transactionService) {
        this.transactionService = transactionService;
    }

    public DataFetcher<List<Transaction>> fetchToTag() {
        return dataFetchingEnvironment -> {
            String account = dataFetchingEnvironment.getArgument("account");
            if (Objects.isNull(account)) {
                error(logger, "request has no argument with name 'account'");
                return Collections.emptyList();
            }
            List<Transaction> transactions = transactionService.all(account);
            if (CollectionUtils.isEmpty(transactions)) {
                error(logger, "there are no transactions for account '%s'", account);
                return Collections.emptyList();
            }
            return transactions.stream().filter(t -> !t.hasTags()).collect(Collectors.toList());
        };
    }
}
