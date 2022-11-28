package com.timmydont.wherearetherocas.models.chart;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class ChartDataSet {

    private String label;
    private String backgroundColor;
    private List<Float> data;

    public void add(float amount) {
        this.data.add(amount);
    }

    public void add(int index, float amount) {
        this.data.set(index, this.data.get(index) + amount);
    }

    public void sort() {
        Collections.sort(data);
    }
}
