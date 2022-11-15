package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.adapters.TransactionAdapter;
import com.timmydont.wherearetherocas.adapters.impl.ExcelRowTransactionAdapter;
import com.timmydont.wherearetherocas.config.ExcelConfig;
import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.models.TransactionByDate;
import com.timmydont.wherearetherocas.models.TransactionByItem;
import graphql.schema.DataFetcher;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.debug;
import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class ExcelLoadDataFetcher {

    private final Logger logger = Logger.getLogger(getClass());

    private final ExcelConfig config;

    private final DBService dbService;

    private final JaroWinklerDistance jaroWinklerDistance;

    private final TransactionAdapter<Row> transactionAdapter;

    public ExcelLoadDataFetcher(DBService dbService, ExcelConfig config) {
        this.config = config;
        this.dbService = dbService;
        this.transactionAdapter = new ExcelRowTransactionAdapter(config);
        this.jaroWinklerDistance = new JaroWinklerDistance();
    }

    public DataFetcher<Boolean> load() {
        return dataFetchingEnvironment -> {
            // get input from request
            String input = dataFetchingEnvironment.getArgument("input");
            if (StringUtils.isBlank(input)) {
                error(logger, "invalid argument in load data");
                return false;
            }
            // create workbook and get sheet
            Sheet sheet = getSheet(input);
            if (sheet == null) {
                error(logger, "unable to get sheet from file %s, sheet %s", input, config.getSheetIndex());
                return false;
            }
            int fr = config.getStartRow();
            int lr = sheet.getLastRowNum();
            //
            List<Transaction> transactions = new ArrayList<>();
            for (int i = fr; i <= lr; i++) {
                Transaction transaction = transactionAdapter.adapt(sheet.getRow(i));
                if (transaction == null) {
                    error(logger, "unable to adapt row %s to transaction", i);
                    continue;
                }
                transactions.add(transaction);
            }
            Collections.sort(transactions);


            Map<String, TransactionByItem> transactionByItem = new HashMap<>();
            Map<Instant, TransactionByDate> transactionByDate = new HashMap<>();
            float balance = 0f;
            //
            for(Transaction transaction : transactions) {
                Instant instant = transaction.getDate().toInstant()
                        .truncatedTo(ChronoUnit.DAYS);
                TransactionByDate tbd = transactionByDate.get(instant);
                if (tbd == null) {
                    tbd = TransactionByDate.builder()
                            .id(transaction.getId())
                            .date(transaction.getDate())
                            .income(0f)
                            .outcome(0f)
                            .balance(balance)
                            .build();
                }
                balance += transaction.getAmount();
                tbd.add(transaction);
                transactionByDate.put(instant, tbd);

                boolean added = false;
                for (String key : transactionByItem.keySet()) {
                    Double distance = jaroWinklerDistance.apply(key.toUpperCase(), transaction.getItem().toUpperCase());
                    debug(logger, "Distance of %s to %s is %,.2f", transaction.getItem(), key, distance);
                    if (distance < 0.2d) {
                        TransactionByItem item = transactionByItem.get(key);
                        item.add(transaction);
                        added = true;
                    }
                }
                if (!added) {
                    TransactionByItem item = TransactionByItem.builder()
                            .id(transaction.getId())
                            .item(transaction.getItem())
                            .build();
                    item.add(transaction);
                    transactionByItem.put(transaction.getItem(), item);
                }
            }


            dbService.add(transactions);
            //
            dbService.add(transactionByItem.values());
            //

            List<TransactionByDate> items = new ArrayList<>(transactionByDate.values());
            Collections.sort(items);
            dbService.add(items);
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
}
