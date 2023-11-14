package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


// Represents an expense tracker with a list of expenses and a list of categories,
// as well as a category that contains all expenses without a category.
public class ExpenseTracker implements Writable {
    public static final String LABEL_OF_NO_CATEGORY = "others";
    private final List<Expense> allExpenses;
    private final List<Category> allCategories;

    // EFFECTS: constructs a new expenseTracker with no expenses added; a categoryOfNoCategory
    //          is added as the first item in allCategories, which will always stay at this position
    public ExpenseTracker() {
        allExpenses = new ArrayList<>();
        allCategories = new ArrayList<>();
        Category categoryOfNoCategory = new Category(LABEL_OF_NO_CATEGORY);
        addCategory(categoryOfNoCategory);
    }

    public List<Expense> getAllExpenses() {
        return allExpenses;
    }

    public List<Category> getAllCategories() {
        return allCategories;
    }

    public Category getCONC() {
        return allCategories.get(0);
    }

    // EFFECTS: given index i, returns the ith category in allCategory
    public Category getCategoryAt(int index) {
        return allCategories.get(index - 1);
    }

    // MODIFIES: this
    // EFFECTS: adds the given expense to the expense tracker, then sort all expenses
    //          chronologically from most recent to oldest
    public void addExpense(Expense expense) {
        allExpenses.add(expense);
        allExpenses.sort(Comparator.comparing(Expense::getDate));
        Collections.reverse(allExpenses);
    }

    // MODIFIES: this
    // EFFECTS: adds the given category to the expense tracker
    public void addCategory(Category category) {
        allCategories.add(category);
    }

    // MODIFIES: this, expense
    // EFFECTS: deletes the expense at the given index
    public void deleteExpense(int index) {
        Expense expense = allExpenses.get(index);
        allExpenses.remove(index);

        Category category = expense.getCategory();
        category.removeExpense(expense);

        if (category.getExpenses().isEmpty() && !category.equals(getCONC())) {
            allCategories.remove(category);
        }
    }

    // MODIFIES: this, category
    // EFFECTS: deletes the given category if it is not the category of no category
    public void deleteCategory(Category category) {
        allCategories.remove(category);

        for (Expense e : category.getExpenses()) {
            e.removeCategory();
        }
    }

    // EFFECTS: returns ture if no expenses are created yet (allExpenses is empty)
    public boolean hasNoExpense() {
        return allExpenses.isEmpty();
    }

    // EFFECTS: returns ture if there is no categories other than the categoryOfNoCategory
    public boolean hasNoCategory() {
        return allCategories.size() <= 1;
    }

    // EFFECTS: returns true if a category with the given label already exists
    public boolean categoryExists(String label) {
        return getCategoryFromLabel(label) != null;
    }

    // EFFECTS: returns the category with the given label; returns null
    //          if there is no category with the given label
    public Category getCategoryFromLabel(String label) {
        Category found = null;

        for (Category c : allCategories) {
            if (c.hasLabel(label)) {
                found = c;
            }
        }

        return found;
    }

    // EFFECTS: calculates the percentage of money spent in the given category
    public String calculatePercentage(Category c) {
        double amountInCategory = c.totalAmount();
        double totalAmount = calculateTotalAmount();
        double ratio = amountInCategory / totalAmount;
        DecimalFormat formatter = new DecimalFormat("0%");

        return formatter.format(ratio);
    }

    // EFFECTS: calculates the total amount spent
    public double calculateTotalAmount() {
        double total = 0;

        for (Expense e : allExpenses) {
            total += e.getAmount();
        }

        return total;
    }


    // EFFECTS: returns the expense tracker as a json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("allExpenses", allExpensesToJson());
        json.put("allCategories", allCategoriesToJson());
        return json;
    }

    // EFFECTS: returns allExpenses in this ExpenseTracker as a JSON array
    private JSONArray allExpensesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Expense e : allExpenses) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: returns allCategories in this ExpenseTracker as a JSON array
    private JSONArray allCategoriesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Category c : allCategories) {
            jsonArray.put(c.toJson());
        }

        return jsonArray;
    }


}
