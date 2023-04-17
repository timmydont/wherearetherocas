package com.timmydont.wherearetherocas.models;

import com.timmydont.wherearetherocas.utils.MathUtils;
import lombok.Builder;
import lombok.Data;

import java.util.stream.IntStream;

@Data
@Builder
public class Statistics {

    private float min;
    private float max;
    private float sum;
    private float median;
    private float average;

    public Statistics populate(Float[] values) {
        IntStream.range(0, values.length).forEach(i -> {
            float value = values[i];
            min = Float.min(min, value);
            max = Float.max(max, value);
            sum += value;
        });
        median = MathUtils.getMedian(values);
        average = sum / values.length;
        return this;
    }
}
