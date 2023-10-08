package model;

import java.time.LocalDate;

public class SpendingEntry {
    private double amount;
    private LocalDate date;
    private String place;

    public SpendingEntry(double amount) {
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

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public int getDaysPriorToToday() {
        return date.until(LocalDate.now()).getDays();
    }
}
