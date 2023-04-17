package com.timmydont.wherearetherocas.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceSummary {

    private Statistics balance;
    private Statistics income;
    private Statistics outcome;
}
