package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Represents a week with 7 days starting at the startDate and ending at the endDate
public class Week {
    LocalDate startDate;
    LocalDate endDate;
    List<Expense> allExpenses;
    List<Expense> expensesInWeek;

    // EFFECTS: startDate is set to the input, endDate is 6 days after the startDate;
    //          allExpenses is passed in from the ExpenseTrackerApp; an expenses is
    //          added to expensesInWeek if it has a date within the range of the week.
    public Week(LocalDate startDate, List<Expense> allExpenses) {
        this.startDate = startDate;
        this.endDate = startDate.plusDays(6);
        this.allExpenses = allExpenses;
        this.expensesInWeek = new ArrayList<>();

        findExpensesInWeek();
    }

    public String getStartDate() {
        return startDate.toString();
    }

    public String getEndDate() {
        return endDate.toString();
    }

    public List<Expense> getExpensesInWeek() {
        return expensesInWeek;
    }

    // EFFECTS: returns the number of expenses in expensesInWeek
    public int getNumOfExpenses() {
        return expensesInWeek.size();
    }

    // REQUIRES: allExpenses is not empty
    // MODIFIES: this
    // EFFECTS: adds an expense in allExpenses to expensesInWeek if it has a date within
    //          the range of the week
    public void findExpensesInWeek() {
        for (Expense e : allExpenses) {
            if (isInThisWeek(e)) {
                expensesInWeek.add(e);
            }
        }
    }

    // EFFECTS: returns the total amount spent in this week
    public double getTotalAmount() {
        double total = 0;

        for (Expense e : expensesInWeek) {
            total += e.getAmount();
        }

        return total;
    }

    // EFFECTS: returns ture if the date of the expense is within this week
    public boolean isInThisWeek(Expense expense) {
        LocalDate date = expense.getDate();
        return (date.isAfter(startDate) && date.isBefore(endDate))
                || date.isEqual(startDate) || date.isEqual(endDate);
    }
}
