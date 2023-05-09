package com.timmydont.wherearetherocas.factory.impl;

import com.timmydont.wherearetherocas.factory.ChartFactory;
import com.timmydont.wherearetherocas.models.chart.Chart;
import com.timmydont.wherearetherocas.models.chart.ChartDataSet;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;

import java.util.*;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class ChartFactoryImpl implements ChartFactory {

    private final Logger logger = Logger.getLogger(getClass());

    private List<String> colours;
    private Map<ChartDataSetType, ChartDataSet> chartDataSetMap;

    public ChartFactoryImpl() {
        chartDataSetMap = new HashMap<>();
        chartDataSetMap.put(ChartDataSetType.Income, ChartDataSet.builder()
                .label("Income")
                .backgroundColor("rgba(81, 152, 114, 1)")
                .build());
        chartDataSetMap.put(ChartDataSetType.Outcome, ChartDataSet.builder()
                .label("Outcome")
                .backgroundColor("rgba(236, 78, 32, 1)")
                .build());
        chartDataSetMap.put(ChartDataSetType.Earnings, ChartDataSet.builder()
                .label("Earnings")
                .backgroundColor("rgba(255, 208, 70, 1)")
                .build());
        chartDataSetMap.put(ChartDataSetType.Savings, ChartDataSet.builder()
                .label("Savings")
                .backgroundColor("rgba(9, 64, 116, 1)")
                .build());
        // create the colours array
        colours = Arrays.asList("rgb(5, 41, 158)", "rgb(94, 74, 227)", "rgb(148, 123, 211)", "rgb(240, 167, 160)",
                "rgb(242, 108, 167)", "rgb(255, 174, 3)", "rgb(230, 127, 13)", "rgb(254, 78, 0)", "rgb(233, 25, 15)");
    }

    @Override
    public ChartDataSet getDataSet(ChartDataSetType type) {
        if(!chartDataSetMap.containsKey(type)) {
            error(logger, "there is no chart set for type '%s'", type);
            return null;
        }
        return SerializationUtils.clone(chartDataSetMap.get(type));
    }

    @Override
    public ChartDataSet createDataSet(String text) {
        return ChartDataSet.builder()
                .label(text)
                .backgroundColor(colours.get(new Random().nextInt(colours.size() - 1)))
                .build();
    }

    @Override
    public Chart createSingleDataSetChart(String title) {
        return Chart.builder()
                .ids(new ArrayList<>())
                .title(title)
                .labels(new ArrayList<>())
                .datasets(Arrays.asList(createDataSet(title)))
                .build();
    }
}
