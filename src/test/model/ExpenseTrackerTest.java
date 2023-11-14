package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTrackerTest {
    ExpenseTracker expenseTracker;
    Expense expense1;
    Expense expense2;
    Expense expense3;
    Category category1;
    Category category2;
    Category category3;
    Category categoryOfNoCategory;

    @BeforeEach
    public void runBefore() {
        expenseTracker = new ExpenseTracker();
        categoryOfNoCategory =  expenseTracker.getCoNC();
    }

    @Test
    public void testConstructor() {
        assertTrue(expenseTracker.getAllExpenses().isEmpty());
        List<Category> categories = expenseTracker.getAllCategories();
        assertEquals(1, categories.size());
        assertTrue(categories.contains(expenseTracker.getCoNC()));
    }

    @Test
    public void testGetCategoryAt() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        assertEquals(categoryOfNoCategory, expenseTracker.getCategoryAt(1));
        assertEquals(category1, expenseTracker.getCategoryAt(2));
        assertEquals(category2, expenseTracker.getCategoryAt(3));
        assertEquals(category3, expenseTracker.getCategoryAt(4));
    }

    @Test
    public void testAddOneExpense() {
        initiateExpensesAndCategories();
        expenseTracker.addExpense(expense1);

        assertEquals(1, expenseTracker.getAllExpenses().size());
        assertEquals(expense1, expenseTracker.getAllExpenses().get(0));
    }

    @Test
    public void testAddMultipleExpenses() {
        initiateExpensesAndCategories();
        expenseTracker.addExpense(expense1);
        expenseTracker.addExpense(expense2);
        expenseTracker.addExpense(expense3);

        assertEquals(3, expenseTracker.getAllExpenses().size());
        assertEquals(expense1, expenseTracker.getAllExpenses().get(0));
        assertEquals(expense3, expenseTracker.getAllExpenses().get(1));
        assertEquals(expense2, expenseTracker.getAllExpenses().get(2));
    }

    @Test
    public void testAddOneCategory() {
        initiateExpensesAndCategories();
        expenseTracker.addCategory(category1);

        assertEquals(2, expenseTracker.getAllCategories().size());
        assertEquals(categoryOfNoCategory, expenseTracker.getCategoryAt(1));
        assertEquals(category1, expenseTracker.getCategoryAt(2));
    }

    @Test
    public void testAddMultipleCategories() {
        initiateExpensesAndCategories();
        expenseTracker.addCategory(category1);
        expenseTracker.addCategory(category2);
        expenseTracker.addCategory(category3);

        assertEquals(4, expenseTracker.getAllCategories().size());
        assertEquals(categoryOfNoCategory, expenseTracker.getCategoryAt(1));
        assertEquals(category1, expenseTracker.getCategoryAt(2));
        assertEquals(category2, expenseTracker.getCategoryAt(3));
        assertEquals(category3, expenseTracker.getCategoryAt(4));
    }

    @Test
    public void testDeleteExpense() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        addExpensesToCategories();
        expenseTracker.deleteExpense(0);

        List<Expense> allExpenses = expenseTracker.getAllExpenses();
        assertEquals(2, allExpenses.size());
        assertFalse(allExpenses.contains(expense1));

        List<Expense> categoryExpenses = category1.getExpenses();
        assertEquals(1, categoryExpenses.size());
        assertFalse(categoryExpenses.contains(expense1));
    }

    @Test
    public void testDeleteExpenseEmpty() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        addExpensesToCategories();
        expenseTracker.deleteExpense(0);
        expenseTracker.deleteExpense(0);
        expenseTracker.deleteExpense(0);

        List<Expense> allExpenses = expenseTracker.getAllExpenses();
        assertEquals(0, allExpenses.size());
        assertFalse(allExpenses.contains(expense1));

        List<Expense> categoryExpenses = category1.getExpenses();
        assertEquals(0, categoryExpenses.size());
        assertFalse(expenseTracker.categoryExists("grocery"));
    }

    @Test
    public void testDeleteCategoryWithNoExpense() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        expenseTracker.deleteCategory(category1);

        List<Category> allCategories = expenseTracker.getAllCategories();
        assertEquals(3, allCategories.size());
        assertFalse(allCategories.contains(category1));
    }

    @Test
    public void testDeleteCategoryWithExpenses() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        addExpensesToCategories();
        expenseTracker.deleteCategory(category1);

        List<Category> allCategories = expenseTracker.getAllCategories();
        assertEquals(3, allCategories.size());
        assertFalse(allCategories.contains(category1));

        assertEquals(categoryOfNoCategory, expense1.getCategory());
    }

    @Test
    public void testHasNoExpenseTrue() {
        assertTrue(expenseTracker.hasNoExpense());
    }

    @Test
    public void testHasNoExpenseFalse() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        assertFalse(expenseTracker.hasNoExpense());
    }

    @Test
    public void testHasNoCategoryTrue() {
        assertTrue(expenseTracker.hasNoCategory());
    }

    @Test
    public void testHasNoCategoryFalse() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        assertFalse(expenseTracker.hasNoCategory());
    }

    @Test
    public void testCategoryExistsFalse() {
        assertFalse(expenseTracker.categoryExists("grocery"));
        assertFalse(expenseTracker.categoryExists("rent"));
    }

    @Test
    public void testCategoryExistsTrue() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        assertTrue(expenseTracker.categoryExists("grocery"));
        assertTrue(expenseTracker.categoryExists("rent"));
    }

    @Test
    public void testGetCategoryFromLabel() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        assertEquals(category1, expenseTracker.getCategoryFromLabel("grocery"));
        assertEquals(category2, expenseTracker.getCategoryFromLabel("clothing"));
        assertEquals(category3, expenseTracker.getCategoryFromLabel("rent"));
    }

    @Test
    public void testCalculatePercentage() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        addExpensesToCategories();
        String percentage = expenseTracker.calculatePercentage(category1);

        assertEquals("69%", percentage);
    }

    @Test
    public void testCalculatePercentageNoExpense() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        addExpensesToCategories();
        String percentage = expenseTracker.calculatePercentage(category3);

        assertEquals("0%", percentage);
    }

    @Test
    public void testCalculateTotalAmount() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        double totalAmount = expenseTracker.calculateTotalAmount();

        assertEquals(175, totalAmount);
    }

    @Test
    public void testCalculateTotalAmountZero() {
        double totalAmount = expenseTracker.calculateTotalAmount();

        assertEquals(0, totalAmount);
    }






    // EFFECTS: creates example expenses and categories
    private void initiateExpensesAndCategories() {
        expense1 = new Expense(categoryOfNoCategory, 100);
        expense2 = new Expense(categoryOfNoCategory, 20);
        expense3 = new Expense(categoryOfNoCategory, 55);
        category1 = new Category("grocery");
        category2 = new Category("clothing");
        category3 = new Category("rent");

        expense1.setDate("2023-10-01");
        expense2.setDate("2023-01-01");
        expense3.setDate("2023-05-01");
    }

    // EFFECTS: adds example expenses and categories to the expense tracker
    private void addExpensesAndCategories() {
        expenseTracker.addExpense(expense1);
        expenseTracker.addExpense(expense2);
        expenseTracker.addExpense(expense3);
        expenseTracker.addCategory(category1);
        expenseTracker.addCategory(category2);
        expenseTracker.addCategory(category3);
    }

    // EFFECTS: adds example expenses to categories
    private void addExpensesToCategories() {
        category1.addExpense(expense1);
        category1.addExpense(expense2);
        category2.addExpense(expense3);
    }
}
