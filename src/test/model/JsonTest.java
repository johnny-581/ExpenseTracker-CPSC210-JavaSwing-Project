package model;

import java.awt.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Adapted from JsonSerializationDemo
public class JsonTest {

    protected void checkExpense(Expense expense, double amount, LocalDateTime date,
                                String place, Category category) {
        assertEquals(amount, expense.getAmount());
        assertEquals(date, expense.getDate());
        assertEquals(place, expense.getPlace());
        assertEquals(category.getLabel(), expense.getCategory().getLabel());
    }

    protected void checkCategory(Category category, String label) {
        assertEquals(label, category.getLabel());
        assertNotNull(category.getIconColor());
    }
}
