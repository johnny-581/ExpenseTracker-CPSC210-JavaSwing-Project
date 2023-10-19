package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// (Class Y) Represents a expense category with a list of Expenses in this category
public class Category {
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
    // EFFECTS: adds the given expense to this category at the given index
    public void add(int index, Expense e) {
        expenses.add(index, e);
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
}

