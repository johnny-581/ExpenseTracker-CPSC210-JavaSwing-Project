package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// (Class X) Represents the record of an expense
public class Expense {
    public static final String LABEL_OF_NO_CATEGORY = "none"; // ask TA about visibility
    public static final String NAME_OF_NO_PLACE = "unknown place";

    private double amount;
    private LocalDateTime date;
    private String place;
    private String category;

    // EFFECTS: creates a new expense of $0 and sets its date to today
    public Expense() {
        this.amount = 0;
        this.date = LocalDateTime.now();
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // REQUIRES: date can't be in the future
    public void setDate(String date) {
        this.date = LocalDate.parse(date).atStartOfDay();
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // MODIFIES: this
    // EFFECTS: sets the expense as having no place
    public void setNoPlace() {
        setPlace(NAME_OF_NO_PLACE);
    }

    // MODIFIES: this
    // EFFECTS: sets the expense as having no category
    public void setNoCategory() {
        setCategory(LABEL_OF_NO_CATEGORY);
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
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
        return ChronoUnit.DAYS.between(date.toLocalDate(), today);
    }

    // EFFECTS: returns a summarization of the Expense as a string
    public String getSummary() {
        long numDaysAgo = getDaysPriorToToday();
        String daysAgoMessage = numDaysAgo + " day(s) ago";

        if (numDaysAgo == 0) {
            daysAgoMessage = "today";
        }

        LocalDate date = this.date.toLocalDate();
        return daysAgoMessage + " (" + date + ") you spent $"
                + amount + " at \"" + place + "\" in the category \""
                + category + "\"";
    }
}
