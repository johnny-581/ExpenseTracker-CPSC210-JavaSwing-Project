package ui;

import model.Category;
import model.Expense;
import model.ExpenseTracker;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ExpenseTrackerApp {
    private static final String JSON_STORE = "./data/expenseTracker.json";
    private ExpenseTracker expenseTracker;
    private final Scanner input;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    // MODIFIES: this
    // EFFECTS: initiates the expense tracker application; creates example expenses and categories;
    //          and runs it
    public ExpenseTrackerApp() {
        expenseTracker = new ExpenseTracker();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        runExpenseTracker();
    }

    // This Method is based on the runTeller() method in the TellerAppExample
    // MODIFIES: this
    // EFFECTS: processes user input on main menu
    public void runExpenseTracker() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayMainMenu();
            command = input.next().toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                mainMenuHandleCommand(command);
            }
        }

        System.out.println("\nGoodbye");
    }

    // EFFECTS: displays main menu with options
    private void displayMainMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tn -> record new expense");
        System.out.println("\te -> see all expenses");
        System.out.println("\tc -> see all categories");
        System.out.println("\ts -> save expenses and categories to file");
        System.out.println("\tl -> load expenses and categories from file");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user input on main menu, provides actions for each command
    private void mainMenuHandleCommand(String command) {
        if (command.equals("n")) {
            newExpense();
        } else if (command.equals("e")) {
            if (expenseTracker.hasNoExpense()) {
                System.out.println("You have no expenses yet");
            } else {
                seeAllExpenses();
            }
        } else if (command.equals("c")) {
            if (expenseTracker.hasNoCategory()) {
                System.out.println("There are no categories yet");
            } else {
                seeAllCategories();
            }
        } else if (command.equals("s")) {
            saveExpenseTracker();
        } else if (command.equals("l")) {
            loadExpenseTracker();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new expense with amount, date, place, and category
    private void newExpense() {
        Expense expense = new Expense();
        expenseTracker.addExpense(expense);

        expenseSetAmount(expense);

        System.out.println("Do you want to add a place to this expense?");
        boolean yes = selectYesOrNo();
        if (yes) {
            expenseSetPlace(expense);
        } else {
            expense.setNoPlace();
        }

        System.out.println("Do you want to add this expense to a category?");
        boolean yes1 = selectYesOrNo();
        if (yes1) {
            expenseSetCategory(expense);
        } else {
            expense.setNoCategory();
            expenseTracker.addExpenseToNoCategory(expense);
        }

        System.out.println("\nGreat! You just recorded the following expense:");
        System.out.println("\n" + expense.getSummary());
    }

    // MODIFIES: this
    // EFFECTS: displays a list of all expenses and provides options to user
    private void seeAllExpenses() {
        System.out.println("Here is a list of all expenses:");
        List<Expense> allExpenses = expenseTracker.getAllExpenses();
        displayExpenses(allExpenses);
        modifyExpenses(allExpenses);
    }

    // MODIFIES: this
    // EFFECTS: displays a list of all categories and provides options to user
    private void seeAllCategories() {
        String selection = "";
        while (!(selection.equals("m") || selection.equals("n")
                || selection.equals("s"))) {
            displayCategories();
            System.out.println("\nm -> select one to modify");
            System.out.println("n -> create a new category");
            System.out.println("s -> display categorical expense statistics");
            selection = input.next().toLowerCase();

            switch (selection) {
                case "m":
                    modifyCategories();
                    break;
                case "n":
                    newCategory();
                    break;
                case "s":
                    displayCategoryStatistics();
                    break;
            }
        }
    }

    // MODIFIES: this, expenses
    // EFFECTS: let the user select an expense from the given list of expenses
    //          and provides options to modify the selected expense
    private void modifyExpenses(List<Expense> expenses) {
        Expense expense = selectExpense(expenses);
        System.out.println("You have selected the following expense:");
        System.out.println("\n" + expense.getSummary());

        String selection = "";
        while (!(selection.equals("f"))) {
            System.out.println("\na -> change its amount");
            System.out.println("d -> change its date");
            System.out.println("p -> change its place");
            System.out.println("c -> change its category");
            System.out.println("x -> delete it");
            System.out.println("f -> finish modification");

            selection = input.next().toLowerCase();
            selection = modifyExpensesHandleCommand(selection, expense);
        }
    }

    // MODIFIES: this, expense
    // EFFECTS: handles user selection for modifyExpenses()
    private String modifyExpensesHandleCommand(String selection, Expense expense) {
        if (selection.equals("a")) {
            expenseSetAmount(expense);
            selection = "";
            displaysModifiedExpense(expense);
        } else if (selection.equals("d")) {
            expenseSetDate(expense);
            selection = "";
            displaysModifiedExpense(expense);
        } else if (selection.equals("p")) {
            expenseSetPlace(expense);
            selection = "";
            displaysModifiedExpense(expense);
        } else if (selection.equals("c")) {
            expenseSetCategory(expense);
            selection = "";
            displaysModifiedExpense(expense);
        } else if (selection.equals("x")) {
            expenseTracker.deleteExpense(expense);
            System.out.println("Expense deleted");
            selection = "f";
        }
        return selection;
    }

    //EFFECTS: prints the summary of the modified expense
    private void displaysModifiedExpense(Expense expense) {
        System.out.println("The expense has been modified:");
        System.out.println("\n" + expense.getSummary());
    }

    // MODIFIES: this
    // EFFECTS: let the user select a category from all allCategories and provides options
    //          to modify the selected category
    private void modifyCategories() {
        Category category = selectCategory();

        String selection = "";
        while (!(selection.equals("f"))) {
            String categoryLabel = category.getLabel();
            System.out.println("\nYou have selected the category \""
                    + categoryLabel + "\"");

            System.out.println("\ne -> see a list of expenses in it");
            System.out.println("l -> change its label");
            System.out.println("x -> delete it");
            System.out.println("f -> finish modification");

            selection = input.next().toLowerCase();
            selection = modifyCategoriesHandleCommand(selection, category);
        }
    }

    // MODIFIES: this, category
    // EFFECTS: handles user selection for modifyCategories()
    private String modifyCategoriesHandleCommand(String selection, Category category) {
        if (selection.equals("e")) {
            seeExpensesInCategory(category, category.getLabel());
            selection = "";
        } else if (selection.equals("l")) {
            categorySetLabel(category);
            selection = "";
        } else if (selection.equals("x")) {
            expenseTracker.deleteCategory(category);
            System.out.println("Category deleted");
            selection = "f";
        }
        return selection;
    }

    // MODIFIES: this, category
    // EFFECTS: displays a list of expenses in the given category and provides option to
    //          modify those expenses
    private void seeExpensesInCategory(Category category, String categoryLabel) {
        String selection = "";
        while (!selection.equals("b")) {
            System.out.println("\nShowing expenses in the category \"" + categoryLabel + "\":");
            List<Expense> categoryExpenses = category.getExpenses();
            displayExpenses(categoryExpenses);

            System.out.println("\nm -> modify");
            System.out.println("b -> go back");
            selection = input.next().toLowerCase();

            if (selection.equals("m")) {
                modifyExpenses(categoryExpenses);
            }
        }
    }

    // EFFECTS: displays the percentage of money spent in each category
    private void displayCategoryStatistics() {
        System.out.println("\nHere is a statistic of how much you spent in each category:");

        List<Category> allCategories = expenseTracker.getAllCategories();
        for (Category c : allCategories) {
            String label = c.getLabel();
            String percentage = expenseTracker.calculatePercentage(c);

            System.out.println(" - " + label + ": " + percentage);
        }
    }

    // MODIFIES: this, expense
    // EFFECTS: let the user set the amount of the given expense
    private void expenseSetAmount(Expense expense) {
        System.out.println("Enter the amount you spent: $");
        double amount = input.nextDouble();
        expense.setAmount(amount);
    }

    // REQUIRES: date input can't be in the futures
    // MODIFIES: this, expense
    // EFFECTS: let the user set the date of the given expense, then sort all the
    //          expenses chronologically so that the expense is at the correct
    //          chronological position
    private void expenseSetDate(Expense expense) {
        System.out.println("Entre a date (YYYY-MM-DD): ");
        String date = input.next();
        expense.setDate(date);
    }

    // MODIFIES: this, expense
    // EFFECTS: let the user set the place of the given expense
    private void expenseSetPlace(Expense expense) {
        System.out.println("Entre the name of a place: ");
        String name = input.next().toLowerCase();
        expense.setPlace(name);
    }

    // MODIFIES: this, expense
    // EFFECTS: let the user set the category of the given expense
    private void expenseSetCategory(Expense expense) {
        if (expenseTracker.hasNoCategory()) {
            newCategory(expense);
        } else {
            chooseOrCreateNew(expense);
        }
    }

    // MODIFIES: this, category
    // EFFECTS: let the user set the label of the given category
    private void categorySetLabel(Category category) {
        System.out.println("enter the new label: ");
        String oldLabel = category.getLabel();
        String newLabel = input.next().toLowerCase();

        category.setLabel(newLabel);
        expenseTracker.changeCategoryForExpenses(oldLabel, newLabel);

        System.out.println("Label changed");
    }

    // MODIFIES: this, expense
    // EFFECTS: provides the user with options to choose from existing categories or to
    //          create a new category to assign to the given expense
    private void chooseOrCreateNew(Expense expense) {
        String selection = "";

        while (!(selection.equals("e") || selection.equals("n"))) {
            System.out.println("e -> choose from existing categories");
            System.out.println("n -> create a new category");
            selection = input.next().toLowerCase();

            if (selection.equals("e")) {
                chooseFromExistingCategories(expense);
            } else if (selection.equals("n")) {
                newCategory(expense);
            }
        }
    }

    // MODIFIES: this, expense
    // EFFECTS: let the user choose an existing category to assign to the given expense
    private void chooseFromExistingCategories(Expense expense) {
        displayCategories();
        Category selected = selectCategory();
        expenseTracker.addExpenseToCategory(expense, selected);
    }

    // MODIFIES: this, expense
    // EFFECTS: lets the user create a new category to assign to the given expense
    private void newCategory(Expense expense) {
        System.out.println("Entre the label of a new Category: ");

        String label = input.next().toLowerCase();
        if (expenseTracker.categoryExists(label)) {
            System.out.println("This category already exists");
            newCategory(expense);
        } else {
            Category category = new Category(label);
            expenseTracker.addCategory(category);
            expenseTracker.addExpenseToCategory(expense, category);
        }
    }

    // MODIFIES: this
    // EFFECTS: lets the user create a new empty category
    private void newCategory() {
        System.out.println("Entre the label a new Category: ");

        String label = input.next().toLowerCase();
        if (expenseTracker.categoryExists(label)) {
            System.out.println("This category already exists");
            newCategory();
        } else {
            Category category = new Category(label);
            expenseTracker.addCategory(category);
        }
        System.out.println("\nNew category created");
    }

    // REQUIRES: input must be an integer in range
    // EFFECTS: let the user select an expense by entering an index
    private Expense selectExpense(List<Expense> expenses) {
        System.out.println("\nSelect an expense by entering the number in front of it: ");
        int index = input.nextInt();

        return expenses.get(index - 1);
    }

    // REQUIRES: input must be an integer in range
    // EFFECTS: let the user select a category by entering an index
    private Category selectCategory() {
        System.out.println("Select a category by entering the number in front of it: ");
        int index = input.nextInt();

        return expenseTracker.getCategoryAt(index);
    }

    // EFFECTS: returns ture if selection is yes, false if selection is no
    private boolean selectYesOrNo() {
        String selection = "";

        while (!(selection.equals("y") || selection.equals("n"))) {
            System.out.println("y -> yes");
            System.out.println("n -> no");
            selection = input.next().toLowerCase();
        }

        return selection.equals("y");
    }

    // EFFECTS: displays a numbered list of the date, amount, place, and category
    //          of all Expenses in the given list of Expenses in chronological order
    //          from most recent to oldest
    private void displayExpenses(List<Expense> expenses) {
        int i = 1;
        expenseTracker.sortExpenses(expenses);
        for (Expense e : expenses) {
            String message = e.getSummary();
            System.out.println("(" + i + ") " + message);
            i++;
        }
    }

    // EFFECTS: displays a numbered list of the labels of all Categories
    private void displayCategories() {
        System.out.println("Here is a list of all categories:");
        List<Category> allCategories = expenseTracker.getAllCategories();

        int i = 1;
        for (Category c : allCategories) {
            System.out.println("(" + i + ") " + c.getLabel());
            i++;
        }
    }

    // EFFECTS: saves the expense tracker to file
    private void saveExpenseTracker() {
        try {
            jsonWriter.open();
            jsonWriter.write(expenseTracker);
            jsonWriter.close();
            System.out.println("Saved your expenses and categories to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads expense tracker from file
    private void loadExpenseTracker() {
        try {
            expenseTracker = jsonReader.read();
            System.out.println("Loaded your expenses and categories from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
