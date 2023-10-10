package ui;

import model.Category;
import model.CollectionOfEntries;
import model.ExpenseEntry;
import model.Place;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ExpenseTrackerApp {
    private List<ExpenseEntry> allExpenses;
    private List<CollectionOfEntries> allCategories;
    private List<CollectionOfEntries> allPlaces;
    private Scanner input = new Scanner(System.in);

    public ExpenseTrackerApp() {
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
        System.out.println("\tp -> see all places");
        System.out.println("\tq -> quit");
    }

    private void processCommand(String command) {
        if (command.equals("n")) {
            newExpense();
        } else if (command.equals("e")) {
            displayExpenses();
        } else if (command.equals("c")) {
            displayCategories();
        } else if (command.equals("p")) {
            displayPlaces();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    private void newExpense() {
        System.out.println("Enter the amount you spent: $");
        double amount = input.nextDouble();
        ExpenseEntry expense = new ExpenseEntry(amount);

        System.out.println("Where did you spend it?");

        if (allPlaces.isEmpty()) {
            createNewItem(expense, allPlaces, "place");
        } else {
            System.out.println("You can now either choose from an existing place (e) " +
                    "or create a new place (n)");
            String selection = input.next().toLowerCase();
            if (selection.equals("e") {
                chooseFromExistingItems();
            } else {
                createNewItem(allPlaces, "place");
            }
        }

        System.out.println("Which category does it belong?");
    }

    private void createNewItem(ExpenseEntry e, List<CollectionOfEntries> listOfItems, String itemType) {
        System.out.println("Entre the name of a " + itemType + " : ");

        String name = input.next().toLowerCase();
        if (itemType.equals("place")) {
            Place place = new Place(name);
            e.setPlace(name);
            listOfItems.add(place);
        } else {
            Category category = new Category(name)
            e.setCategory(name);
            listOfItems.add(category);
        }
    }

    private void chooseFromExistingItems() {
        System.out.println("Choose one of the following places " +
                "by entering the number in front of it";
        displayPlaces();

        Integer index = input.nextInt();
    }

    private void displayExpenses() {
        System.out.println("Here is a list of all expenses:");

        int i = 1;

        for (ExpenseEntry e : allExpenses) {
            String message = "on " + e.getDate() + " you spent $" + e.getAmount()
                    + " at " + e.getPlace() + " in the " + e.getCategory() + " category";

            System.out.println("(" + i + ") " + message);
            i++;
        }
    }

    private void displayCategories() {
        System.out.println("Here is a list of all categories:");
        displayNamesOfItems(allPlaces);
    }

    private void displayPlaces() {
        System.out.println("Here is a list of all places:");
        displayNamesOfItems(allPlaces);
    }

    private void displayNamesOfItems(List<CollectionOfEntries> listToDisplay) {
        int numOfItems = listToDisplay.size();

        for (int i = 1; i <= numOfItems; i++) {
            String message = listToDisplay.get(i - 1).getName();
            System.out.println("(" + i + ") " + message);
        }
    }
}
