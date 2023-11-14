package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static model.ExpenseTracker.LABEL_OF_NO_CATEGORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Adapted from JsonSerializationDemo
public class JsonWriterTest extends JsonTest {
    Expense expense1;
    Expense expense2;
    Expense expense3;
    Category category1;

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
    void testWriterInvalidFile() {
        try {
            ExpenseTracker et = new ExpenseTracker();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyExpenseTracker() {
        try {
            ExpenseTracker et = new ExpenseTracker();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyExpenseTracker.json");
            writer.open();
            writer.write(et);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyExpenseTracker.json");
            et = reader.read();
            assertEquals(0, et.getAllExpenses().size());
            assertEquals(1, et.getAllCategories().size());
            assertEquals(0, et.getCoNC().getExpenses().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralExpenseTracker() {
        try {
            ExpenseTracker et = new ExpenseTracker();
            initiateExpensesAndCategories(et);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralExpenseTracker.json");
            writer.open();
            writer.write(et);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralExpenseTracker.json");
            et = reader.read();
            checkGeneralExpenseTracker(et);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    private void checkGeneralExpenseTracker(ExpenseTracker et) {
        List<Expense> allExpenses = et.getAllExpenses();
        List<Category> allCategories = et.getAllCategories();
        Category categoryOfNoCategory = et.getCoNC();

        assertEquals(3, et.getAllExpenses().size());
        assertEquals(2, et.getAllCategories().size());
        assertEquals(1, categoryOfNoCategory.getExpenses().size());
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
    }



    // EFFECTS: creates example expenses and categories containing them
    private void initiateExpensesAndCategories(ExpenseTracker et) {
        expense1 = new Expense(et.getCoNC(), 100);
        expense2 = new Expense(et.getCoNC(), 20);
        expense3 = new Expense(et.getCoNC(), 55);
        category1 = new Category("grocery");

        expense1.setDate("2023-10-01");
        expense1.setPlace("place1");
        expense2.setDate("2023-01-01");
        expense2.setPlace("place2");
        expense3.setDate("2023-05-01");
        expense3.setPlace("place3");
        et.addExpense(expense1);
        et.addExpense(expense2);
        et.addExpense(expense3);
        et.addCategory(category1);
        category1.addExpense(expense1);
        category1.addExpense(expense2);
    }
}
