package ui;

import model.Category;
import model.Expense;

import java.text.DecimalFormat;
import java.util.List;

public class CategoryStatistics {
    List<Category> allCategories;
    List<Expense> allExpenses;

    public CategoryStatistics(List<Category> allCategories, List<Expense> allExpenses) {
        this.allCategories = allCategories;
        this.allExpenses = allExpenses;
        display();
    }

    private void display() {
        System.out.println("\nHere is a statistic of how much you spent in each category:");

        for (Category c : allCategories) {
            String label = c.getLabel();
            String percentage = calculatePercentage(c);

            System.out.println(" - " + label + ": " + percentage);
        }
    }

    private String calculatePercentage(Category c) {
        double amountInCategory = c.totalAmount();
        double totalAmount = calculateTotalAmount();
        double ratio = amountInCategory / totalAmount;
        DecimalFormat formatter = new DecimalFormat("0%");

        return formatter.format(ratio);
    }

    private double calculateTotalAmount() {
        double total = 0;

        for (Expense e : allExpenses) {
            total += e.getAmount();
        }

        return total;
    }
}
