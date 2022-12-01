package com.timmydont.wherearetherocas.fetcher.impl;

import com.timmydont.wherearetherocas.fetcher.AbstractModelDataFetcher;
import com.timmydont.wherearetherocas.models.TransactionByItem;
import com.timmydont.wherearetherocas.services.ModelService;
import org.apache.log4j.Logger;

public class TransactionByItemDataFetcher extends AbstractModelDataFetcher<TransactionByItem> {

    private final Logger logger = Logger.getLogger(getClass());

    public TransactionByItemDataFetcher(ModelService<TransactionByItem> modelService) {
        super(modelService);
    }
}
