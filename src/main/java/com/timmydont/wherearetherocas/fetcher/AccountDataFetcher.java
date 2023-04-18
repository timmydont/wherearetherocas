package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.adapters.InputModelAdapter;
import com.timmydont.wherearetherocas.adapters.impl.AccountAdapter;
import com.timmydont.wherearetherocas.models.Account;
import com.timmydont.wherearetherocas.services.ModelService;
import graphql.schema.DataFetcher;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class AccountDataFetcher {

    private final Logger logger = Logger.getLogger(getClass());

    private final ModelService<Account> modelService;

    private final InputModelAdapter<Account> modelAdapter;

    public AccountDataFetcher(ModelService<Account> modelService) {
        this.modelService = modelService;
        this.modelAdapter = new AccountAdapter();
    }

    public DataFetcher<Boolean> create() {
        return dataFetchingEnvironment -> {
            // adapt input to account
            Account account = modelAdapter.adapt(dataFetchingEnvironment.getArgument("input"));
            if (Objects.isNull(account)) {
                error(logger, "invalid information to create an account, skipping operation");
                return false;
            }
            // check if the account already exists
            if (Objects.nonNull(modelService.first("name", account.getName()))) {
                error(logger, "attempting to create an account that already exists '%s'", account.getName());
                return false;
            }
            // set account id
            account.setId(UUID.randomUUID().toString());
            // save account
            return modelService.save(Arrays.asList(account));
        };
    }
}
