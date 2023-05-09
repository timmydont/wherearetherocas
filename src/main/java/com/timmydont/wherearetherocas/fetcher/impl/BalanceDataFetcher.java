package com.timmydont.wherearetherocas.fetcher.impl;

import com.timmydont.wherearetherocas.factory.ChartFactory;
import com.timmydont.wherearetherocas.fetcher.AbstractModelDataFetcher;
import com.timmydont.wherearetherocas.models.Balance;
import com.timmydont.wherearetherocas.models.BalanceSummary;
import com.timmydont.wherearetherocas.models.enums.Period;
import com.timmydont.wherearetherocas.models.Statistics;
import com.timmydont.wherearetherocas.models.chart.Chart;
import com.timmydont.wherearetherocas.models.chart.ChartDataSet;
import com.timmydont.wherearetherocas.services.ModelService;
import com.timmydont.wherearetherocas.utils.DateUtils;
import graphql.schema.DataFetcher;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.IntStream;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class BalanceDataFetcher extends AbstractModelDataFetcher<Balance> {

    private final Logger logger = Logger.getLogger(getClass());

    private final ChartFactory chartFactory;

    public BalanceDataFetcher(ModelService<Balance> modelService, ChartFactory chartFactory) {
        super(modelService);
        this.chartFactory = chartFactory;
    }

    /**
     * Retrieve a chart of income/outcome/earnings/savings for a given account and period of time.
     *
     * @return the chart
     */
    public synchronized DataFetcher<Chart> fetchAccountBalanceChart() {
        return dataFetchingEnvironment -> {
            // create the properties map to query for balances data
            Map<String, Object> properties = getArgumentsMap(dataFetchingEnvironment,
                    new ImmutablePair("period", Period.class),
                    new ImmutablePair("account", String.class));
            // query for balances data
            List<Balance> items = modelService.all(properties);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve balances from db with properties '%s'", properties);
                return null;
            }
            List<String> ids = Collections.synchronizedList(new ArrayList<>());
            List<String> labels = Collections.synchronizedList(new ArrayList<>());
            // create chart datasets
            ChartDataSet incomes = chartFactory.getDataSet(ChartFactory.ChartDataSetType.Income);
            ChartDataSet savings = chartFactory.getDataSet(ChartFactory.ChartDataSetType.Savings);
            ChartDataSet outcomes = chartFactory.getDataSet(ChartFactory.ChartDataSetType.Outcome);
            ChartDataSet earnings = chartFactory.getDataSet(ChartFactory.ChartDataSetType.Earnings);
            // populate chart datasets
            float rocas = 0f;
            for (Balance item : items) {
                ids.add(item.getId());
                labels.add(DateUtils.toString(item.getStart()));
                rocas += item.earnings();
                incomes.add(item.getIncome());
                savings.add(rocas);
                outcomes.add(item.getOutcome());
                earnings.add(item.earnings());
            }
            // create and return chart
            return Chart.builder()
                    .ids(ids)
                    .title("Balance Chart")
                    .labels(labels)
                    .datasets(Arrays.asList(incomes, outcomes, earnings, savings))
                    .build();
        };
    }

    /**
     * Retrieve a balance summary for a specific account
     *
     * @return a balance summary
     */
    public DataFetcher<BalanceSummary> fetchBalanceSummary() {
        return dataFetchingEnvironment -> {
            // create the properties map to query for balances data
            Map<String, Object> properties = getArgumentsMap(dataFetchingEnvironment,
                    new ImmutablePair("period", Period.class),
                    new ImmutablePair("account", String.class));
            // query for balances data
            List<Balance> items = modelService.all(properties);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve balances from db with properties '%s'", properties);
                return null;
            }
            // populate data array of each data set
            Float[] balance = new Float[items.size()];
            Float[] incomes = new Float[items.size()];
            Float[] outcomes = new Float[items.size()];
            IntStream.range(0, items.size()).forEach(i -> {
                balance[i] = items.get(i).earnings();
                incomes[i] = items.get(i).getIncome();
                outcomes[i] = items.get(i).getOutcome();
            });
            return BalanceSummary.builder()
                    .balance(Statistics.builder()
                            .build()
                            .populate(balance))
                    .income(Statistics.builder()
                            .build()
                            .populate(incomes))
                    .outcome(Statistics.builder()
                            .build()
                            .populate(outcomes))
                    .build();
        };
    }
}
