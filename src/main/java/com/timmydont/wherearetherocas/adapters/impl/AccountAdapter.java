package com.timmydont.wherearetherocas.adapters.impl;

import com.google.gson.Gson;
import com.timmydont.wherearetherocas.adapters.InputModelAdapter;
import com.timmydont.wherearetherocas.models.Account;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Objects;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class AccountAdapter implements InputModelAdapter<Account> {

    private final Logger logger = Logger.getLogger(getClass());

    private final Gson gson = new Gson();

    @Override
    public Account adapt(Map<String, Object> input) {
        if (Objects.isNull(input)) {
            error(logger, "input is null, impossible to adapt");
            return null;
        }
        return gson.fromJson(input.toString(), Account.class);
    }
}
