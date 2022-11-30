package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.services.ModelService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;
import static com.timmydont.wherearetherocas.utils.DateUtils.inRange;

public class TransactionService implements ModelService<Transaction> {

    private final Logger logger = Logger.getLogger(getClass());

    private final DBService dbService;

    private TransactionService(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public boolean save(List<Transaction> items) {
        if(CollectionUtils.isEmpty(items)) {
            error(logger, "attempting to save an empty list of Transactions.");
            return false;
        }
        Collections.sort(items);
        try {
            dbService.add(items);
            return true;
        } catch (Exception e) {
            error(logger, "unable to store %s transactions in db, check previous errors.", items.size());
            return false;
        }
    }

    @Override
    public List<Transaction> get(Date start, Date end) {
        List<Transaction> items = dbService.list(Transaction.class);
        if(CollectionUtils.isEmpty(items)) {
            error(logger, "unable to retrieve Transaction list from db");
            return null;
        }
        return items.stream().filter(i -> inRange(i.getDate(), start, end)).collect(Collectors.toList());
    }
}
