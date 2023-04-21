package com.timmydont.wherearetherocas.factory.impl;

import com.timmydont.wherearetherocas.factory.ChartFactory;
import com.timmydont.wherearetherocas.models.chart.ChartDataSet;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class ChartFactoryImpl implements ChartFactory {

    private final Logger logger = Logger.getLogger(getClass());

    private Map<ChartDataSetType, ChartDataSet> chartDataSetMap;

    public ChartFactoryImpl() {
        chartDataSetMap = new HashMap<>();
        chartDataSetMap.put(ChartDataSetType.Income, ChartDataSet.builder()
                .label("Income")
                .backgroundColor("rgba(19, 111, 99, 1)")
                .build());
        chartDataSetMap.put(ChartDataSetType.Outcome, ChartDataSet.builder()
                .label("Outcome")
                .backgroundColor("rgba(208, 0, 0, 1)")
                .build());
        chartDataSetMap.put(ChartDataSetType.Earnings, ChartDataSet.builder()
                .label("Earnings")
                .backgroundColor("rgba(125, 251, 0, 1)")
                .build());
        chartDataSetMap.put(ChartDataSetType.Savings, ChartDataSet.builder()
                .label("Savings")
                .backgroundColor("rgba(208, 208, 0, 1)")
                .build());
    }

    @Override
    public ChartDataSet getDataSet(ChartDataSetType type) {
        if(!chartDataSetMap.containsKey(type)) {
            error(logger, "there is no chart set for type '%s'", type);
            return null;
        }
        return SerializationUtils.clone(chartDataSetMap.get(type));
    }
}
