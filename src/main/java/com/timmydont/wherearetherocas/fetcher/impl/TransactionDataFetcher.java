package com.timmydont.wherearetherocas.fetcher.impl;

import com.timmydont.wherearetherocas.factory.ChartFactory;
import com.timmydont.wherearetherocas.fetcher.AbstractModelDataFetcher;
import com.timmydont.wherearetherocas.models.BalanceFiltered;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.models.chart.Chart;
import com.timmydont.wherearetherocas.services.ModelService;
import com.timmydont.wherearetherocas.utils.DateUtils;
import graphql.schema.DataFetcher;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class TransactionDataFetcher extends AbstractModelDataFetcher<Transaction> {

    private final Logger logger = Logger.getLogger(getClass());

    public TransactionDataFetcher(ModelService<Transaction> modelService, ChartFactory chartFactory) {
        super(modelService, chartFactory);
    }

    /**
     * Return a list of all transactions that match a given text, for a specific account
     *
     * @return a list of transactions
     */
    public DataFetcher<List<Transaction>> fetchByText() {
        return dataFetchingEnvironment -> {
            String text = getArgument(dataFetchingEnvironment, "text", String.class);
            String account = getArgument(dataFetchingEnvironment, "account", String.class);
            // get all transactions
            List<Transaction> items = modelService.all(account);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "no transactions where retrieved, with text '%s', for account '%s'", text, account);
                return null;
            }
            return items.stream()
                    .filter(item -> item.getItem().contains(text))
                    .collect(Collectors.toList());
        };
    }

    /**
     * Return a balance of all transactions that match a given text, for a specific account
     *
     * @return a filtered balance
     */
    public DataFetcher<BalanceFiltered> fetchBalanceByText() {
        return dataFetchingEnvironment -> {
            String text = getArgument(dataFetchingEnvironment, "text", String.class);
            String account = getArgument(dataFetchingEnvironment, "account", String.class);
            // get all transactions
            List<Transaction> items = modelService.all(account);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "no transactions where retrieved, with text '%s', for account '%s'", text, account);
                return null;
            }
            // create balance filtered and populate with data
            BalanceFiltered balance = BalanceFiltered.builder()
                    .item(text)
                    .build();
            items.stream()
                    .filter(item -> item.getItem().equalsIgnoreCase(text))
                    .forEach(item -> balance.add(item.getAmount(), item.getDate()));
            return balance;
        };
    }

    /**
     * Retrieve a chart of transactions that contains a given text
     *
     * @return a chart
     */
    public DataFetcher<Chart> fetchByTextChart() {
        return dataFetchingEnvironment -> {
            String text = getArgument(dataFetchingEnvironment, "text", String.class);
            String account = getArgument(dataFetchingEnvironment, "account", String.class);
            // get all transactions
            List<Transaction> items = modelService.all(account);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "no transactions where retrieved, with text '%s', for account '%s'", text, account);
                return null;
            }
            // create single dataset chart and populate
            Chart chart = chartFactory.createSingleDataSetChart(text);
            items.stream()
                    .filter(item -> item.getItem().contains(text))
                    .forEachOrdered(item -> {
                        chart.addSingleDataSet(
                                DateUtils.toString(item.getDate()),
                                item.getAmount());
                    });
            return chart;
        };
    }
}
