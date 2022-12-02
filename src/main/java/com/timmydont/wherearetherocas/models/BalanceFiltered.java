package com.timmydont.wherearetherocas.models;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Date;

@Data
@Builder
public class BalanceFiltered {

    private Date end;
    private Date start;
    private String item;
    private int count; // how many transactions were added to the balance
    private float income;
    private float outcome;
    private float average;

    public void add(float amount, Date date) {
        if (amount > 0) income += amount;
        else outcome += amount;
        end = ObjectUtils.max(end, date);
        start = ObjectUtils.min(start, date);
        average = (income + outcome) / ++count;
    }
}
