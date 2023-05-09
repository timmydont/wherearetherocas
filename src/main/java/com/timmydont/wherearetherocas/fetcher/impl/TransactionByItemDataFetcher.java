package com.timmydont.wherearetherocas.fetcher.impl;

import com.timmydont.wherearetherocas.fetcher.AbstractModelDataFetcher;
import com.timmydont.wherearetherocas.models.Statistics;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.models.TransactionByItem;
import com.timmydont.wherearetherocas.models.chart.Chart;
import com.timmydont.wherearetherocas.models.chart.ChartDataSet;
import com.timmydont.wherearetherocas.models.chart.ChartPie;
import com.timmydont.wherearetherocas.models.chart.ChartPieDataSet;
import com.timmydont.wherearetherocas.models.enums.DataType;
import com.timmydont.wherearetherocas.models.enums.Period;
import com.timmydont.wherearetherocas.services.ModelService;
import com.timmydont.wherearetherocas.utils.DataStructureUtils;
import graphql.schema.DataFetcher;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;
import static com.timmydont.wherearetherocas.utils.DateUtils.inRange;

public class TransactionByItemDataFetcher extends AbstractModelDataFetcher<TransactionByItem> {

    private final Logger logger = Logger.getLogger(getClass());

    public TransactionByItemDataFetcher(ModelService<TransactionByItem> modelService) {
        super(modelService);
    }

    /**
     * @return
     */
    public DataFetcher<TransactionByItem> fetchByItem() {
        return dataFetchingEnvironment -> {
            String item = getArgument(dataFetchingEnvironment, "item", String.class);
            // check if request argument is valid
            if (StringUtils.isBlank(item)) return null;
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
     * @return
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
     * @return
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
     * @return
     */
    public DataFetcher<Chart> fetchByItemChart() {
        return dataFetchingEnvironment -> {
            // get item from request
            String item = getArgument(dataFetchingEnvironment, "item", String.class);
            if (!ObjectUtils.allNotNull(item)) return null;
            // get transactions by item for the item passed as argument
            TransactionByItem byItem = modelService.withId(item);
            if (Objects.isNull(byItem)) {
                error(logger, "unable to retrieve transactions by items from db for item '%s'.", item);
                return null;
            }
            // create chart dataset
            ChartDataSet dataSet = ChartDataSet.builder()
                    .label("By Item " + item)
                    .backgroundColor("rgba(1, 2, 114, 1)")
                    .build();
            List<String> labels = new ArrayList<>();
            Map<String, Float> asMap = byItem.asMap();
            asMap.keySet().forEach(key -> {
                labels.add(key);
                dataSet.add(asMap.get(key));
            });
            // create chart
            return Chart.builder()
                    .title("By Item " + item)
                    .labels(labels)
                    .datasets(Arrays.asList(dataSet))
                    .build();
        };
    }

    /**
     * @return
     */
    public DataFetcher<Statistics> fetchBalanceByItem() {
        return dataFetchingEnvironment -> {
            // get item from request
            String item = getArgument(dataFetchingEnvironment, "item", String.class);
            if (!ObjectUtils.allNotNull(item)) return null;
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
     * @return
     */
    public DataFetcher<Chart> fetchAccountByItemsChart() {
        return dataFetchingEnvironment -> {
            // get account and data type from request
            DataType type = getArgument(dataFetchingEnvironment, "type", DataType.class);
            String account = getArgument(dataFetchingEnvironment, "account", String.class);
            if (!ObjectUtils.allNotNull(account)) return null;
            // get all transactions by items
            List<TransactionByItem> items = modelService.all(account);
            if (CollectionUtils.isEmpty(items)) {
                error(logger, "unable to retrieve transactions by items from db for account '%s'.", account);
                return null;
            }
            //
            Collections.sort(items);
            //
            List<String> ids = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            // create chart datasets
            ChartDataSet dataSet = ChartDataSet.builder()
                    .label("By Items")
                    .backgroundColor("rgba(81, 152, 114, 1)")
                    .build();
            for (TransactionByItem item : items) {
                if (!type.equals(DataType.All) && !item.getType().equals(type)) continue;
                ids.add(item.getId());
                labels.add(item.getItem());
                dataSet.add(item.getAmount());
            }
            //
            return Chart.builder()
                    .ids(ids)
                    .title("By Items Chart")
                    .labels(labels)
                    .datasets(Arrays.asList(dataSet))
                    .build();
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
