package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static model.Expense.LABEL_OF_NO_CATEGORY;
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
        expense1 = new Expense();
        expense2 = new Expense();
        expense3 = new Expense();
        category1 = new Category("grocery");
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
            assertEquals(0, et.getAllCategories().size());
            assertEquals(0, et.getCategoryOfNoCategory().getExpenses().size());
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
    }



    // EFFECTS: creates example expenses and categories containing them
    private void initiateExpensesAndCategories(ExpenseTracker et) {
        expense1.setAmount(100);
        expense1.setDate("2023-10-01");
        expense1.setPlace("place1");
        expense2.setAmount(20);
        expense2.setDate("2023-01-01");
        expense2.setPlace("place2");
        expense3.setAmount(55);
        expense3.setDate("2023-05-01");
        expense3.setPlace("place3");
        et.addExpense(expense1);
        et.addExpense(expense2);
        et.addExpense(expense3);
        et.addCategory(category1);

        category1.add(expense1);
        category1.add(expense2);
        et.getCategoryOfNoCategory().add(expense3);
        expense1.setCategory("grocery");
        expense2.setCategory("grocery");
        expense3.setCategory(LABEL_OF_NO_CATEGORY);
    }
}
