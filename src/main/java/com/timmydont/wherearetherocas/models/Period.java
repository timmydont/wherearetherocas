package com.timmydont.wherearetherocas.models;

import com.timmydont.wherearetherocas.utils.DateUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public enum Period {
    Day(Calendar.DAY_OF_YEAR), Week(Calendar.WEEK_OF_YEAR), Month(Calendar.MONTH), Year(Calendar.YEAR);

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

    public Date getEnd(Date date) {
        switch (this) {
            case Day:
                return date;
            case Week:
                LocalDate end = DateUtils.convertToLocalDate(date);
                while (end.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    end = end.plusDays(1);
                }
                return DateUtils.toDate(end);
            case Month:
                LocalDate when = DateUtils.convertToLocalDate(date);
                LocalDate start = when.withDayOfMonth(
                        when.getMonth().length(when.isLeapYear()));
                return DateUtils.toDate(start);
            default:
                return null;
        }
    }

    public Date getStart(Date date) {
        switch (this) {
            case Day:
                return date;
            case Week:
                LocalDate start = DateUtils.convertToLocalDate(date);
                while (start.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    start = start.minusDays(1);
                }
                return DateUtils.toDate(start);
            case Month:
                return DateUtils.toDate(DateUtils.convertToLocalDate(date)
                        .withDayOfMonth(1));
            default:
                return null;
        }
    }
}
