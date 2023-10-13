package model;

import java.time.LocalDate;
import java.util.List;

public class Week {
    LocalDate startDate;
    LocalDate endDate;
    List<Expense> expensesInWeek;
    List<Expense> allExpenses;

    private Week(LocalDate startDate, List<Expense> allExpenses) {
        this.startDate = startDate;
        this.endDate = startDate.plusWeeks(1);
        this.allExpenses = allExpenses;
    }

    private List<Expense> getExpensesInWeek() {
        for (Expense e : allExpenses) {
            if (wasInThisWeek(e)) {
                expensesInWeek.add(e);
            }
        }

        return allExpenses;
    }

    private double getTotalAmount() {
        double total = 0;

        for (Expense e : expensesInWeek) {
            total += e.getAmount();
        }

        return total;
    }

    private boolean wasInThisWeek(Expense expense) {
        LocalDate date = expense.getDate();
        return date.isAfter(startDate) || date.isBefore(endDate)
                || date.isEqual(startDate) || date.isEqual(endDate);
    }
}
