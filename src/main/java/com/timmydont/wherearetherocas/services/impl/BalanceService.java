package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.Balance;
import com.timmydont.wherearetherocas.services.AbstractModelService;
import com.timmydont.wherearetherocas.utils.DateUtils;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class BalanceService extends AbstractModelService<Balance> {

    private final Logger logger = Logger.getLogger(getClass());

    public BalanceService(DBService dbService) {
        super(dbService, Balance.class);
    }

    @Override
    public List<Balance> get(@NonNull Date start, @NonNull Date end) {
        List<Balance> items = dbService.list(Balance.class);
        if (CollectionUtils.isEmpty(items)) {
            error(logger, "unable to retrieve balances from db.");
            return null;
        }
        List<Balance> toRemove = items.stream()
                .filter(i -> !DateUtils.inRange(i.getStart(), start, end) || !DateUtils.inRange(i.getEnd(), start, end))
                .collect(Collectors.toList());
        if (!items.removeAll(toRemove)) {
            error(logger, "unable to remove filtered balances");
        }
        return items;
    }
}
