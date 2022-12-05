package com.timmydont.wherearetherocas.fetcher.impl;

import com.timmydont.wherearetherocas.fetcher.AbstractModelDataFetcher;
import com.timmydont.wherearetherocas.models.TransactionByDate;
import com.timmydont.wherearetherocas.models.chart.Chart;
import com.timmydont.wherearetherocas.models.chart.ChartDataSet;
import com.timmydont.wherearetherocas.services.ModelService;
import com.timmydont.wherearetherocas.utils.DateUtils;
import graphql.schema.DataFetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransactionByDateDataFetcher extends AbstractModelDataFetcher<TransactionByDate> {

    public TransactionByDateDataFetcher(ModelService<TransactionByDate> modelService) {
        super(modelService);
    }

    /**
     * @return
     */
    public DataFetcher<Chart> fetchBalanceChart() {
        return dataFetchingEnvironment -> {
            List<String> labels = new ArrayList<>();
            // create chart datasets
            ChartDataSet income = ChartDataSet.builder()
                    .label("Income")
                    .backgroundColor("rgba(19, 111, 99, 1)")
                    .build();
            ChartDataSet outcome = ChartDataSet.builder()
                    .label("Outcome")
                    .backgroundColor("rgba(208, 0, 0, 1)")
                    .build();
            ChartDataSet balance = ChartDataSet.builder()
                    .label("Balance")
                    .backgroundColor("rgba(63, 136, 197, 1)")
                    .build();
            // populate data sets from transactions by dates
            modelService.all().forEach(item -> {
                labels.add(DateUtils.toString(item.getDate()));
                income.add(item.getIncome());
                outcome.add(item.getOutcome());
                balance.add(item.getBalance());
            });
            // create chart
            return Chart.builder()
                    .title("Balance History")
                    .labels(labels)
                    .datasets(Arrays.asList(balance, income, outcome))
                    .build();
        };
    }
}
