package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.services.AbstractModelService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;
import static com.timmydont.wherearetherocas.utils.DateUtils.inRange;

public class TransactionService extends AbstractModelService<Transaction> {

    private final Logger logger = Logger.getLogger(getClass());

    public TransactionService(DBService dbService) {
        super(dbService);
    }

    @Override
    public List<Transaction> get(Date start, Date end) {
        List<Transaction> items = dbService.list(Transaction.class);
        if (CollectionUtils.isEmpty(items)) {
            error(logger, "unable to retrieve Transaction list from db");
            return null;
        }
        return items.stream().filter(i -> inRange(i.getDate(), start, end)).collect(Collectors.toList());
    }
}
