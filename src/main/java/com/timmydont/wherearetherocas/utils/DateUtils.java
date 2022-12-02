package com.timmydont.wherearetherocas.utils;

import com.timmydont.wherearetherocas.models.Period;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class DateUtils {

    private static final Logger logger = Logger.getLogger(DateUtils.class);

    private static Calendar calendar = Calendar.getInstance();

    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public static SimpleDateFormat localFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static int getAsPeriod(Date date, Period period) {
        calendar.setTime(date);
        return calendar.get(period.period);
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    public static Date toDate(LocalDate date) {
        //local date + atStartOfDay() + default time zone + toInstant() = Date
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(String date) {
        if (StringUtils.isBlank(date)) {
            error(logger, "unable to get date from null or empty string");
            return null;
        }
        try {
            return format.parse(date);
        } catch (ParseException e) {
            error(logger, e, "unable to parse the date '%s' to format '%s'", date, format.toPattern());
        }
        return null;
    }

    public static Date min(Date min,@NonNull Date date) {
        return min != null && min.before(date) ? min : date;
    }

    public static Date max(Date max,@NonNull Date date) {
        return max != null && max.after(date) ? max : date;
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
