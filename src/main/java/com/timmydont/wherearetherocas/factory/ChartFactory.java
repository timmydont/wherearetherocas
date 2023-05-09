package com.timmydont.wherearetherocas.factory;

import com.timmydont.wherearetherocas.models.chart.Chart;
import com.timmydont.wherearetherocas.models.chart.ChartDataSet;

public interface ChartFactory {

    enum ChartDataSetType {
        Income, Outcome, Savings, Earnings
    }

    /**
     * Retrieve a chart dataset for a given dataset type
     *
     * @param type the dataset type
     * @return a chart dataset
     */
    ChartDataSet getDataSet(ChartDataSetType type);

    /**
     * Create a chart dataset with a given title and a random colour
     *
     * @param title the title
     * @return a chart
     */
    ChartDataSet createDataSet(String title);

    /**
     * Create a single dataset chart with a given title, and a random colour
     *
     * @param title the title
     * @return a chart
     */
    Chart createSingleDataSetChart(String title);
}
