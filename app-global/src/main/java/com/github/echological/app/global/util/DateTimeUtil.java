package com.github.echological.app.global.util;

import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import static com.github.echological.app.global.constant.FormatConstant.STD_DATE_FORMAT;

@UtilityClass
public class DateTimeUtil {

    public static Date addDay(Date targetDate, Integer day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(targetDate);
        calendar.add(Calendar.DAY_OF_YEAR, day);
        return calendar.getTime();
    }

    public static Date getCurrentDate(){
        String currentDateValue = new SimpleDateFormat(STD_DATE_FORMAT).format(new Date());
        try {
            return new SimpleDateFormat(STD_DATE_FORMAT).parse(currentDateValue);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String toString(String format, Date value){
        try {
            return new SimpleDateFormat(format).format(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        // Convert Date to Instant
        Instant instant = date.toInstant();

        // Convert Instant to LocalDateTime using system's default timezone
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static String getCurrentTimeMillisString() {
        return String.valueOf(getCurrentTimeMillis());
    }

    public static String getCurrentDateString() {
        return DateTimeFormatter.ofPattern(STD_DATE_FORMAT).format(LocalDateTime.now());
    }

    public static long getDateDifference(LocalDate date1, LocalDate date2) {
        return Math.abs(ChronoUnit.DAYS.between(date1, date2));
    }
}
