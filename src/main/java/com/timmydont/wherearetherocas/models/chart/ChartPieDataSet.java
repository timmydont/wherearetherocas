package com.timmydont.wherearetherocas.models.chart;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChartPieDataSet {

    private String label;
    private List<Float> data;
    private List<String> backgroundColor;

    public void add(int index, float amount) {
        this.data.set(index, this.data.get(index) + amount);
    }
}
