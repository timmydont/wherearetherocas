package com.timmydont.wherearetherocas.fetcher.impl;

import com.timmydont.wherearetherocas.factory.ChartFactory;
import com.timmydont.wherearetherocas.fetcher.AbstractModelDataFetcher;
import com.timmydont.wherearetherocas.models.Statistics;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.models.TransactionByItem;
import com.timmydont.wherearetherocas.models.chart.Chart;
import com.timmydont.wherearetherocas.models.chart.ChartPie;
import com.timmydont.wherearetherocas.models.chart.ChartPieDataSet;
import com.timmydont.wherearetherocas.models.enums.DataType;
import com.timmydont.wherearetherocas.models.enums.Period;
import com.timmydont.wherearetherocas.services.ModelService;
import com.timmydont.wherearetherocas.utils.DataStructureUtils;
import graphql.schema.DataFetcher;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;
import static com.timmydont.wherearetherocas.utils.DateUtils.inRange;

public class TransactionByItemDataFetcher extends AbstractModelDataFetcher<TransactionByItem> {

    private final Logger logger = Logger.getLogger(getClass());

    public TransactionByItemDataFetcher(ModelService<TransactionByItem> modelService, ChartFactory chartFactory) {
        super(modelService, chartFactory);
    }

    /**
     * Return a transaction by item object from a given item
     *
     * @return a transaction by item
     */
    public DataFetcher<TransactionByItem> fetchByItem() {
        return dataFetchingEnvironment -> {
            String item = getArgument(dataFetchingEnvironment, "item", String.class);
            // get transactions by specific item
            TransactionByItem byItem = modelService.withId(item);
            if (byItem == null) {
                error(logger, "unable to retrieve transaction by item '%s'", item);
                return null;
            }
            return byItem;
        };
    }

    /**
     * Return a transaction by item object for a given item for a period of time
     *
     * @return a transaction by item
     */
    public DataFetcher<TransactionByItem> fetchByItemByPeriod() {
        return dataFetchingEnvironment -> {
            // get item start and end date from request
            Date end = getArgument(dataFetchingEnvironment, "end", Date.class);
            Date start = getArgument(dataFetchingEnvironment, "start", Date.class);
            String item = getArgument(dataFetchingEnvironment, "item", String.class);
            // get a specific transaction by item
            TransactionByItem transactionByItem = modelService.withId(item);
            if (transactionByItem == null) {
                error(logger, "unable to retrieve transaction by item '%s'", item);
                return null;
            }
            // filter transactions that are not in date range
            transactionByItem.removeAll(transactionByItem.getTransactions()
                    .stream()
                    .filter(t -> !inRange(t.getDate(), start, end))
                    .collect(Collectors.toList()));
            return transactionByItem;
        };
    }

