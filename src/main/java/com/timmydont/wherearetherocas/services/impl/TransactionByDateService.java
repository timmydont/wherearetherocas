package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.TransactionByDate;
import com.timmydont.wherearetherocas.services.AbstractModelService;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

public class TransactionByDateService extends AbstractModelService<TransactionByDate> {

    private final Logger logger = Logger.getLogger(getClass());

    public TransactionByDateService(DBService dbService) {
        super(dbService);
    }

    @Override
    public List<TransactionByDate> get(Date start, Date end) {
        return null;
    }
}
