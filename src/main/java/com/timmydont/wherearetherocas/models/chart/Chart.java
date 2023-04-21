package com.timmydont.wherearetherocas.models.chart;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Chart {

    private String title;
    private List<String> ids;
    private List<String> labels;
    private List<ChartDataSet> datasets;
}
