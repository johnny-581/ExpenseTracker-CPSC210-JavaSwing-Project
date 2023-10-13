package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Category {
    private String label;
    private List<Expense> expenses;

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

    public boolean hasLabel(String label) {
        return Objects.equals(this.label, label);
    }

    public void add(Expense e) {
        expenses.add(e);
    }

    public void remove(Expense e) {
        expenses.remove(e);
    }

    public double totalAmount() {
        double total = 0;

        for (Expense entry : expenses) {
            total += entry.getAmount();
        }

        return total;
    }
}

