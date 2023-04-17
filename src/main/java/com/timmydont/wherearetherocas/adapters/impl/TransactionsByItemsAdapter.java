package com.timmydont.wherearetherocas.adapters.impl;

import com.timmydont.wherearetherocas.adapters.TransactionsAdapter;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.models.TransactionByItem;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.log4j.Logger;

import java.util.*;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.debug;

public class TransactionsByItemsAdapter implements TransactionsAdapter<TransactionByItem> {

    private final Logger logger = Logger.getLogger(getClass());

    private final JaroWinklerDistance jaroWinklerDistance;

    public TransactionsByItemsAdapter() {
        this.jaroWinklerDistance = new JaroWinklerDistance();
    }

    @Override
    public List<TransactionByItem> adapt(List<Transaction> transactions) {
        Map<String, TransactionByItem> transactionsByItems = new HashMap<>();
        for (Transaction transaction : transactions) {
            boolean added = false;
            for (String key : transactionsByItems.keySet()) {
                Double distance = jaroWinklerDistance.apply(key.toLowerCase(), transaction.getItem().toLowerCase());
                debug(logger, "Distance of %s to %s is %,.2f", transaction.getItem(), key, distance);
                if (distance < 0.2d) {
                    transactionsByItems.get(key).add(transaction);
                    added = true;
                    break;
                }
            }
            if (!added) {
                TransactionByItem item = TransactionByItem.builder()
                        .id(UUID.randomUUID().toString())
                        .item(transaction.getItem())
                        .account(transaction.getAccount())
                        .build();
                item.add(transaction);
                transactionsByItems.put(transaction.getItem().toLowerCase(), item);
            }
        }
        return new ArrayList<>(transactionsByItems.values());
    }
}
