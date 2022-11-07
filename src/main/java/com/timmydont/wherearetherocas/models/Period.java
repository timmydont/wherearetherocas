package com.timmydont.wherearetherocas.models;

import com.timmydont.wherearetherocas.utils.DateUtils;

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
/*        LocalDate end = DateUtils.convertToLocalDate(when);
        while (end.getDayOfWeek() != DayOfWeek.SUNDAY) {
            end = end.plusDays(1);
        }
        return end.toString();*/
        LocalDate when = DateUtils.convertToLocalDate(date);
        LocalDate start = when.withDayOfMonth(
                when.getMonth().length(when.isLeapYear()));
        return start.toString();
    }

    public String getStart(Date date) {
        /*LocalDate start = DateUtils.convertToLocalDate(when);
        while (start.getDayOfWeek() != DayOfWeek.SUNDAY) {
            start = start.minusDays(1);
        }
        return start.toString();*/
        LocalDate when = DateUtils.convertToLocalDate(date);
        LocalDate start = when.withDayOfMonth(1);
        return start.toString();
    }
}
