package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.TransactionByItem;
import com.timmydont.wherearetherocas.services.AbstractModelService;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

public class TransactionByItemService extends AbstractModelService<TransactionByItem> {

    private final Logger logger = Logger.getLogger(getClass());

    public TransactionByItemService(DBService dbService) {
        super(dbService, TransactionByItem.class);
    }

    @Override
    public List<TransactionByItem> get(Date start, Date end) {
        return null;
    }
}
