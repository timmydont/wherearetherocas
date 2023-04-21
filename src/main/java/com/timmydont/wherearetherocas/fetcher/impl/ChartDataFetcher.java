package com.timmydont.wherearetherocas.fetcher.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.Period;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.models.TransactionByItem;
import com.timmydont.wherearetherocas.models.chart.Chart;
import com.timmydont.wherearetherocas.models.chart.ChartDataSet;
import graphql.schema.DataFetcher;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChartDataFetcher {

    private final Logger logger = Logger.getLogger(getClass());

    private final DBService dbService;

    private final JaroWinklerDistance jaroWinklerDistance;

    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public ChartDataFetcher(DBService dbService) {
        this.dbService = dbService;
        this.jaroWinklerDistance = new JaroWinklerDistance();
    }

    public DataFetcher<Chart> fetchAnotherOne() {
        return dataFetchingEnvironment -> {
            Period period = Period.Month;
            Map<Integer, String> periods = new HashMap<>();
            Map<String, ChartDataSet> dataSetMap = new HashMap<>();

            List<Transaction> transactions = dbService.list(Transaction.class);
            Date start = transactions.get(0).getDate();
            Date end = transactions.get(transactions.size() - 1).getDate();

            int asCalendarEnd = period.getAsCalendar(end);
            int asCalendarStart = period.getAsCalendar(start);

            int magic = asCalendarEnd - asCalendarStart; // chequear

            List<TransactionByItem> items = dbService.list(TransactionByItem.class);
            for (TransactionByItem item : items) {
                for (Transaction t : item.getTransactions()) {
                    int asCalendar = period.getAsCalendar(t.getDate());
                    if (!periods.containsKey(asCalendar)) {
                        periods.put(asCalendar, period.getStart(t.getDate()).toString());
                    }
                    try {
                        ChartDataSet dataSet = dataSetMap.containsKey(item.getItem()) ?
                                dataSetMap.get(item.getItem()) :
                                ChartDataSet.builder()
                                        .label(item.getItem())
                                        .backgroundColor(randomColor())
                                        .data(lateta(magic + 1))
                                        .build();
                        dataSet.add(asCalendar - asCalendarStart, Math.abs(t.getAmount()));
                        dataSetMap.put(item.getItem(), dataSet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return Chart.builder()
                    .title("ya ni se")
                    .labels(new ArrayList<>(periods.values()))
                    .datasets(new ArrayList<>(dataSetMap.values()))
                    .build();
        };
    }

    private List<Float> lateta(int size) {
        return IntStream.range(0, size).mapToObj(i -> 0f).collect(Collectors.toList());
    }

    /*public DataFetcher<Chart> fetchChartDayItems() {
        return dataFetchingEnvironment -> {
            List<TransactionByDate> dates = dbService.list(TransactionByDate.class);
            List<TransactionByItem> items = dbService.list(TransactionByItem.class);
            List<String> labels = new ArrayList<>();

            Map<String, ChartDataSet> dataSetMap = new HashMap<>();
            dates.forEach(td -> {
                labels.add(dateFormat.format(td.getDate()));

                items.forEach(ti -> {
                    ChartDataSet dataSet = dataSetMap.containsKey(ti.getItem()) ?
                            dataSetMap.get(ti.getItem()) :
                            ChartDataSet.builder()
                                    .label(ti.getItem())
                                    .backgroundColor(randomColor())
                                    .data(new ArrayList<>())
                                    .build();
                    float amount = 0f;
                    for (Transaction t : td.getTransactions()) {
                        if (jaroWinklerDistance.apply(t.getItem(), ti.getItem().toUpperCase()) < 0.2d) {
                            amount += t.getAmount();
                        }
                    }
                    dataSet.add(amount);
                    dataSetMap.put(ti.getItem(), dataSet);
                });
            });
            return Chart.builder()
                    .title("by Day by Item")
                    .labels(labels)
                    .datasets(new ArrayList<>(dataSetMap.values()))
                    .build();
        };
    }*/

    private static Random rand = new Random();

    private String randomColor() {
        // Java 'Color' class takes 3 floats, from 0 to 1.
        float r = rand.nextFloat() * 255;
        float g = rand.nextFloat() * 255;
        float b = rand.nextFloat() * 255;
        return String.format("rgb(%s,%s,%s)", r, g, b);
    }
}
