package com.timmydont.wherearetherocas.models.chart;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChartDataSet {

    private String label;
    private List<Float> data;
}
