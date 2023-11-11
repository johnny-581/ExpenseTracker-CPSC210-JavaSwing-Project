//package model;
//
//package model;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//import persistence.Writable;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import static model.Expense.LABEL_OF_NO_CATEGORY;
//
//// Represents an expense tracker with a list of expenses and a list of categories,
//// as well as a category that contains all expenses without a category.
//public class ExpenseTrackerNew implements Writable {
//    private final List<Expense> allExpenses;
//    private final List<Category> allCategories;
//    private final Category categoryOfNoCategory; // not in allCategories
//
//    // EFFECTS: constructs a new expenseTracker with no expenses nor categories recorded
//    public ExpenseTracker() {
//        allExpenses = new ArrayList<>();
//        allCategories = new ArrayList<>();
//        categoryOfNoCategory = new Category(LABEL_OF_NO_CATEGORY);
//    }
//
//    public List<Expense> getAllExpenses() {
//        return allExpenses;
//    }
//
//    public List<Category> getAllCategories() {
//        return allCategories;
//    }
//
//    // EFFECTS: given index i, returns the ith category in allCategory
//    public Category getCategoryAt(int index) {
//        return allCategories.get(index - 1);
//    }
//
//    // MODIFIES: this
//    // EFFECTS: adds the given expense, then sort all expenses
//    public void addExpense(Expense expense) {
//        allExpenses.add(expense);
//    }
//
//    // MODIFIES: this
//    // EFFECTS: adds the given category to the expense tracker
//    public void addCategory(Category category) {
//        allCategories.add(category);
//    }
//
//    // MODIFIES: this
//    // EFFECTS: adds the given expense to the given category
//    public void addExpenseToCategory(Expense expense, Category category) {
//        String label = category.getLabel();
//        expense.setCategory(label);
//        category.add(expense);
//    }
//
//    // MODIFIES: this
//    // EFFECTS: adds the given expense to expensesWithNoCategory
//    public void addExpenseToNoCategory(Expense expense) {
//        categoryOfNoCategory.add(expense);
//    }
//
//    // MODIFIES: this, expense
//    // EFFECTS: deletes the given expense
//    public void deleteExpense(Expense expense) {
//        allExpenses.remove(expense);
//        String categoryLabel = expense.getCategory();
//        Category category = getCategoryFromLabel(categoryLabel);
//        category.remove(expense);
//    }
//
//    // MODIFIES: this, category
//    // EFFECTS: deletes the given category
//    public void deleteCategory(Category category) {
//        allCategories.remove(category);
//
//        for (Expense e : category.getExpenses()) {
//            e.setNoCategory();
//        }
//    }
//
//    // EFFECTS: returns the category with the given label; returns categoryOfNoCategory
//    //          if there is no category with the given label
//    public Category getCategoryFromLabel(String label) {
//        Category found = categoryOfNoCategory;
//
//        for (Category c : allCategories) {
//            if (c.hasLabel(label)) {
//                found = c;
//            }
//        }
//
//        return found;
//    }
//
//    // EFFECTS: returns ture if no expenses are created yet (allExpenses is empty)
//    public boolean hasNoExpense() {
//        return allExpenses.isEmpty();
//    }
//
//    // EFFECTS: returns ture if no categories are created yet (allCategory is empty)
//    public boolean hasNoCategory() {
//        return allCategories.isEmpty();
//    }
//
//    // EFFECTS: returns true if a category with the given label already exists
//    public boolean categoryExists(String label) {
//        return getCategoryFromLabel(label) != categoryOfNoCategory;
//    }
//
//    // MODIFIES: this
//    // EFFECTS: changes all expenses with the old category label to having the new
//    //          label.
//    public void changeCategoryForExpenses(String oldLabel, String newLabel) {
//        for (Expense e : allExpenses) {
//            if (e.getCategory().equals(oldLabel)) {
//                e.setCategory(newLabel);
//            }
//        }
//    }
//
//    // REQUIRES: the given list of expenses is not empty
//    // MODIFIES: this
//    // EFFECTS: sorts the given list of expenses chronologically from most recent
//    //          to oldest
//    public void sortExpenses(List<Expense> expenses) {
//        expenses.sort(Comparator.comparing(Expense::getDate));
//        Collections.reverse(expenses);
//    }
//
//    // EFFECTS: calculates the percentage of money spent in the given category
//    public String calculatePercentage(Category c) {
//        double amountInCategory = c.totalAmount();
//        double totalAmount = calculateTotalAmount();
//        double ratio = amountInCategory / totalAmount;
//        DecimalFormat formatter = new DecimalFormat("0%");
//
//        return formatter.format(ratio);
//    }
//
//    // EFFECTS: calculates the total amount spent
//    public double calculateTotalAmount() {
//        double total = 0;
//
//        for (Expense e : allExpenses) {
//            total += e.getAmount();
//        }
//
//        return total;
//    }
//
//
//    @Override
//    public JSONObject toJson() {
//        JSONObject json = new JSONObject();
//        json.put("allExpenses", allExpensesToJson());
//        json.put("allCategories", allCategoriesToJson());
//        json.put("categoryOfNoCategory", categoryOfNoCategory.toJson());
//        return json;
//    }
//
//    // EFFECTS: returns allExpenses in this ExpenseTracker a JSON array
//    private JSONArray allExpensesToJson() {
//        JSONArray jsonArray = new JSONArray();
//
//        for (Expense e : allExpenses) {
//            jsonArray.put(e.toJson());
//        }
//
//        return jsonArray;
//    }
//
//    // EFFECTS: returns allCategories in this ExpenseTracker a JSON array
//    private JSONArray allCategoriesToJson() {
//        JSONArray jsonArray = new JSONArray();
//
//        for (Category c : allCategories) {
//            jsonArray.put(c.toJson());
//        }
//
//        return jsonArray;
//    }
//
//
//}
