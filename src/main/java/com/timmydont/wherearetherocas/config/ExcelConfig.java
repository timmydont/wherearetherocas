package com.timmydont.wherearetherocas.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;

@Getter
@Setter
@Builder
public class ExcelConfig {

    private int startRow;
    private int sheetIndex;
    private int itemIndex;
    private int dateIndex;
    private int amountIndex;
    private int descriptionIndex;
    private DateFormat format;
}
