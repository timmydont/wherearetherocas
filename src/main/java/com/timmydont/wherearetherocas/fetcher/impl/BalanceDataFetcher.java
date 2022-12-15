package com.timmydont.wherearetherocas.fetcher.impl;

import com.timmydont.wherearetherocas.fetcher.AbstractModelDataFetcher;
import com.timmydont.wherearetherocas.models.Balance;
import com.timmydont.wherearetherocas.models.BalanceSummary;
import com.timmydont.wherearetherocas.models.chart.Chart;
import com.timmydont.wherearetherocas.models.chart.ChartDataSet;
import com.timmydont.wherearetherocas.services.ModelService;
import com.timmydont.wherearetherocas.utils.DateUtils;
import com.timmydont.wherearetherocas.utils.MathUtils;
import graphql.schema.DataFetcher;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class BalanceDataFetcher extends AbstractModelDataFetcher<Balance> {

    private final Logger logger = Logger.getLogger(getClass());

    public BalanceDataFetcher(ModelService<Balance> modelService) {
        super(modelService);
    }

    public DataFetcher<Chart> fetchBalanceChart() {
        return dataFetchingEnvironment -> {
            List<Balance> items = modelService.all();
            if(CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve balances from db.");
                return null;
            }
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
            ChartDataSet savings = ChartDataSet.builder()
                    .label("Savings")
                    .backgroundColor("rgba(208, 208, 0, 1)")
                    .build();
            items.forEach(item-> {
                labels.add(DateUtils.toString(item.getStart()));
                income.add(item.getIncome());
                outcome.add(Math.abs(item.getOutcome()));
                savings.add(item.earnings());
            });
            return Chart.builder()
                    .title("Balance Chart")
                    .labels(labels)
                    .datasets(Arrays.asList(income, outcome, savings))
                    .build();
        };
    }

    public DataFetcher<BalanceSummary> fetchBalanceSummary() {
        return dataFetchingEnvironment -> {
            List<Balance> items = modelService.all();
            if(CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve balances from db.");
                return null;
            }
            // calculate the different balance summary variables
            float sum = 0f;
            float min = Float.MAX_VALUE;
            float max = Float.MIN_VALUE;
            Float[] values = new Float[items.size()];
            for(int i = 0; i < items.size(); i++) {
                float value = items.get(i).earnings();
                min = Float.min(min, value);
                max = Float.max(max, value);
                sum += value;
                values[i] = value;
            }
            return BalanceSummary.builder()
                    .min(min)
                    .max(max)
                    .sum(sum)
                    .median(MathUtils.getMedian(values))
                    .average(sum / items.size())
                    .build();
        };
    }
}
