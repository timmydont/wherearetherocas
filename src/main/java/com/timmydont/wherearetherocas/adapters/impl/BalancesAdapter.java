package com.timmydont.wherearetherocas.adapters.impl;

import com.timmydont.wherearetherocas.adapters.TransactionsAdapter;
import com.timmydont.wherearetherocas.models.Balance;
import com.timmydont.wherearetherocas.models.Period;
import com.timmydont.wherearetherocas.models.Transaction;

import java.util.*;

public class BalancesAdapter implements TransactionsAdapter<Balance> {

    private final Period period;

    public BalancesAdapter() {
        this.period = Period.Month;
    }

    @Override
    public List<Balance> adapt(List<Transaction> transactions) {
        Map<Integer, Balance> balanceMap = new HashMap<>();
        float current = 0f;
        for (Transaction i : transactions) {
            int asCalendar = period.getAsCalendar(i.getDate());
            Balance balance = balanceMap.containsKey(asCalendar) ?
                    balanceMap.get(asCalendar) :
                    Balance.builder()
                            .id(UUID.randomUUID().toString())
                            .account(i.getAccount())
                            .income(0f)
                            .outcome(0f)
                            .current(current)
                            .period(period)
                            .end(period.getEnd(i.getDate()))
                            .start(period.getStart(i.getDate()))
                            .build();
            current += i.getAmount();
            balance.add(i.getAmount());
            balanceMap.put(asCalendar, balance);
        }
        return new ArrayList<>(balanceMap.values());
    }
}
