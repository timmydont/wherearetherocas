package com.timmydont.wherearetherocas.adapters.impl;

import com.timmydont.wherearetherocas.adapters.TransactionsAdapter;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.models.TransactionByItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionsByItemsAdapter implements TransactionsAdapter<TransactionByItem> {

    @Override
    public List<TransactionByItem> adapt(List<Transaction> items) {
        Map<String, TransactionByItem> transactionsByItems = new HashMap<>();
        for(Transaction item : items) {

        }
        return new ArrayList<>(transactionsByItems.values());
    }
}
