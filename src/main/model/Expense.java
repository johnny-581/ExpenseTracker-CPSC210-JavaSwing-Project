package model;

import org.json.JSONObject;
import persistence.Writable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// (Class X) Represents the record of an expense
public class Expense implements Writable {
    public static final String NAME_OF_NO_PLACE = "unknown place";
    public static final int ICON_DIAMETER = 10;

    private double amount;
    private LocalDateTime date;
    private String place;
    private Category category;

    public final Category categoryOfNoCategory;

    // EFFECTS: creates a new expense of the given amount with no place nor category
    //          and sets its date and time to now
    public Expense(Category categoryOfNoCategory, double amount) {
        this.amount = amount;
        this.date = LocalDateTime.now();
        this.place = NAME_OF_NO_PLACE;
        this.category = categoryOfNoCategory;
        categoryOfNoCategory.addExpense(this);
        this.categoryOfNoCategory = categoryOfNoCategory;
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

    public Category getCategory() {
        return category;
    }

    public Category getCONC() {
        return categoryOfNoCategory;
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

    // MODIFIES: this
    // EFFECTS: sets the expense as having no place
    public void setNoPlace() {
        setPlace(NAME_OF_NO_PLACE);
    }

    // MODIFIES: this
    // EFFECTS: replace the current category in the field with the given new category;
    //          also removes the expense from the old category and adds it to the new category
    public void setCategory(Category category) {
        Category oldCategory = this.category;
        this.category = category;

        if (oldCategory.contains(this)) {
            oldCategory.removeExpense(this);
        }

        if (!category.contains(this)) {
            category.addExpense(this);
        }
    }

    // MODIFIES: this
    // EFFECTS: replace the current category in the field with the categoryOfNoCategory;
    //          also removes the expense from its category
    public void removeCategory() {
        Category oldCategory = this.category;
        this.category = categoryOfNoCategory;

        if (oldCategory.contains(this)) {
            oldCategory.removeExpense(this);
        }

        if (!categoryOfNoCategory.contains(this)) {
            categoryOfNoCategory.addExpense(this);
        }
    }

    // EFFECTS: returns the period in days from the date of the expense to today
    public long getDaysPriorToToday() {
        LocalDate today = LocalDate.now();
        return ChronoUnit.DAYS.between(date.toLocalDate(), today);
    }

    // EFFECTS: returns a summarization of the Expense as a string
    public String getSummary() {
        long numDaysAgo = getDaysPriorToToday();
        String daysAgoMessage = numDaysAgo + " days ago";

        if (numDaysAgo == 0) {
            daysAgoMessage = "today";
        } if (numDaysAgo == 1) {
            daysAgoMessage = "yesterday";
        }

        LocalDate date = this.date.toLocalDate();
        return daysAgoMessage + " (" + date + ") you spent $"
                + amount + " at \"" + place + "\"" + " in the category \""
                + category.getLabel() + "\"";
    }

    // EFFECTS: returns a filled circle with a color representing the expense's category
    public ImageIcon getIcon() {
        BufferedImage icon = new BufferedImage(ICON_DIAMETER, ICON_DIAMETER, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();

        g2d.setColor(category.getIconColor());
        g2d.fill(new Ellipse2D.Double(0, 0, ICON_DIAMETER, ICON_DIAMETER));

        return new ImageIcon(icon);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("amount", amount);
        json.put("date", date);
        json.put("place", place);
        json.put("category", category.getLabel());
        return json;
    }
}
