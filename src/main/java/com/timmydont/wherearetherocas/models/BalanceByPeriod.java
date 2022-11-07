package com.timmydont.wherearetherocas.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceByPeriod {

    private String end;
    private String start;
    private float income;
    private float outcome;

    public void add(float amount) {
        if (amount > 0) income += amount;
        else outcome += amount;
    }
}
