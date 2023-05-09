package com.timmydont.wherearetherocas.models.chart;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

@Data
@Builder
public class Chart {

    private final Logger logger = Logger.getLogger(getClass());

    private String title;
    private List<String> ids;
    private List<String> labels;
    private List<ChartDataSet> datasets;

    /**
     * @param label
     * @param amount
     */
    public void addSingleDataSet(String label, Float amount) {
        // check that the Chart has been initialized
        if (labels == null || datasets == null) {
            error(logger, "attempting to add data to an uninitialized chart");
            return;
        }
        labels.add(label);
        datasets.get(0).add(amount);
    }

    /**
     *
     * @param id
     * @param label
     * @param amount
     */
    public void addSingleDataSet(String id, String label, Float amount) {
        // check that the Chart has been initialized
        if (ids == null || labels == null || datasets == null) {
            error(logger, "attempting to add data to an uninitialized chart");
            return;
        }
        ids.add(id);
        labels.add(label);
        datasets.get(0).add(amount);
    }
}
