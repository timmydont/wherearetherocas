package com.timmydont.wherearetherocas.factory.impl;

import com.timmydont.wherearetherocas.factory.ModelServiceFactory;
import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.lib.model.Model;
import com.timmydont.wherearetherocas.models.Balance;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.models.TransactionByDate;
import com.timmydont.wherearetherocas.models.TransactionByItem;
import com.timmydont.wherearetherocas.services.ModelService;
import com.timmydont.wherearetherocas.services.impl.BalanceService;
import com.timmydont.wherearetherocas.services.impl.TransactionByDateService;
import com.timmydont.wherearetherocas.services.impl.TransactionByItemService;
import com.timmydont.wherearetherocas.services.impl.TransactionService;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ModelServiceFactoryImpl<T extends Model> implements ModelServiceFactory {

    private Map<Class, ModelService> serviceMap;

    public ModelServiceFactoryImpl(DBService dbService) {
        this.serviceMap = new HashMap<>();
        this.serviceMap.put(Balance.class, new BalanceService(dbService));
        this.serviceMap.put(Transaction.class, new TransactionService(dbService));
        this.serviceMap.put(TransactionByItem.class, new TransactionByItemService(dbService));
        this.serviceMap.put(TransactionByDate.class, new TransactionByDateService(dbService));
    }

    @Override
    public <T extends Model> ModelService<T> getService(@NonNull Class<T> clazz) {
        return serviceMap.get(clazz);
    }
}