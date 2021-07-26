package com.timmydont.wherearetherocas.models.impl;

import com.timmydont.wherearetherocas.models.Transaction;

public class Debit implements Transaction {

    @Override
    public float getAmount() {
        return 0;
    }
}
