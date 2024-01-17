package com.supermarket.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Date {
    private int date;
    private int month;
    private int year;

    public Date(java.sql.Date date) {
    }

    public Date(Date date) {
        this.date = date.getDate();
        this.month = date.getMonth();
        this.year = date.getYear();
    }

    public Date(int date, int month, int year) {
        this.date = date;
        this.month = month;
        this.year = year;
    }

    public Date(java.util.Date date) {
        LocalDate localDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
        this.date = localDate.getDayOfMonth();
        this.month = localDate.getMonthValue();
        this.year = localDate.getYear();
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public static boolean isLeapYear(int year) {
        return year % 400 == 0 || (year % 100 != 0 && year % 4 == 0);
    }

    public static int numOfDays(int month, int year) {
        return switch (month) {
            case 2 -> isLeapYear(year) ? 29 : 28;
            case 1, 3, 5, 7, 8, 10, 12 -> 31;
            case 4, 6, 9, 11 -> 30;
            default -> 0;
        };
    }

    public static boolean isValidDay(int date, int month, int year) {
        return year >= 1000 && year <= 9999 &&
            month > 0 && month <= 12 &&
            date > 0 && date <= numOfDays(month, year);
    }

    public boolean isAfter(Date day) {
        if (this.year != day.year) {
            return this.year > day.year;
        } else if (this.month != day.month) {
            return this.month > day.month;
        } else {
            return this.date > day.date;
        }
    }

    public boolean isBefore(Date day) {
        if (this.year != day.year) {
            return this.year < day.year;
        } else if (this.month != day.month) {
            return this.month < day.month;
        } else {
            return this.date < day.date;
        }
    }

    public boolean isBetween(Date day1, Date day2) {
        return (isAfter(day1) || equals(day1)) && (isBefore(day2) || equals(day2));
    }

    public static Date parseDate(String str) throws Exception {
        int date, month, year;
        if (str.matches("^\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$")) {
            String[] temp = str.split("-");
            year = Integer.parseInt(temp[0]);
            month = Integer.parseInt(temp[1]);
            date = Integer.parseInt(temp[2]);
        } else if (str.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[0-2])/\\d{4}$")) {
            String[] temp = str.split("/");
            date = Integer.parseInt(temp[0]);
            month = Integer.parseInt(temp[1]);
            year = Integer.parseInt(temp[2]);
        } else {
            throw new Exception("Invalid day.");
        }
        if (!Date.isValidDay(date, month, year))
            throw new Exception("Invalid day.");
        return new Date(date, month, year);
    }




    public java.util.Date toJDate() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);
        return formatter.parse(date + "/" + month + "/" + year);
    }



    public java.util.Date toJDateSafe() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);
        try {
            return formatter.parse(date + "/" + month + "/" + year);
        } catch (ParseException ignored) {
            return new java.util.Date();
        }
    }

    public static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm:ss"));
    }

    public static String dateNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String toString() {
        // yyyy-MM-dd
        return String.format("%04d-%02d-%02d", year, month, date);
    }
}
