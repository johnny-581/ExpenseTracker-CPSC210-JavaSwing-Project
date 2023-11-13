package persistence;

import Exceptions.InvalidDateException;
import model.Category;
import model.Expense;
import model.ExpenseTracker;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Adapted from JsonSerializationDemo
// Represents a reader that reads ExpenseTracker from JSON data stored in file
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads ExpenseTracker from file and returns it;
    //          throws IOException if an error occurs reading data from file
    public ExpenseTracker read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseExpenseTracker(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses ExpenseTracker from JSON object and returns it
    private ExpenseTracker parseExpenseTracker(JSONObject jsonObject) {
        ExpenseTracker et = new ExpenseTracker();
        et.deleteCategory(et.getCONC());
        JSONArray jsonCategories = jsonObject.getJSONArray("allCategories");
        JSONArray jsonExpenses = jsonObject.getJSONArray("allExpenses");
        addAllCategories(et, jsonCategories);
        addAllExpenses(et, jsonExpenses);
        return et;
    }

    // MODIFIES: et
    // EFFECTS: parses allCategories from JSON object and adds them to expense tracker
    private void addAllCategories(ExpenseTracker et, JSONArray jsonCategories) {
        for (Object json : jsonCategories) {
            JSONObject jsonCategory = (JSONObject) json;
            addCategory(et, jsonCategory);
        }
    }

    // MODIFIES: et
    // EFFECTS: parses category from JSON array and adds it to expense tracker
    private void addCategory(ExpenseTracker et, JSONObject jsonCategory) {
        String label = jsonCategory.getString("label");
        Color iconColor = parseColor(jsonCategory);
        Category category = new Category(label);
        category.setIconColor(iconColor);
        et.addCategory(category);
    }

    private Color parseColor(JSONObject jsonCategory) {
        JSONObject jsonColor = jsonCategory.getJSONObject("iconColor");
        int red = jsonColor.getInt("red");
        int green = jsonColor.getInt("green");
        int blue = jsonColor.getInt("blue");

        return new Color(red, green, blue);
    }

    // MODIFIES: et
    // EFFECTS: parses allExpenses from JSON array and adds them to expense tracker
    private void addAllExpenses(ExpenseTracker et, JSONArray jsonExpenses) {
        for (Object json : jsonExpenses) {
            JSONObject jsonExpense = (JSONObject) json;
            addExpense(et, jsonExpense);
        }
    }

    // MODIFIES: et
    // EFFECTS: parses expense from JSON object and adds it to expense tracker
    private void addExpense(ExpenseTracker et, JSONObject jsonExpense) {
        double amount = jsonExpense.getDouble("amount");
        String date = jsonExpense.getString("date").substring(0, 10);
        String place = jsonExpense.getString("place");
        String categoryLabel = jsonExpense.getString("category");
        Category category = et.getCategoryFromLabel(categoryLabel);

        Expense expense = new Expense(et.getCONC(), amount);
        expense.setDate(date);
        expense.setPlace(place);
        expense.setCategory(category);
        et.addExpense(expense);
    }
}