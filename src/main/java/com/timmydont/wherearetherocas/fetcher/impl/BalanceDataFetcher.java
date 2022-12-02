package com.timmydont.wherearetherocas.fetcher.impl;

import com.timmydont.wherearetherocas.fetcher.AbstractModelDataFetcher;
import com.timmydont.wherearetherocas.models.Balance;
import com.timmydont.wherearetherocas.services.ModelService;
import org.apache.log4j.Logger;

public class BalanceDataFetcher extends AbstractModelDataFetcher<Balance> {

    private final Logger logger = Logger.getLogger(getClass());

    public BalanceDataFetcher(ModelService<Balance> modelService) {
        super(modelService);
    }
}
