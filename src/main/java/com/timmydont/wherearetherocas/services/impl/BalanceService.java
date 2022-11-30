package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.Balance;
import com.timmydont.wherearetherocas.services.AbstractModelService;

import java.util.Date;
import java.util.List;

public class BalanceService extends AbstractModelService<Balance> {

    public BalanceService(DBService dbService) {
        super(dbService);
    }

    @Override
    public List<Balance> get(Date start, Date end) {
        return null;
    }
}
