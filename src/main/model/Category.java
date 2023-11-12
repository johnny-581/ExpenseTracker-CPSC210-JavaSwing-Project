package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

// (Class Y) Represents a expense category with a list of Expenses in this category
public class Category implements Writable {
    private String label;
    private final List<Expense> expenses;
    private Color iconColor;

    public Category(String label) {
        this.label = label;
        this.expenses = new ArrayList<>();
        this.iconColor = generateRandomColor();
    }

    public String getLabel() {
        return label;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public Color getIconColor() {
        return iconColor;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setIconColor(Color iconColor) {
        this.iconColor = iconColor;
    }

    // EFFECTS: returns true if the category has the given label
    public boolean hasLabel(String label) {
        return Objects.equals(this.label, label);
    }

    // MODIFIES: this
    // EFFECTS: adds the given expense to this category
    public void addExpense(Expense e) {
        expenses.add(e);

        if (!this.equals(e.getCategory())) {
            e.setCategory(this);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes the given expense from this category
    public void removeExpense(Expense e) {
        expenses.remove(e);

        if (this.equals(e.getCategory())) {
            e.removeCategory();
        }
    }

    // EFFECTS: returns true if the given expense is in the category
    public boolean contains(Expense expense) {
        return expenses.contains(expense);
    }

    // EFFECTS: calculates and returns the total amount of all expenses in
    //          this category
    public double totalAmount() {
        double total = 0;

        for (Expense entry : expenses) {
            total += entry.getAmount();
        }

        return total;
    }

    // EFFECTS: returns a new random color
    private Color generateRandomColor() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        return new Color(red, green, blue);
    }



    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("label", label);
        json.put("expenses", expensesToJson());
        json.put("iconColor", createColorJsonObject(iconColor));
        return json;
    }

    // EFFECTS: returns expenses in this category as a JSON array
    private JSONArray expensesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Expense e : expenses) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }

    private JSONObject createColorJsonObject(Color color) {
        JSONObject colorJson = new JSONObject();

        colorJson.put("red", color.getRed());
        colorJson.put("green", color.getGreen());
        colorJson.put("blue", color.getBlue());

        return colorJson;
    }
}

