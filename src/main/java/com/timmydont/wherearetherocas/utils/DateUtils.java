package com.timmydont.wherearetherocas.utils;

import com.timmydont.wherearetherocas.models.Period;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static Calendar calendar = Calendar.getInstance();
    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public static int getAsPeriod(Date date, Period period) {
        calendar.setTime(date);
        return calendar.get(period.period);
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    public static Date toDate(String date) {
        if(StringUtils.isBlank(date)) return null;
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Instant instant1 = date1.toInstant()
                .truncatedTo(ChronoUnit.DAYS);
        Instant instant2 = date2.toInstant()
                .truncatedTo(ChronoUnit.DAYS);
        return instant1.equals(instant2);
    }

    public static boolean inRange(Date date, Date start, Date end) {
        return date.after(start) && date.before(end);
    }
}
