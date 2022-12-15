package com.timmydont.wherearetherocas.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceSummary {

    private float min;
    private float max;
    private float sum;
    private float median;
    private float average;
}
