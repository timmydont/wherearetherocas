package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.TransactionByDate;
import com.timmydont.wherearetherocas.models.TransactionByItem;
import com.timmydont.wherearetherocas.models.chart.ChartDataSet;
import com.timmydont.wherearetherocas.models.chart.ChartLine;
import graphql.schema.DataFetcher;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.debug;

public class ChartDataFetcher {

    private final Logger logger = Logger.getLogger(getClass());

    private final DBService dbService;

    private final JaroWinklerDistance jaroWinklerDistance;

    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public ChartDataFetcher(DBService dbService) {
        this.dbService = dbService;
        this.jaroWinklerDistance = new JaroWinklerDistance();
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

    public DataFetcher<ChartLine> fetchChartDayItems() {
        return dataFetchingEnvironment -> {
            List<TransactionByDate> dates = dbService.list(TransactionByDate.class);
            List<TransactionByItem> items = dbService.list(TransactionByItem.class);
            List<String> labels = new ArrayList<>();

            Map<String, ChartDataSet> dataSetMap = new HashMap<>();
            dates.forEach(td -> {
                labels.add(dateFormat.format(td.getDate()));

                items.forEach(ti -> {
                    td.getTransactions().forEach(t -> {
                        ChartDataSet dataSet = dataSetMap.get(ti.getItem());
                        if(dataSet == null) {
                            dataSet = ChartDataSet.builder()
                                    .label(ti.getItem())
                                    .backgroundColor(randomColor())
                                    .data(new ArrayList<>())
                                    .build();
                        }
                        if (jaroWinklerDistance.apply(t.getItem(), ti.getItem().toUpperCase()) < 0.2d) {
                            dataSet.add(t.getAmount());
                        } else {
                            dataSet.add(0f);
                        }
                        dataSetMap.put(ti.getItem(), dataSet);
                    });
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
