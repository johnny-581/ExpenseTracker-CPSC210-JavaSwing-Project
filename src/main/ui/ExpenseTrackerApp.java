package ui;

import model.Category;
import model.Expense;

import java.util.*;

public class ExpenseTrackerApp {
    private static final String LABEL_OF_NO_CATEGORY = "none";
    private static final String NAME_OF_NO_PLACE = "unknown";

    private List<Expense> allExpenses;
    private List<Category> allCategories;
    private Category expensesWithoutCategory;
    private Scanner input;

    // MODIFIES: this
    // EFFECTS: initiates the expense tracker application; creates example expenses and categories;
    //          and runs it
    public ExpenseTrackerApp() {
        allExpenses = new ArrayList<>();
        allCategories = new ArrayList<>();
        expensesWithoutCategory = new Category(LABEL_OF_NO_CATEGORY); // not in allCategories
        input = new Scanner(System.in);

        new CreateExamples(allExpenses, allCategories); // can be removed
        runExpenseTracker();
    }

    // EFFECTS: displays main menu with options
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tn -> record new expense");
        System.out.println("\te -> see all expenses");
        System.out.println("\tc -> see all expense categories");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user input on main menu
    public void runExpenseTracker() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayMenu();
            command = input.next().toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye");
    }

    // MODIFIES: this
    // EFFECTS: processes user input on main menu, provides actions for each command
    private void processCommand(String command) {
        switch (command) {
            case "n":
                newExpense();
                break;
            case "e":
                if (allExpenses.isEmpty()) {
                    System.out.println("You have no expenses yet");
                } else {
                    seeAllExpenses();
                }
                break;
            case "c":
                if (allCategories.isEmpty()) {
                    System.out.println("There are no categories yet");
                } else {
                    seeAllCategories();
                }
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new expense with amount, date, place, and category
    private void newExpense() {
        Expense expense = new Expense();
        allExpenses.add(expense);

        expenseSetAmount(expense);

        System.out.println("Do you want to add a place to this expense?");
        boolean yes = selectYesOrNo();
        if (yes) {
            expenseSetPlace(expense);
        } else {
            expense.setPlace(NAME_OF_NO_PLACE);
        }

        System.out.println("Do you want to add this expense to a category?");
        boolean yes1 = selectYesOrNo();
        if (yes1) {
            expenseSetCategory(expense);
        } else {
            expensesWithoutCategory.add(expense);
            expense.setCategory(LABEL_OF_NO_CATEGORY);
        }

        System.out.println("\nGreat! You just recorded the following expense:");
        displayExpenseSummary(expense);
    }

    // MODIFIES: this
    // EFFECTS: displays a list of all expenses and provides options to user
    private void seeAllExpenses() {
        System.out.println("Here is a list of all expenses:");
        displayExpenses(allExpenses);

        String selection = "";
        while (!(selection.equals("m") || selection.equals("s"))) {
            System.out.println("\nm -> modify");
            System.out.println("s -> display weekly expense statistics");
            selection = input.next().toLowerCase();

            if (selection.equals("m")) {
                modifyExpenses(allExpenses);
            } else if (selection.equals("s")) {
                displayWeeklyStatistics();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: displays a list of all categories and provides options to user
    private void seeAllCategories() {
        String selection = "";
        while (!(selection.equals("m") || selection.equals("s"))) {
            displayCategories();
            System.out.println("\nm -> select one to modify");
            System.out.println("n -> create a new category");
            System.out.println("s -> display categorical expense statistics");
            selection = input.next().toLowerCase();

            if (selection.equals("m")) {
                modifyCategories();
            } else if (selection.equals("n")) {
                newCategory();
            } else if (selection.equals("s")) {
                displayCategoryStatistics();
            }
        }
    }

    // MODIFIES: this, expenses
    // EFFECTS: let the user select an expense from the given list of expenses
    //          and provides options to modify the selected expense
    private void modifyExpenses(List<Expense> expenses) {
        Expense expense = selectExpense(expenses);
        System.out.println("You have selected the following expense:");
        displayExpenseSummary(expense);

        String selection = "";
        while (!(selection.equals("f"))) {
            modifyExpensesDisplayOptions();
            selection = input.next().toLowerCase();

            switch (selection) {
                case "a":
                    expenseSetAmount(expense);
                    displayModifiedExpense(expense);
                    break;
                case "d":
                    expenseSetDate(expense);
                    displayModifiedExpense(expense);
                    break;
                case "p":
                    expenseSetPlace(expense);
                    displayModifiedExpense(expense);
                    break;
                case "c":
                    expenseSetCategory(expense);
                    displayModifiedExpense(expense);
                    break;
                case "x":
                    deleteExpense(expense);
                    selection = "f";
                    break;
            }
        }
    }

    // EFFECTS: displays options for modifyExpenses
    private void modifyExpensesDisplayOptions() {
        System.out.println("\na -> change its amount");
        System.out.println("d -> change its date");
        System.out.println("p -> change its place");
        System.out.println("c -> change its category");
        System.out.println("x -> delete it");
        System.out.println("f -> finish modification");
    }

    // MODIFIES: this
    // EFFECTS: let the user select a category from all allCategories and provides options
    //          to modify the selected category
    private void modifyCategories() {
        Category category = selectCategory();

        String selection = "";
        while (!(selection.equals("f"))) {
            String categoryLabel = category.getLabel();
            System.out.println("\nYou have selected the following category: " + categoryLabel);

            System.out.println("\ne -> see a list of expenses in it");
            System.out.println("l -> change its label");
            System.out.println("x -> delete it");
            System.out.println("f -> finish modification");
            selection = input.next().toLowerCase();

            switch (selection) {
                case "e":
                    seeExpensesInCategory(category, categoryLabel);
                    break;
                case "l":
                    categorySetLabel(category);
                    break;
                case "x":
                    deleteCategory(category);
                    selection = "f";
                    break;
            }
        }
    }

    // MODIFIES: this, category
    // EFFECTS: displays a list of expenses in the given category and provides option to
    //          modify those expenses
    private void seeExpensesInCategory(Category category, String categoryLabel) {
        String selection = "";
        while (!selection.equals("b")) {
            System.out.println("\nExpenses in the category " + categoryLabel + ":");
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

    // EFFECTS: displays the total expense of each week
    private void displayWeeklyStatistics() {
        new WeeklyStatistics(allExpenses);
    }

    // EFFECTS: displays the percentage of money spent in each category
    private void displayCategoryStatistics() {
        new CategoryStatistics(allCategories, allExpenses);
    }

    // MODIFIES: this, expense
    // EFFECTS: deletes the given expense
    private void deleteExpense(Expense expense) {
        allExpenses.remove(expense);
        String categoryLabel = expense.getCategory();

        if (!categoryLabel.equals(LABEL_OF_NO_CATEGORY)) {
            Category category = findCategoryFromLabel(categoryLabel);
            category.remove(expense);
        }

        System.out.println("Expense deleted");
    }

    // MODIFIES: this, category
    // EFFECTS: deletes the given category
    private void deleteCategory(Category category) {
        allCategories.remove(category);

        for (Expense e : category.getExpenses()) {
            e.setCategory(LABEL_OF_NO_CATEGORY);
        }

        System.out.println("Category deleted");
    }

    // MODIFIES: this, expense
    // EFFECTS: let the user set the amount of the given expense
    private void expenseSetAmount(Expense expense) {
        System.out.println("Enter the amount you spent: $");
        double amount = input.nextDouble();
        expense.setAmount(amount);
    }

    // MODIFIES: this, expense
    // EFFECTS: let the user set the date of the given expense
    private void expenseSetDate(Expense expense) {
        System.out.println("Entre a date (YYYY-MM-DD): ");
        String date = input.next();
        expense.setDate(date);
    }

    // MODIFIES: this, expense
    // EFFECTS: let the user set the place of the given expense
    private void expenseSetPlace(Expense expense) {
        System.out.println("Entre the name of a place: ");
        String name = input.nextLine().toLowerCase();
        expense.setPlace(name);
    }

    // MODIFIES: this, expense
    // EFFECTS: let the user set the category of the given expense
    private void expenseSetCategory(Expense expense) {
        if (allCategories.isEmpty()) {
            newCategory(expense);
        } else {
            chooseOrCreateNew(expense);
        }
    }

    // MODIFIES: this, category
    // EFFECTS: let the user set the label of the given category
    private void categorySetLabel(Category category) {
        System.out.println("enter the new label: ");
        String label = input.nextLine().toLowerCase();
        category.setLabel(label);

        for (Expense e : allExpenses) {
            e.setCategory(label);
        }

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
        String label = selected.getLabel();
        selected.add(expense);
        expense.setCategory(label);
    }

    // MODIFIES: this, expense
    // EFFECTS: let the user create a new category to assign to the given expense
    private void newCategory(Expense expense) {
        System.out.println("Entre the label a new Category: ");

        String label = input.nextLine().toLowerCase();
        if (categoryAlreadyExists(label)) {
            System.out.println("This category already exists");
            newCategory(expense);
        } else {
            Category category = new Category(label);
            allCategories.add(category);
            category.add(expense);
            expense.setCategory(label);
        }
    }

    // MODIFIES: this
    // EFFECTS: let the user create a new empty category
    private void newCategory() {
        System.out.println("Entre the label a new Category: ");

        String label = input.nextLine().toLowerCase();
        if (categoryAlreadyExists(label)) {
            System.out.println("This category already exists");
            newCategory();
        } else {
            Category category = new Category(label);
            allCategories.add(category);
        }
        System.out.println("\nNew category created");
    }

    // EFFECTS: let the user select an expense by entering an index
    private Expense selectExpense(List<Expense> expenses) {
        System.out.println("Select an expense by entering the number in front of it: ");
        int index = input.nextInt();

        return expenses.get(index - 1);
    }

    // EFFECTS: let the user select a category by entering an index
    private Category selectCategory() {
        System.out.println("Select a category by entering the number in front of it: ");
        int index = input.nextInt();

        return allCategories.get(index - 1);
    }

    // EFFECTS: returns true if a category with the given label already exists
    private boolean categoryAlreadyExists(String label) {
        boolean b = false;

        for (Category c : allCategories) {
            if (c.hasLabel(label)) {
                b = true;
            }
        }

        return b;
    }

    // EFFECTS: displays an expense summary of the given expense
    private void displayExpenseSummary(Expense expense) {
        System.out.println("\tamount: " + expense.getAmount());
        System.out.println("\tdate: " + expense.getDate());
        System.out.println("\tplace: " + expense.getPlace());
        System.out.println("\tcategory: " + expense.getCategory());
    }

    // EFFECTS: displays an expense summary of the given expense, with a message that
    //          the expense has been modified
    private void displayModifiedExpense(Expense expense) {
        System.out.println("The expense has been modified:");
        displayExpenseSummary(expense);
    }

    // EFFECTS: displays a numbered list of the date, amount, place, and category
    //          of all Expenses in the given list of Expenses
    private void displayExpenses(List<Expense> expenses) {
        sortExpenses(expenses);
        int i = 1;
        for (Expense e : expenses) {
            long daysAgo = e.getDaysPriorToToday();
            String daysAgoMessage = daysAgo + " days ago";

            if (daysAgo == 0) {
                daysAgoMessage = "today";
            }

            String message = daysAgoMessage + " (" + e.getDate() + ") you spent $"
                    + e.getAmount() + " at " + e.getPlace() + " in the category "
                    + e.getCategory();

            System.out.println("(" + i + ") " + message);
            i++;
        }
    }

    // REQUIRES: the list of Expenses is not empty
    // MODIFIES: expenses
    // EFFECTS: sort the given list of expenses chronologically from most recent to most distant
    private void sortExpenses(List<Expense> expenses) {
        expenses.sort(Comparator.comparing(Expense::getDate));
        Collections.reverse(expenses);
    }

    // EFFECTS: displays a numbered list of the labels of all Categories
    private void displayCategories() {
        System.out.println("Here is a list of all categories:");
        int numOfItems = allCategories.size();

        for (int i = 1; i <= numOfItems; i++) {
            String message = allCategories.get(i - 1).getLabel();
            System.out.println("(" + i + ") " + message);
        }
    }

    // REQUIRES: a category with the given label must be in allCategories
    // EFFECTS: returns the category with the given label
    private Category findCategoryFromLabel(String label) {
        Category found = null;

        for (Category c : allCategories) {
            if (c.hasLabel(label)) {
                found = c;
            }
        }

        return found;
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
}
