package com.timmydont.wherearetherocas.factory;

import com.timmydont.wherearetherocas.models.chart.ChartDataSet;

public interface ChartFactory {

    enum ChartDataSetType {
        Income, Outcome, Savings, Earnings
    }

    ChartDataSet getDataSet(ChartDataSetType type);
}
