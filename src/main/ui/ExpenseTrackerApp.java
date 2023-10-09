package ui;

import java.util.Locale;
import java.util.Scanner;

public class ExpenseTrackerApp {
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
    }

    private void processCommand(String command) {
    }
}
