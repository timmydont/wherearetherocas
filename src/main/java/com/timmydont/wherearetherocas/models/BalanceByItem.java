package com.timmydont.wherearetherocas.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceByItem {

    private String item;
    private float income;
    private float outcome;

    public void add(float amount) {
        if (amount > 0) income += amount;
        else outcome += amount;
    }
}
