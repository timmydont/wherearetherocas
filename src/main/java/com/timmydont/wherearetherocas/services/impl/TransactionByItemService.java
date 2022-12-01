package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.TransactionByItem;
import com.timmydont.wherearetherocas.services.AbstractModelService;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;
import static com.timmydont.wherearetherocas.utils.DateUtils.inRange;
import static com.timmydont.wherearetherocas.utils.DateUtils.toDate;

public class TransactionByItemService extends AbstractModelService<TransactionByItem> {

    private final Logger logger = Logger.getLogger(getClass());

    public TransactionByItemService(DBService dbService) {
        super(dbService, TransactionByItem.class);
    }

    @Override
    public List<TransactionByItem> get(@NonNull Date start, @NonNull Date end) {
        List<TransactionByItem> items = dbService.list(TransactionByItem.class);
        if (CollectionUtils.isEmpty(items)) {
            error(logger, "unable to retrieve transactions by items from db.");
            return null;
        }
        // remove transactions that are not in rage
        items.forEach(item -> {
            item.removeAll(item.getTransactions()
                    .stream()
                    .filter(t -> !inRange(t.getDate(), start, end))
                    .collect(Collectors.toList()));
        });
        return items;
    }
}
