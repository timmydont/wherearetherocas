package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.*;
import graphql.schema.DataFetcher;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class BalanceDataFetcher {

    private final Logger logger = Logger.getLogger(getClass());

    private final DBService dbService;

    public BalanceDataFetcher(DBService dbService) {
        this.dbService = dbService;
    }

    public DataFetcher<BalanceByItem> fetchBalanceByItem() {
        return dataFetchingEnvironment -> {
            String item = dataFetchingEnvironment.getArgument("item");
            TransactionByItem transactionByItem = dbService.find(item, TransactionByItem.class);
            BalanceByItem balanceByItem = BalanceByItem.builder().item(transactionByItem.getItem()).build();
            transactionByItem.getTransactions().forEach(i -> balanceByItem.add(i.getAmount()));
            return balanceByItem;
        };
    }

    public DataFetcher<List<BalanceByPeriod>> fetchBalanceByPeriod() {
        return dataFetchingEnvironment -> {
            Period period = Period.getPeriod(dataFetchingEnvironment.getArgument("period"));
            //
            List<Transaction> items = dbService.list(Transaction.class);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve transactions by items.");
                return null;
            }
            //
            Map<Integer, BalanceByPeriod> balanceMap = new HashMap<>();
            items.forEach(item -> {
                int asCalendar = period.getAsCalendar(item.getDate());
                BalanceByPeriod balance = balanceMap.containsKey(asCalendar) ?
                        balanceMap.get(asCalendar) :
                        BalanceByPeriod.builder()
                                .end(period.getEnd(item.getDate()))
                                .start(period.getStart(item.getDate()))
                                .build();
                balance.add(item.getAmount());
                balanceMap.put(asCalendar, balance);
            });
            return new ArrayList<>(balanceMap.values());
        };
    }
}
