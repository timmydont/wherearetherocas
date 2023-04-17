package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.TransactionByDate;
import com.timmydont.wherearetherocas.services.AbstractModelService;
import com.timmydont.wherearetherocas.utils.DateUtils;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class TransactionByDateService extends AbstractModelService<TransactionByDate> {

    private final Logger logger = Logger.getLogger(getClass());

    public TransactionByDateService(DBService dbService) {
        super(dbService, TransactionByDate.class);
    }

    @Override
    public List<TransactionByDate> get(@NonNull Date start, @NonNull Date end) {
        List<TransactionByDate> items = dbService.list(TransactionByDate.class);
        if (CollectionUtils.isEmpty(items)) {
            error(logger, "unable to retrieve transactions by dates from db.");
            return null;
        }
        List<TransactionByDate> remove = items.stream()
                .filter(i -> !DateUtils.inRange(i.getDate(), start, end))
                .collect(Collectors.toList());
        if (items.removeAll(remove)) {
            error(logger, "something failed while removing filtered transaction by date.");
        }
        return items;
    }
}
