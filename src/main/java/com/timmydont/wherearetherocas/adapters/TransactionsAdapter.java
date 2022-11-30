package com.timmydont.wherearetherocas.adapters;

import com.timmydont.wherearetherocas.lib.model.Model;
import com.timmydont.wherearetherocas.models.Transaction;

import java.util.Collection;
import java.util.List;

public interface TransactionsAdapter<T extends Model> {

    List<T> adapt(List<Transaction> items);
}
