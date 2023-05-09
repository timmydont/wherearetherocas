package com.timmydont.wherearetherocas.adapters.impl;

import com.timmydont.wherearetherocas.adapters.TransactionsAdapter;
import com.timmydont.wherearetherocas.models.Balance;
import com.timmydont.wherearetherocas.models.enums.Period;
import com.timmydont.wherearetherocas.models.Transaction;

import java.util.*;

public class BalancesAdapter implements TransactionsAdapter<Balance> {

    @Override
    public List<Balance> adapt(List<Transaction> transactions) {
        return adapt(transactions, Period.Month);
    }

    public List<Balance> adapt(List<Transaction> transactions, Period period) {
        Map<Integer, Balance> balanceMap = new HashMap<>();
        for (Transaction i : transactions) {
            int asCalendar = period.getAsCalendar(i.getDate());
            Balance balance = balanceMap.containsKey(asCalendar) ?
                    balanceMap.get(asCalendar) :
                    Balance.builder()
                            .id(UUID.randomUUID().toString())
                            .account(i.getAccount())
                            .income(0f)
                            .outcome(0f)
                            .period(period)
                            .end(period.getEnd(i.getDate()))
                            .start(period.getStart(i.getDate()))
                            .build();
            balance.add(i);
            balanceMap.put(asCalendar, balance);
        }
        List<Balance> balances = new ArrayList<>(balanceMap.values());
        // sort balances by period
        Collections.sort(balances);
        return balances;
    }
}
