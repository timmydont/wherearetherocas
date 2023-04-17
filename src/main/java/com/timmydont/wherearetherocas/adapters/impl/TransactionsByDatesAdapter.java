package com.timmydont.wherearetherocas.adapters.impl;

import com.timmydont.wherearetherocas.adapters.TransactionsAdapter;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.models.TransactionByDate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TransactionsByDatesAdapter implements TransactionsAdapter<TransactionByDate> {

    @Override
    public List<TransactionByDate> adapt(List<Transaction> transactions) {
        float balance = 0f;
        Map<Instant, TransactionByDate> transactionsByDates = new HashMap<>();
        for (Transaction transaction : transactions) {
            Instant instant = transaction.getDate()
                    .toInstant()
                    .truncatedTo(ChronoUnit.DAYS);
            TransactionByDate tbd = transactionsByDates.get(instant);
            if (tbd == null) {
                tbd = TransactionByDate.builder()
                        .id(UUID.randomUUID().toString())
                        .date(transaction.getDate())
                        .account(transaction.getAccount())
                        .income(0f)
                        .outcome(0f)
                        .balance(balance)
                        .build();
            }
            tbd.add(transaction);
            balance += transaction.getAmount();
            transactionsByDates.put(instant, tbd);
        }
        List<TransactionByDate> items = new ArrayList<>(transactionsByDates.values());
        Collections.sort(items);
        return items;
    }
}
