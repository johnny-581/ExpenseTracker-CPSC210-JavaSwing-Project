package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// (Class Y) Represents a expense category with a list of Expenses in this category
public class Category implements Writable {
    private String label;
    private final List<Expense> expenses;

    public Category(String label) {
        this.label = label;
        this.expenses = new ArrayList<>();
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    // EFFECTS: returns true if the category has the given label
    public boolean hasLabel(String label) {
        return Objects.equals(this.label, label);
    }

    // MODIFIES: this
    // EFFECTS: adds the given expense to this category
    public void add(Expense e) {
        expenses.add(e);
    }

    // MODIFIES: this
    // EFFECTS: removes the given expense from this category
    public void remove(Expense e) {
        expenses.remove(e);
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

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("label", label);
        json.put("expenses", expensesToJson());
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
}

