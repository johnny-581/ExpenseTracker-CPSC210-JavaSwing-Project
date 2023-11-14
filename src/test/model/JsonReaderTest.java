package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static model.ExpenseTracker.LABEL_OF_NO_CATEGORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Adapted from JsonSerializationDemo
public class JsonReaderTest extends JsonTest {
    Category category1;
    LocalDateTime date1;
    LocalDateTime date2;
    LocalDateTime date3;

    @BeforeEach
    public void runBefore() {
        category1 = new Category("grocery");
        date1 = LocalDate.parse("2023-10-01").atStartOfDay();
        date2 = LocalDate.parse("2023-01-01").atStartOfDay();
        date3 = LocalDate.parse("2023-05-01").atStartOfDay();
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ExpenseTracker et = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyExpenseTracker.json");
        try {
            ExpenseTracker et = reader.read();
            assertEquals(0, et.getAllExpenses().size());
            assertEquals(1, et.getAllCategories().size());
            assertEquals(0, et.getCoNC().getExpenses().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkroom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralExpenseTracker.json");
        try {
            ExpenseTracker et = reader.read();
            List<Expense> allExpenses = et.getAllExpenses();
            List<Category> allCategories = et.getAllCategories();
            Category categoryOfNoCategory = et.getCoNC();
            assertEquals(3, et.getAllExpenses().size());
            assertEquals(2, et.getAllCategories().size());
            checkExpense(allExpenses.get(0), 100, date1, "place1", category1);
            checkExpense(allExpenses.get(1), 55, date3, "place3", categoryOfNoCategory);
            checkExpense(allExpenses.get(2), 20, date2, "place2", category1);

            checkCategory(allCategories.get(0), LABEL_OF_NO_CATEGORY);
            checkCategory(allCategories.get(1), "grocery");
            List<Expense> categoryOfNoCategoryExpenses = categoryOfNoCategory.getExpenses();
            List<Expense> category1Expenses = allCategories.get(1).getExpenses();
            checkExpense(category1Expenses.get(0), 100, date1, "place1", category1);
            checkExpense(category1Expenses.get(1), 20, date2, "place2", category1);
            checkExpense(categoryOfNoCategoryExpenses.get(0), 55, date3, "place3", categoryOfNoCategory);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}