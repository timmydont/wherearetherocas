package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.TransactionByDate;
import com.timmydont.wherearetherocas.models.chart.ChartDataSet;
import com.timmydont.wherearetherocas.models.chart.ChartLine;
import graphql.schema.DataFetcher;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChartDataFetcher {

    private final Logger logger = Logger.getLogger(getClass());

    private final DBService dbService;

    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public ChartDataFetcher(DBService dbService) {
        this.dbService = dbService;
    }

    /**
     * @return
     */
    public DataFetcher<ChartLine> fetchChartLine() {
        return dataFetchingEnvironment -> {
            List<TransactionByDate> items = dbService.list(TransactionByDate.class);
            List<Float> income = new ArrayList<>();
            List<Float> outcome = new ArrayList<>();
            List<Float> balance = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            items.forEach(item -> {
                labels.add(dateFormat.format(item.getDate()));
                income.add(item.getIncome());
                outcome.add(item.getOutcome());
                balance.add(item.getBalance());
            });
            return ChartLine.builder()
                    .title("Balance")
                    .labels(labels)
                    .datasets(Arrays.asList(
                            ChartDataSet.builder()
                                    .label("Income")
                                    .data(income)
                                    .build(),
                            ChartDataSet.builder()
                                    .label("Outcome")
                                    .data(outcome)
                                    .build(),
                            ChartDataSet.builder()
                                    .label("Balance")
                                    .data(balance)
                                    .build()))
                    .build();
        };
    }
}
