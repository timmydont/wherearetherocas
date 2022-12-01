package com.timmydont.wherearetherocas.fetcher.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.Period;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.models.TransactionByDate;
import com.timmydont.wherearetherocas.models.TransactionByItem;
import com.timmydont.wherearetherocas.models.chart.ChartDataSet;
import com.timmydont.wherearetherocas.models.chart.ChartLine;
import com.timmydont.wherearetherocas.models.chart.ChartPie;
import com.timmydont.wherearetherocas.models.chart.ChartPieDataSet;
import graphql.schema.DataFetcher;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
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

    public DataFetcher<ChartLine> fetchAnotherOne() {
        return dataFetchingEnvironment -> {
            Period period = Period.Month;
            Map<Integer, String> periods = new HashMap<>();
            Map<String, ChartDataSet> dataSetMap = new HashMap<>();

            List<Transaction> transactions = dbService.list(Transaction.class);
            Date start = transactions.get(0).getDate();
            Date end = transactions.get(transactions.size() - 1).getDate();

            int asCalendarStart = period.getAsCalendar(start);
            int asCalendarEnd = period.getAsCalendar(end);

            int magic = asCalendarEnd - asCalendarStart;

            List<TransactionByItem> items = dbService.list(TransactionByItem.class);
            for (TransactionByItem item : items) {
                for (Transaction t : item.getTransactions()) {
                    int asCalendar = period.getAsCalendar(t.getDate());
                    if (!periods.containsKey(asCalendar)) {
                        periods.put(asCalendar, period.getStart(t.getDate()).toString());
                    }
                    ChartDataSet dataSet = dataSetMap.containsKey(item.getItem()) ?
                            dataSetMap.get(item.getItem()) :
                            ChartDataSet.builder()
                                    .label(item.getItem())
                                    .backgroundColor(randomColor())
                                    .data(lateta(magic + 1))
                                    .build();
                    dataSet.add(asCalendar - magic, Math.abs(t.getAmount()));
                    dataSetMap.put(item.getItem(), dataSet);
                }
            }
            return ChartLine.builder()
                    .title("ya ni se")
                    .labels(new ArrayList<>(periods.values()))
                    .datasets(new ArrayList<>(dataSetMap.values()))
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
            List<TransactionByItem> items = dbService.list(TransactionByItem.class);
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
                                    .data(lateta(items.size()))
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

    public static void main(String[] args) {
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse("30-09-2022");
            System.out.println(Period.Month.getAsCalendar(date));
            System.out.println(Period.Month.getStart(date));
            System.out.println(Period.Month.getEnd(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private List<Float> lateta(int size) {
        return IntStream.range(0, size).mapToObj(i -> 0f).collect(Collectors.toList());
    }

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

    public DataFetcher<ChartLine> fetchChartDayItems() {
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


            return ChartLine.builder()
                    .title("by Day by Item")
                    .labels(labels)
                    .datasets(new ArrayList<>(dataSetMap.values()))
                    .build();
        };
    }

    private static Random rand = new Random();

    private String randomColor() {
        // Java 'Color' class takes 3 floats, from 0 to 1.
        float r = rand.nextFloat() * 255;
        float g = rand.nextFloat() * 255;
        float b = rand.nextFloat() * 255;
        return String.format("rgb(%s,%s,%s)", r, g, b);
    }
}
