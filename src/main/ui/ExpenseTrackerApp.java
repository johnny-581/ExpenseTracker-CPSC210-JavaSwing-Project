package ui;

import model.Category;
import model.Expense;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ExpenseTrackerApp {
    private static final String LABEL_OF_NO_CATEGORY = "none";

    private List<Expense> allExpenses; //!!!
    private List<Category> allCategories;
    private Category expensesWithoutCategory;
    private Scanner input;

    public ExpenseTrackerApp() {
        allExpenses = new ArrayList<>();
        allCategories = new ArrayList<>();
        expensesWithoutCategory = new Category(LABEL_OF_NO_CATEGORY); // not in allCategories
        input = new Scanner(System.in);

        createExamples(); // temporary
        runExpenseTracker();
    }

    public void runExpenseTracker() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye");
    }

    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tn -> record new expense");
        System.out.println("\te -> see all expenses");
        System.out.println("\tc -> see all expense categories");
        System.out.println("\tq -> quit");
    }

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

    private void newExpense() {
        Expense expense = new Expense();
        allExpenses.add(expense);

        expenseSetAmount(expense);

        System.out.println("Do you want to add a place to this expense?");
        boolean yes = selectYesOrNo();
        if (yes) {
            expenseSetPlace(expense);
        } else {
            expense.setPlace("unknown");
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
                weeklyStatistics();
            }
        }
    }

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
                categoryStatistics();
            }
        }
    }

    private void modifyExpenses(List<Expense> expenses) {
        Expense expense = selectExpense(expenses);
        System.out.println("You have selected the following expense:");
        displayExpenseSummary(expense);

        String selection = "";
        while (!(selection.equals("f"))) {
            System.out.println("\na -> change its amount");
            System.out.println("d -> change its date");
            System.out.println("p -> change its place");
            System.out.println("c -> change its category");
            System.out.println("x -> delete it");
            System.out.println("f -> finish modification");
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

    private void weeklyStatistics() {
    }

    private void categoryStatistics() {
        CategoryStatistics stats = new CategoryStatistics(allCategories, allExpenses);
    }

    private void deleteExpense(Expense expense) {
        allExpenses.remove(expense);
        String categoryLabel = expense.getCategory();

        if (!categoryLabel.equals(LABEL_OF_NO_CATEGORY)) {
            Category category = findCategoryFromLabel(categoryLabel);
            category.remove(expense);
        }

        System.out.println("Expense deleted");
    }

    private void deleteCategory(Category category) {
        allCategories.remove(category);

        for (Expense e : category.getExpenses()) {
            e.setCategory(LABEL_OF_NO_CATEGORY);
        }

        System.out.println("Category deleted");
    }

    private void expenseSetAmount(Expense expense) {
        System.out.println("Enter the amount you spent: $");
        double amount = input.nextDouble();
        expense.setAmount(amount);
    }

    private void expenseSetDate(Expense expense) {
        System.out.println("Entre a date (YYYY-MM-DD): ");
        String date = input.next();
        expense.setDate(date);
    }

    private void expenseSetPlace(Expense expense) {
        System.out.println("Entre the name of a place: ");
        String name = input.nextLine().toLowerCase();
        expense.setPlace(name);
    }

    private void expenseSetCategory(Expense expense) {
        if (allCategories.isEmpty()) {
            newCategory(expense);
        } else {
            chooseOrCreateNew(expense);
        }
    }

    private void categorySetLabel(Category category) {
        System.out.println("enter the new label: ");
        String label = input.nextLine().toLowerCase();
        category.setLabel(label);

        for (Expense e : allExpenses) {
            e.setCategory(label);
        }

        System.out.println("Label changed");
    }

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

    private void chooseFromExistingCategories(Expense expense) {
        displayCategories();
        Category selected = selectCategory();
        String label = selected.getLabel();
        selected.add(expense);
        expense.setCategory(label);
    }

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

    private Expense selectExpense(List<Expense> expenses) {
        System.out.println("Select an expense by entering the number in front of it: ");
        int index = input.nextInt();

        return expenses.get(index - 1);
    }

    private Category selectCategory() {
        System.out.println("Select a category by entering the number in front of it: ");
        int index = input.nextInt();

        return allCategories.get(index - 1);
    }

    private boolean categoryAlreadyExists(String label) {
        boolean b = false;

        for (Category c : allCategories) {
            if (c.hasLabel(label)) {
                b = true;
            }
        }

        return b;
    }

    private void displayExpenseSummary(Expense expense) {
        System.out.println("\tamount: " + expense.getAmount());
        System.out.println("\tdate: " + expense.getDate());
        System.out.println("\tplace: " + expense.getPlace());
        System.out.println("\tcategory: " + expense.getCategory());
    }

    private void displayModifiedExpense(Expense expense) {
        System.out.println("The expense has been modified:");
        displayExpenseSummary(expense);
    }

    private void displayExpenses(List<Expense> expenses) {
//        expenses = sortExpenses(expenses);
        int i = 1;
        for (Expense e : expenses) {
            String message = "on " + e.getDate() + " you spent $" + e.getAmount()
                    + " at " + e.getPlace() + " in the category " + e.getCategory();

            System.out.println("(" + i + ") " + message);
            i++;
        }
    }

//    private List<Expense> sortExpenses(List<Expense> expenses) {
//        List<Expense> sortedExpenses = new ArrayList<>();
//        List<LocalDate> dates = new ArrayList<>();
//        expenses.sort(Comparator.comparing());
//    }

    private void displayCategories() {
        System.out.println("Here is a list of all categories:");
        int numOfItems = allCategories.size();

        for (int i = 1; i <= numOfItems; i++) {
            String message = allCategories.get(i - 1).getLabel();
            System.out.println("(" + i + ") " + message);
        }
    }

    private Category findCategoryFromLabel(String label) {
        Category found = null;

        for (Category c : allCategories) {
            if (c.hasLabel(label)) {
                found = c;
            }
        }

        return found;
    }

    private boolean selectYesOrNo() {
        String selection = "";

        while (!(selection.equals("y") || selection.equals("n"))) {
            System.out.println("y -> yes");
            System.out.println("n -> no");
            selection = input.next().toLowerCase();
        }

        return selection.equals("y");
    }



    private void createExamples() {
        Expense E1 = new Expense();
        Expense E2 = new Expense();
        Expense E3 = new Expense();
        Expense E4 = new Expense();
        Expense E5 = new Expense();
        allExpenses = List.of(E1, E2, E3, E4, E5);

        Category C1 = new Category("grocery");
        Category C2 = new Category("clothing");
        Category C3 = new Category("rent");
        allCategories = List.of(C1, C2, C3);

        E1.setAmount(100);
        E1.setPlace("no frills");
        E1.setCategory("grocery");
        C1.add(E1);

        E2.setAmount(20);
        E2.setPlace("save on food");
        E2.setCategory("grocery");
        C1.add(E2);

        E3.setAmount(55);
        E3.setPlace("ubc book store");
        E3.setCategory("clothing");
        C2.add(E3);

        E4.setAmount(210);
        E4.setPlace("lululemon");
        E4.setCategory("clothing");
        C2.add(E4);

        E5.setAmount(1000);
        E5.setPlace("ubc housing");
        E5.setCategory("rent");
        C3.add(E5);
    }
}
