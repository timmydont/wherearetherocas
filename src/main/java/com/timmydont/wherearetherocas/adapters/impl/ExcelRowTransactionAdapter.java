package com.timmydont.wherearetherocas.adapters.impl;

import com.timmydont.wherearetherocas.adapters.TransactionAdapter;
import com.timmydont.wherearetherocas.config.ExcelConfig;
import com.timmydont.wherearetherocas.models.Transaction;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;

import java.text.ParseException;
import java.util.UUID;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class ExcelRowTransactionAdapter implements TransactionAdapter<Row> {

    private final Logger logger = Logger.getLogger(getClass());

    private ExcelConfig config;

    public ExcelRowTransactionAdapter(ExcelConfig config) {
        this.config = config;
    }

    @Override
    public Transaction adapt(Row item) {
        String date = item.getCell(config.getDateIndex()).getStringCellValue();
        float amount = (float) item.getCell(config.getAmountIndex()).getNumericCellValue();
        String description = item.getCell(config.getDescriptionIndex()).getStringCellValue();
        try {
            return Transaction.builder()
                    .id(UUID.randomUUID().toString())
                    .item(description)
                    .date(config.getFormat().parse(date))
                    .amount(amount)
                    .build();
        } catch (ParseException e) {
            error(logger, "unable to parse date field, with format %s, with value %s", config.getFormat().toString(), date);
            return null;
        }
    }
}
