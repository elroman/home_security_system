package service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static String getStringFromDate(Date date) {
        if (date != null) {
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            return dateFormatter.format(date);
        }
        return null;
    }

    public static Date getDateFromDateString(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getStringFromDateTime(Date date) {
        if (date != null) {
            DateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            return dateTimeFormatter.format(date);
        }
        return null;
    }

    public static Date getDateFromDateTimeString(String dateTimeString) {
        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return dateTimeFormat.parse(dateTimeString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getDateFromDateTimeMillisString(String dateTimeMillisString) {
        try {
            SimpleDateFormat dateTimeMillisFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            dateTimeMillisFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return dateTimeMillisFormat.parse(dateTimeMillisString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getDateFromString(String dateString) {
        if (isValidDateStr(dateString)) {
            return getDateFromDateString(dateString);
        }
        if (isValidDateTimeStr(dateString)) {
            return getDateFromDateTimeString(dateString);
        }
        if (isValidDateTimeMillisStr(dateString)) {
            return getDateFromDateTimeMillisString(dateString);
        }
        return null;
    }

    public static String getDateTimeMillisStringFromTimestamp(long timestampMillis) {
        return getDateTimeMillisStringFromTimestamp(new Date(timestampMillis));
    }

    public static String getDateTimeMillisStringFromTimestamp(Date date) {
        SimpleDateFormat dateTimeMillisFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        dateTimeMillisFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateTimeMillisFormat.format(date);
    }

    // matches with XXXX-XX-XX
    public static boolean isValidDateStr(String dateStr) {
        return dateStr != null && dateStr.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    // matches with XXXX-XX-XX XX:XX:XX
    public static boolean isValidDateTimeStr(String dateTimeStr) {
        return dateTimeStr != null && dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    }

    // matches with XXXX-XX-XX XX:XX:XX.XXX
    public static boolean isValidDateTimeMillisStr(String dateTimeStr) {
        return dateTimeStr != null && dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3}");
    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        return calendar.getTime();
    }

    public static Date getOffsetDate(Date fromDate, int amount, int calendarField) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);
        calendar.add(calendarField, amount);
        return calendar.getTime();
    }
}
