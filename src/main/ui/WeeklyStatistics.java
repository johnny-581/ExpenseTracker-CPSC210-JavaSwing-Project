package ui;

import model.Expense;
import model.Week;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// A display of the total expense of each week
public class WeeklyStatistics {
    List<Expense> allExpenses;
    List<Week> allWeeks;
    LocalDate today;

    // EFFECTS: initiates a WeeklyStatistics with a list of weeks; creates all
    //          the weeks and displays the total expense in each week
    public WeeklyStatistics(List<Expense> allExpenses) {
        this.allExpenses = sortExpenses(allExpenses);
        this.allWeeks = new ArrayList<>();
        this.today = LocalDate.now();

        createWeeks();
        display();
    }

    // EFFECTS: displays the total expense in each week
    private void display() {
        System.out.println("\nHere is a statistic of your weekly spending:");

        String startDate;
        String endDate;
        double totalAmount;
        int numOfExpenses;

        for (Week week : allWeeks) {
            startDate = week.getStartDate();
            endDate = week.getEndDate();
            totalAmount = week.getTotalAmount();
            numOfExpenses = week.getNumOfExpenses();

            System.out.println(" - in the week from " + startDate + " to " + endDate + ", " +
                    "you spent $" + totalAmount + " (" + numOfExpenses + " expenses)");
        }
    }

    // EFFECTS: creates all the weeks up to week of the earliest expense, from most recent to
    //           most distant, with today as the last day of the first week
    private void createWeeks() {
        LocalDate startDate = today.plusDays(1);

        for (long i = 1; i <= numWeeksToCreate(); i++) {
            startDate = startDate.minusWeeks(1);

            Week week = new Week(startDate, allExpenses);
            allWeeks.add(week);
        }
    }

    // EFFECTS: determines the number of weeks that must be created to include all expenses
    private long numWeeksToCreate() {
        Expense earliestExpense = allExpenses.get(allExpenses.size() - 1);
        LocalDate earliestDate = earliestExpense.getDate();
        long period = ChronoUnit.DAYS.between(earliestDate, today);
        period++; // to include today
        long numWeeksToCreate = period / 7;

        if (period % 7 != 0) {
            numWeeksToCreate++;
        }

        return numWeeksToCreate;
    }

    // EFFECTS: sort the given list of expenses and chronologically from most recent to most distant
    private List<Expense> sortExpenses(List<Expense> expenses) {
        expenses.sort(Comparator.comparing(Expense :: getDate));
        Collections.reverse(expenses);
        return expenses;
    }
}
