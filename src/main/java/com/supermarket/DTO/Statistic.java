package com.supermarket.DTO;
import com.supermarket.utils.Date;
public class Statistic {
    private int id;
    private Date date;
    private double  amount;
    private double  expenses;
    private boolean deleted;
    public Statistic() {
    }
    public Statistic(int id, Date date, double amount, double expenses, boolean deleted) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.expenses = expenses;
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public String toString() {
        return id + " | " +
            date + " | " +
            amount + " | " +
            expenses ;
    }
}