    /**
     * Return a transaction by items list for a given period of time
     *
     * @return a transaction by items list
     */
    public DataFetcher<List<TransactionByItem>> fetchByItemsByPeriod() {
        return dataFetchingEnvironment -> {
            // get item start and end date from request
            Date end = getArgument(dataFetchingEnvironment, "end", Date.class);
            Date start = getArgument(dataFetchingEnvironment, "start", Date.class);
            String account = getArgument(dataFetchingEnvironment, "account", String.class);
            // get all transactions by item
            List<TransactionByItem> items = modelService.all(account);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve transactions by items from db.");
                return null;
            }
            // remove transactions that are not in range
            items.forEach(item -> {
                item.removeAll(item.getTransactions()
                        .stream()
                        .filter(t -> !inRange(t.getDate(), start, end))
                        .collect(Collectors.toList()));
            });
            return items;
        };
    }

    /**
     * Retrieve statistic balance information for a given item
     *
     * @return a statistics balance
     */
    public DataFetcher<Statistics> fetchBalanceByItem() {
        return dataFetchingEnvironment -> {
            // get item from request
            String item = getArgument(dataFetchingEnvironment, "item", String.class);
            // get transactions by item for the item passed as argument
            TransactionByItem byItem = modelService.withId(item);
            if (Objects.isNull(byItem)) {
                error(logger, "unable to retrieve transactions by items from db for item '%s'.", item);
                return null;
            }
            // create statistics object and populate with transactions data
            return Statistics.builder()
                    .build()
                    .populate(byItem.asArray());
        };
    }

    /**
     * Retrieve a chart of transactions for a given item
     *
     * @return a chart
     */
    public DataFetcher<Chart> fetchByItemChart() {
        return dataFetchingEnvironment -> {
            // get item from request
            String item = getArgument(dataFetchingEnvironment, "item", String.class);
            // get transactions by item for the item passed as argument
            TransactionByItem byItem = modelService.withId(item);
            if (Objects.isNull(byItem)) {
                error(logger, "unable to retrieve transactions by items from db for item '%s'.", item);
                return null;
            }
            // create single dataset chart and populate
            Map<String, Float> asMap = byItem.asMap();
            Chart chart = chartFactory.createSingleDataSetChart("item");
            asMap.keySet().forEach(key -> {
                chart.addSingleDataSet(key, asMap.get(key));
            });
            return chart;
        };
    }

    /**
     * Retrieve a chart of transactions by items for a given data type and account
     *
     * @return a chart
     */
    public DataFetcher<Chart> fetchAccountByItemsChart() {
        return dataFetchingEnvironment -> {
            // get account and data type from request
            DataType type = getArgument(dataFetchingEnvironment, "type", DataType.class);
            String account = getArgument(dataFetchingEnvironment, "account", String.class);
            // get all transactions by items
            List<TransactionByItem> items = modelService.all(account);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve transactions by items from db for account '%s'.", account);
                return null;
            }
            // sort the transactions by items
            Collections.sort(items);
            // create single dataset chart and populate
            Chart chart = chartFactory.createSingleDataSetChart("By Items");
            for (TransactionByItem item : items) {
                if (!type.equals(DataType.All) && !item.getType().equals(type)) continue;
                // add data to chart
                chart.addSingleDataSet(item.getId(), item.getItem(), item.getAmount());
            }
            return chart;
        };
    }

    /**
     * Data is going to be items, dataset is going to be weeks
     *
     * @return
     */
    public DataFetcher<ChartPie> fetchPieChart() {
        return dataFetchingEnvironment -> {
            Period period = Period.Month;
            List<String> labels = new ArrayList<>();
            List<String> colors = new ArrayList<>();
            Map<Integer, ChartPieDataSet> dataSets = new HashMap<>();
            //
            List<TransactionByItem> items = modelService.all("");//TODO BROKEN
            items.forEach(e -> colors.add(randomColor()));
            int i = 0;
            for (TransactionByItem item : items) {
                labels.add(item.getItem());
                for (Transaction t : item.getTransactions()) {
                    int asCalendar = period.getAsCalendar(t.getDate());
                    ChartPieDataSet dataSet = dataSets.containsKey(asCalendar) ?
                            dataSets.get(asCalendar) :
                            ChartPieDataSet.builder()
                                    .label(period.getStart(t.getDate()).toString())
                                    .backgroundColor(colors)
                                    .data(DataStructureUtils.initialize(items.size(), 0f))
                                    .build();
                    dataSet.add(i, Math.abs(t.getAmount()));
                    dataSets.put(asCalendar, dataSet);
                }
                i++;
            }

            return ChartPie.builder()
                    .title("data is items, dataset is weeks")
                    .labels(labels)
                    .datasets(new ArrayList<>(dataSets.values()))
                    .build();
        };
    }

    private String randomColor() {
        Random rand = new Random();
        // Java 'Color' class takes 3 floats, from 0 to 1.
        float r = rand.nextFloat() * 255;
        float g = rand.nextFloat() * 255;
        float b = rand.nextFloat() * 255;
        return String.format("rgb(%s,%s,%s)", r, g, b);
    }
}
