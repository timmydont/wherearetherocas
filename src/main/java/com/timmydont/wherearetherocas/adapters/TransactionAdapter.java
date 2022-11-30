package com.timmydont.wherearetherocas.adapters;

import com.timmydont.wherearetherocas.models.Transaction;

public interface TransactionAdapter<T> {

    Transaction adapt(T item);

}
