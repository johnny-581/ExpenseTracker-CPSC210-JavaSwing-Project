package ui;

import model.Category;
import model.Expense;

import java.text.DecimalFormat;
import java.util.List;

// A display of the percentage of money spent in each category
public class CategoryStatistics {
    List<Category> allCategories;
    List<Expense> allExpenses;

    // EFFECTS: initiates a CategoryStatistics and displays the statistic
    public CategoryStatistics(List<Category> allCategories, List<Expense> allExpenses) {
        this.allCategories = allCategories;
        this.allExpenses = allExpenses;
        display();
    }

    // EFFECTS: displays the percentage of money spent in each category
    private void display() {
        System.out.println("\nHere is a statistic of how much you spent in each category:");

        for (Category c : allCategories) {
            String label = c.getLabel();
            String percentage = calculatePercentage(c);

            System.out.println(" - " + label + ": " + percentage);
        }
    }

    // EFFECTS: calculates the percentage of money spent in the given category
    private String calculatePercentage(Category c) {
        double amountInCategory = c.totalAmount();
        double totalAmount = calculateTotalAmount();
        double ratio = amountInCategory / totalAmount;
        DecimalFormat formatter = new DecimalFormat("0%");

        return formatter.format(ratio);
    }

    // EFFECTS: calculates the total amount in allCategories
    private double calculateTotalAmount() {
        double total = 0;

        for (Expense e : allExpenses) {
            total += e.getAmount();
        }

        return total;
    }
}
