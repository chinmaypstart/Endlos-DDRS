/**
 *
 */
package com.endlosiot.common.util;

import com.endlosiot.common.logger.LoggerService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * All Date related operation will be performed in this class.
 *
 * @author Dhruvang
 */
public class DateUtility {

    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";
    public static final String MM_DD_YYYY = "MM/dd/yyyy";
    public static final String DD_MM_YY = "dd/MM/yy";
    public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yy hh:mm:ss";
    public static final String HH_MM_A = "hh:mm a";

    private static final Map<Long, String> monthsMap = new HashMap<>();
    private static final Map<Long, String> yearsmap = new HashMap<>();

    private DateUtility() {
    }

    /**
     * To get the local date & time from epoch.
     *
     * @param date
     * @return
     */
    public static LocalDate stringToDateForSpecificFormat(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
            return LocalDate.parse(date, formatter);
        } catch (Exception exception) {
            LoggerService.exception(exception);
            return null;
        }
    }

    /**
     * To format the locate date and time according to the format.
     *
     * @param localDateTime
     * @param format
     * @return
     */
    public static String formateLocalDateTime(LocalDateTime localDateTime, String format) {
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * Get current epoch.
     *
     * @return
     */
    public static long getCurrentEpoch() {
        return Instant.now().getEpochSecond();
    }

    /**
     * To format the string date into localdate.
     *
     * @param date
     * @param format - dd/MM/yyyy
     * @return
     */
    public static LocalDate stringToLocalDate(String date, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(date, formatter);
        } catch (Exception exception) {
            LoggerService.exception(exception);
            return null;
        }
    }

    /**
     * To format the string date & time to localdatetime.
     *
     * @param date
     * @param format - dd/MM/yyyy HH:mm:ss
     * @return
     */
    public static LocalDateTime stringToDateTime(String date, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(date, formatter);
        } catch (Exception exception) {
            LoggerService.exception(exception);
            return null;
        }
    }

    /**
     * To get the current year
     *
     * @return
     */
    public static int getCurrentYear() {
        return LocalDateTime.now().getYear();
    }

    /**
     * To get the current month.
     *
     * @return
     */
    public static int getCurrentMonth() {
        return LocalDateTime.now().getMonth().getValue();
    }

    /**
     * To get current month name
     *
     * @return
     */
    public static String getCurrentMonthName() {
        return LocalDateTime.now().getMonth().name();
    }

    /**
     * To get current day.
     *
     * @return
     */
    public static int getCurrentDay() {
        return LocalDateTime.now().getDayOfMonth();
    }

    /**
     * This method is used to format local date.
     *
     * @param localDate
     * @param format
     * @return
     */
    public static String formateLocalDate(LocalDate localDate, String format) {
        return localDate.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * This methdo is used to get a number days in a month.
     *
     * @param year
     * @param month
     * @return
     */
    public static Integer getDaysInMonth(Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }

    /**
     * To format the string time to localtime.
     *
     * @param time
     * @param format hh:mm a
     * @return
     */
    public static LocalTime stringToTime(String time, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalTime.parse(time, formatter);
        } catch (Exception exception) {
            LoggerService.exception(exception);
            return null;
        }
    }

    /**
     * This method is used to get a epoch after specified period using Unit of time.
     */
    public static Instant getEpochAfterSpecificPeriod(Long startEpoch, ChronoUnit chronoUnit, long period) {
        return Instant.ofEpochSecond(startEpoch).plus(period, chronoUnit);
    }

    /**
     * This method is used to get a epoch after specified period using Unit of time.
     */
    public static Instant getEpochBeforeSpecificPeriod(Long startEpoch, ChronoUnit chronoUnit, long period) {
        return Instant.ofEpochSecond(startEpoch).minus(period, chronoUnit);
    }

    /**
     * Find a days between two time instance
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static long getPeriodDifferenceBetweenDates(Instant fromDate, Instant toDate, ChronoUnit chronoUnit) {
        return chronoUnit.between(fromDate, toDate);
    }

    /**
     * This method is used to covert string to Date format.
     *
     * @param date
     * @param format
     * @return
     */
    public static Date stringToDateFormat(String date, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(date);
        } catch (Exception exception) {
            LoggerService.exception(exception);
            return null;
        }
    }

    /**
     * parse datetime to date.
     *
     * @param date
     * @param format
     * @return
     */
    public static String parseDateTimeToDate(String date, String parseFormat, String format) {
        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat(parseFormat);
            Date dateParse = dateTimeFormat.parse(date);

            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(dateParse);
        } catch (Exception exception) {
            LoggerService.exception(exception);
            return null;
        }
    }

    /**
     * check first date is after second date from epoch
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static boolean isAfterDate(Long firstDate, Long secondDate) {
        LocalDate firstLocalDate = Instant.ofEpochSecond(firstDate).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate secondLocalDate = Instant.ofEpochSecond(secondDate).atZone(ZoneId.systemDefault()).toLocalDate();
        return firstLocalDate.isAfter(secondLocalDate);
    }

    /**
     * check first date is after second date from epoch
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static boolean isBeforeDateTime(Long firstDate, Long secondDate) {
        LocalDateTime firstLocalDateTime = Instant.ofEpochSecond(firstDate).atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime secondLocalDateTime = Instant.ofEpochSecond(secondDate).atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return firstLocalDateTime.isBefore(secondLocalDateTime);
    }

    /**
     * this method is used for set months.
     *
     * @return
     */
    public static Map<Long, String> setMonth() {
        for (int i = 1; i <= 12; i++) {
            monthsMap.put((long) i, Month.of(i).getDisplayName(TextStyle.FULL, Locale.US));
        }
        return monthsMap;
    }

    /**
     * this method is used for year months.
     *
     * @return
     */
    public static Map<Long, String> setYear() {
        List<Integer> range = IntStream.rangeClosed(1900, Calendar.getInstance().get(Calendar.YEAR)).boxed()
                .collect(Collectors.toList());
        Collections.sort(range, Collections.reverseOrder());
        for (Integer year : range) {
            yearsmap.put(Long.valueOf(year), year.toString());
        }
        return yearsmap;
    }

    /**
     * this method is used for get month by key.
     *
     * @param key
     * @return
     */
    public static Long getMonth(Long key) {
        try {
            setMonth();
            return Long.valueOf(Month.valueOf(monthsMap.get(key).toUpperCase()).getValue());
        } catch (Exception exception) {
            LoggerService.exception(exception);
            return null;
        }
    }

    /**
     * this method is used for get month name by key.
     *
     * @param key
     * @return
     */
    public static String getMonthName(Long key) {
        try {
            setMonth();
            return monthsMap.get(key);
        } catch (Exception exception) {
            LoggerService.exception(exception);
            return null;
        }
    }

    /**
     * this method is used for get year by key.
     *
     * @param key
     * @return
     */
    public static Long getyear(Long key) {
        try {
            setYear();
            return Long.valueOf(yearsmap.get(key));
        } catch (Exception exception) {
            LoggerService.exception(exception);
            return null;
        }
    }

    /**
     * To format the epoch to localdatetime.
     *
     * @param epoch
     * @param format - dd/MM/yyyy HH:mm:ss
     * @return
     */
    public static String epochToDateTime(Long epoch, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return Instant.ofEpochSecond(epoch).atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatter);
        } catch (Exception exception) {
            LoggerService.exception(exception);
            return null;
        }
    }

    /**
     * To get the local date from epoch.
     *
     * @param date
     * @return
     */
    public static LocalDate getLocalDate(Long date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault());
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toLocalDate();
    }

    /**
     * Get is reset password valid minutes.
     *
     * @return
     */
    public static boolean isResetPasswordValidMinutes(Long epoch, Long min) {
        Instant epochInstant = Instant.ofEpochSecond(epoch);
        Instant addMinToEpoch = epochInstant.plus(min, ChronoUnit.MINUTES);
        return addMinToEpoch.isBefore(Instant.now());
    }

    /**
     * To get epoch from local date.
     *
     * @param date
     * @return
     */
    public static Long dateEpoch(String date) {
        String dates = DateUtility.parseDateTimeToDate(date, "yyyy-MM-dd", DateUtility.DD_MM_YYYY);
        try {
            SimpleDateFormat df = new SimpleDateFormat(DateUtility.DD_MM_YYYY);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            return ((df.parse(dates).getTime()) / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long getStartEpoch(LocalDate localDate) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        LocalDateTime startOfDay = localDate.atStartOfDay();
        long startEpoch = startOfDay.atZone(timeZone.toZoneId()).toEpochSecond();
        return startEpoch;
    }

    public static Long getTodayStartEpoch() {
        return (LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli())/1000;
    }

    public static Long getEndEpoch(LocalDate localDate) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        long endEpoch = endOfDay.atZone(timeZone.toZoneId()).toEpochSecond();
        return endEpoch;
    }
    public static LocalDate getEpochTodate(long epochSeconds) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        ZoneId zoneId = timeZone.toZoneId();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), zoneId);
        return localDateTime.toLocalDate();
    }
}