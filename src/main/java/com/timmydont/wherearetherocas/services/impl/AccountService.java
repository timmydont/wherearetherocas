package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.Account;
import com.timmydont.wherearetherocas.services.AbstractModelService;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

public class AccountService extends AbstractModelService<Account> {

    public AccountService(DBService dbService) { super(dbService, Account.class); }

    @Override
    public List<Account> get(@NonNull Date start, @NonNull Date end) {
        return null;
    }
}
