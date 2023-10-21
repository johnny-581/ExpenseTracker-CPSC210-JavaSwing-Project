package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static model.Expense.LABEL_OF_NO_CATEGORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Adapted from JsonSerializationDemo
public class JsonReaderTest extends JsonTest{
    LocalDateTime date1;
    LocalDateTime date2;
    LocalDateTime date3;

    @BeforeEach
    public void runBefore() {
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
            assertEquals(0, et.getAllCategories().size());
            assertEquals(0, et.getCategoryOfNoCategory().getExpenses().size());
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
            Category categoryOfNoCategory = et.getCategoryOfNoCategory();
            assertEquals(3, et.getAllExpenses().size());
            assertEquals(1, et.getAllCategories().size());
            assertEquals(1, et.getCategoryOfNoCategory().getExpenses().size());
            checkExpense(allExpenses.get(0), 100, date1, "place1", "grocery");
            checkExpense(allExpenses.get(1), 20, date2, "place2", "grocery");
            checkExpense(allExpenses.get(2), 55, date3, "place3", LABEL_OF_NO_CATEGORY);

            checkCategoryLabel(allCategories.get(0), "grocery");
            checkCategoryLabel(categoryOfNoCategory, LABEL_OF_NO_CATEGORY);
            List<Expense> category1Expenses = allCategories.get(0).getExpenses();
            List<Expense> categoryOfNoCategoryExpenses = categoryOfNoCategory.getExpenses();
            checkExpense(category1Expenses.get(0), 100, date1, "place1", "grocery");
            checkExpense(category1Expenses.get(1), 20, date2, "place2", "grocery");
            checkExpense(categoryOfNoCategoryExpenses.get(0), 55, date3, "place3", LABEL_OF_NO_CATEGORY);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}