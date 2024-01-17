package com.supermarket.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTime {
    private LocalDateTime dateTime;

    public static DateTime MIN = new DateTime();
    public static DateTime MAX = new DateTime();
    static {
        MIN.dateTime = LocalDateTime.of(1000, 1, 1, 0, 0, 0, 0);
        MAX.dateTime = LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999999);
    }

    public DateTime(LocalDateTime localDateTime) {
        if (!isValidDateTime(localDateTime))
            throw new IllegalArgumentException("Invalid date and time");
        this.dateTime = localDateTime;
    }

    public DateTime() {
        this.dateTime = LocalDateTime.now();
    }

    public DateTime(int y, int M, int d, int h, int m, int s, int ns) {
        this.dateTime = LocalDateTime.of(y, M, d, h, m, s, ns);
    }

    public int getYear() {
        return this.dateTime.getYear();
    }

    public void setYear(int year) {
        this.dateTime = this.dateTime.withYear(year);
    }

    public int getMonth() {
        return this.dateTime.getMonthValue();
    }

    public void setMonth(int month) {
        this.dateTime = this.dateTime.withMonth(month);
    }

    public int getDate() {
        return this.dateTime.getDayOfMonth();
    }

    public void setDate(int date) {
        this.dateTime = this.dateTime.withDayOfMonth(date);
    }

    public int getHour() {
        return this.dateTime.getHour();
    }

    public void setHour(int hour) {
        this.dateTime = this.dateTime.withHour(hour);
    }

    public int getMinute() {
        return this.dateTime.getMinute();
    }

    public void setMinute(int minute) {
        this.dateTime = this.dateTime.withMinute(minute);
    }

    public int getSecond() {
        return this.dateTime.getSecond();
    }

    public void setSecond(int second) {
        this.dateTime = this.dateTime.withSecond(second);
    }

    public int getNano() {
        return this.dateTime.getNano();
    }

    public void setNano(int nano) {
        this.dateTime = this.dateTime.withNano(nano);
    }

    public static DateTime parseDateTime(String str) {
        if (str.contains("T"))
            str = str.replace("T", " ");
        if (str.matches("^\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01]) (\\d{2}:\\d{2}(?::\\d{2}(\\.\\d{1,6})?)?)?$")) {
            if (str.contains(".")) {
                return parse(str, "yyyy-MM-dd HH:mm:ss.SSSSSS");
            } else if (str.contains(":")) {
                return parse(str, "yyyy-MM-dd HH:mm:ss");
            } else {
                return parse(str, "yyyy-MM-dd");
            }
        }
        if (str.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[0-2])/\\d{4} (\\d{2}:\\d{2}(?::\\d{2}(\\.\\d{1,6})?)?)?$")) {
            if (str.contains(".")) {
                return parse(str, "dd/MM/yyyy HH:mm:ss.SSSSSS");
            } else if (str.contains(":")) {
                return parse(str, "dd/MM/yyyy HH:mm:ss");
            } else {
                return parse(str, "dd/MM/yyyy");
            }
        }
        throw new IllegalArgumentException("Invalid date and time");
    }

    public static DateTime parse(String text, String pattern) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern));
            if (!isValidDateTime(localDateTime))
                throw new IllegalArgumentException("Invalid date and time");
            return new DateTime(localDateTime);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date and time");
        }
    }

    public static boolean isValidDateTime(LocalDateTime localDateTime) {
        return !localDateTime.isBefore(MIN.dateTime) && !localDateTime.isAfter(MAX.dateTime);
    }

    public static long calculateTime(DateTime dateTime1, DateTime dateTime2) {
        return Duration.between(LocalDateTime.of(dateTime2.getYear(), dateTime1.getMonth(), dateTime1.getDate(), dateTime1.getHour(), dateTime1.getMinute(), dateTime1.getSecond()), LocalDateTime.of(dateTime2.getYear(), dateTime2.getMonth(), dateTime2.getDate(), dateTime2.getHour(), dateTime2.getMinute(), dateTime2.getSecond())).toSeconds();
    }

    public String toSQL() {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS"));
    }

    @Override
    public String toString() {
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSSSSS"));
    }
}
