package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// (Class X) Represents the record of an expense
public class Expense {
    private double amount;
    private LocalDate date;
    private String place;
    private String category;

    // EFFECTS: creates a new expense of $0 and sets its date to today
    public Expense() {
        this.amount = 0;
        this.date = LocalDate.now();
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public String getCategory() {
        return category;
    }

    // EFFECTS: returns the period in days from the date of the expense to today
    public long getDaysPriorToToday() {
        LocalDate today = LocalDate.now();
        return ChronoUnit.DAYS.between(date, today);
    }
}
