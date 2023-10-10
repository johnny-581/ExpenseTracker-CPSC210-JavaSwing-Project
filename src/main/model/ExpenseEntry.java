package model;

import java.time.LocalDate;

public class ExpenseEntry {
    private double amount;
    private LocalDate date;
    private String place;
    private String category;

    public ExpenseEntry(double amount) {
        this.amount = amount;
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

    public int getDaysPriorToToday() {
        return date.until(LocalDate.now()).getDays();
    }
}
