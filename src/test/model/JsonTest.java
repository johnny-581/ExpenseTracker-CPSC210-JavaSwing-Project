package model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Adapted from JsonSerializationDemo
public class JsonTest {

    protected void checkExpense(Expense expense, double amount, LocalDateTime date,
                                String place, String category) {
        assertEquals(amount, expense.getAmount());
        assertEquals(date, expense.getDate());
        assertEquals(place, expense.getPlace());
        assertEquals(category, expense.getCategory());
    }

    protected void checkCategoryLabel(Category category, String label) {
        assertEquals(label, category.getLabel());
    }
}
