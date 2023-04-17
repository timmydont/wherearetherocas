package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.adapters.TransactionAdapter;
import com.timmydont.wherearetherocas.adapters.TransactionsAdapter;
import com.timmydont.wherearetherocas.adapters.impl.BalancesAdapter;
import com.timmydont.wherearetherocas.adapters.impl.ExcelRowTransactionAdapter;
import com.timmydont.wherearetherocas.adapters.impl.TransactionsByDatesAdapter;
import com.timmydont.wherearetherocas.adapters.impl.TransactionsByItemsAdapter;
import com.timmydont.wherearetherocas.config.ExcelConfig;
import com.timmydont.wherearetherocas.factory.ModelServiceFactory;
import com.timmydont.wherearetherocas.models.*;
import graphql.schema.DataFetcher;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class ExcelLoadDataFetcher {

    private final Logger logger = Logger.getLogger(getClass());

    private final ExcelConfig config;

    // transaction adapters
    private final TransactionAdapter<Row> transactionAdapter;
    private final TransactionsAdapter<Balance> balanceAdapter;
    private final TransactionsAdapter<TransactionByItem> transactionsByItemsAdapter;
    private final TransactionsAdapter<TransactionByDate> transactionsByDatesAdapter;
    // model service factory
    private final ModelServiceFactory serviceFactory;

    public ExcelLoadDataFetcher(ModelServiceFactory serviceFactory, ExcelConfig config) {
        this.config = config;
        this.serviceFactory = serviceFactory;
        this.balanceAdapter = new BalancesAdapter();
        this.transactionAdapter = new ExcelRowTransactionAdapter(config);
        this.transactionsByItemsAdapter = new TransactionsByItemsAdapter();
        this.transactionsByDatesAdapter = new TransactionsByDatesAdapter();
    }

    public DataFetcher<Boolean> load() {
        return dataFetchingEnvironment -> {
            // check that account exists, and retrieve it
            Account account = serviceFactory.getService(Account.class).withId(dataFetchingEnvironment.getArgument("account"));
            if(Objects.isNull(account)) {
                error(logger, "invalid account provided '%s'", dataFetchingEnvironment.getArgument("account"));
                return false;
            }
            // get input from request
            String input = dataFetchingEnvironment.getArgument("input");
            if (StringUtils.isBlank(input)) {
                error(logger, "invalid document provided, impossible to load data to account '%s'", account.getId());
                return false;
            }
            // create workbook and get sheet
            Sheet sheet = getSheet(input);
            if (sheet == null) {
                error(logger, "unable to get sheet from file '%s', sheet '%s'", input, config.getSheetIndex());
                return false;
            }
            // get list of transactions from sheet
            List<Transaction> transactions = getTransactions(sheet, account.getId());
            try {
                serviceFactory.getService(Transaction.class).save(transactions);
                // get list of balances from transactions, and save
                serviceFactory.getService(Balance.class).save(balanceAdapter.adapt(transactions));
                // get list of transactions by items from transactions, and save
                serviceFactory.getService(TransactionByItem.class).save(transactionsByItemsAdapter.adapt(transactions));
                // get list of transactions by dates from transactions, and save
                serviceFactory.getService(TransactionByDate.class).save(transactionsByDatesAdapter.adapt(transactions));
            } catch (Exception e) {
                error(logger, e, "failed to load data from sheet");
                throw new Exception("failed to load data from sheet, check previous log entries");
            }
            return true;
        };
    }

    private Sheet getSheet(String input) {
        try {
            Workbook workbook = WorkbookFactory.create(new File(input));
            return workbook.getSheetAt(config.getSheetIndex());
        } catch (IOException e) {
            error(logger, "unable to create workbook from file %s", input, e);
        }
        return null;
    }

    private List<Transaction> getTransactions(Sheet sheet, String account) {
        // get first and last transaction row
        int fr = config.getStartRow();
        int lr = sheet.getLastRowNum();
        // read each row of the sheet, and try to adapt it into a Transaction
        List<Transaction> transactions = new ArrayList<>();
        IntStream.rangeClosed(fr, lr).forEachOrdered(i -> {
            Transaction transaction = transactionAdapter.adapt(sheet.getRow(i));
            if (transaction == null) {
                error(logger, "unable to adapt row %s to transaction", i);
            } else {
                transaction.setAccount(account);
                transactions.add(transaction);
            }
        });
        // sort transactions by date
        Collections.sort(transactions);

        return transactions;
    }
}
