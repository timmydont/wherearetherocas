package com.timmydont.wherearetherocas.models;

import com.timmydont.wherearetherocas.utils.DateUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public enum Period {
    Week(Calendar.WEEK_OF_YEAR), Month(Calendar.MONTH), Year(Calendar.YEAR);

    Period(int period) {
        this.period = period;
    }

    public int period;

    public static Period getPeriod(int id) {
        return Arrays.stream(Period.values()).filter(period -> period.period == id).findFirst().orElse(null);
    }

    public int getAsCalendar(Date date) {
        return DateUtils.getAsPeriod(date, this);
    }

    public String getEnd(Date date) {
        switch (this) {
            case Week:
                LocalDate end = DateUtils.convertToLocalDate(date);
                while (end.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    end = end.plusDays(1);
                }
                return end.toString();
            case Month:
                LocalDate when = DateUtils.convertToLocalDate(date);
                LocalDate start = when.withDayOfMonth(
                        when.getMonth().length(when.isLeapYear()));
                return start.toString();
            default:
                return null;
        }
    }

    public String getStart(Date date) {
        switch (this) {
            case Week:
                LocalDate start = DateUtils.convertToLocalDate(date);
                while (start.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    start = start.minusDays(1);
                }
                return start.toString();
            case Month:
                return DateUtils.convertToLocalDate(date)
                        .withDayOfMonth(1)
                        .toString();
            default:
                return null;
        }
    }
}
