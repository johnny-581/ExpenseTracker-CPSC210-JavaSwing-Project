package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static model.Expense.LABEL_OF_NO_CATEGORY;
import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTrackerTest {
    ExpenseTracker expenseTracker;

    Expense expense1;
    Expense expense2;
    Expense expense3;
    Category category1;
    Category category2;
    Category category3;

    @BeforeEach
    public void runBefore() {
        expenseTracker = new ExpenseTracker();
    }

    @Test
    public void testConstructor() {
        assertTrue(expenseTracker.getAllExpenses().isEmpty());
        assertTrue(expenseTracker.getAllCategories().isEmpty());
        String label = expenseTracker.getCategoryOfNoCategory().getLabel();
        assertEquals(LABEL_OF_NO_CATEGORY, label);
    }

    @Test
    public void testGetCategoryAt() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        assertEquals(category1, expenseTracker.getCategoryAt(1));
        assertEquals(category2, expenseTracker.getCategoryAt(2));
        assertEquals(category3, expenseTracker.getCategoryAt(3));
    }

    @Test
    public void testGetNumCategoriesZero() {
        assertEquals(0, expenseTracker.getNumCategories());
    }

    @Test
    public void testGetNumCategoriesThree() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        assertEquals(3, expenseTracker.getNumCategories());
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

        assertEquals(1, expenseTracker.getNumCategories());
        assertEquals(category1, expenseTracker.getCategoryAt(1));
    }

    @Test
    public void testAddMultipleCategories() {
        initiateExpensesAndCategories();
        expenseTracker.addCategory(category1);
        expenseTracker.addCategory(category2);
        expenseTracker.addCategory(category3);

        assertEquals(3, expenseTracker.getNumCategories());
        assertEquals(category1, expenseTracker.getCategoryAt(1));
        assertEquals(category2, expenseTracker.getCategoryAt(2));
        assertEquals(category3, expenseTracker.getCategoryAt(3));
    }

    @Test
    public void testAddOneExpenseToCategory() {
        initiateExpensesAndCategories();
        expenseTracker.addExpenseToCategory(expense1, category1);

        assertEquals(1, category1.getExpenses().size());
        assertEquals(expense1, category1.getExpenses().get(0));
        assertEquals(category1.getLabel(), expense1.getCategory());
    }

    @Test
    public void testAddMultipleExpensesToCategory() {
        initiateExpensesAndCategories();
        expenseTracker.addExpenseToCategory(expense1, category1);
        expenseTracker.addExpenseToCategory(expense2, category1);
        expenseTracker.addExpenseToCategory(expense3, category1);

        assertEquals(3, category1.getExpenses().size());
        assertEquals(expense1, category1.getExpenses().get(0));
        assertEquals(expense3, category1.getExpenses().get(1));
        assertEquals(expense2, category1.getExpenses().get(2));
        assertEquals(category1.getLabel(), expense1.getCategory());
        assertEquals(category1.getLabel(), expense2.getCategory());
        assertEquals(category1.getLabel(), expense3.getCategory());
    }

    @Test
    public void testAddOneToNoCategory() {
        initiateExpensesAndCategories();
        expenseTracker.addExpenseToNoCategory(expense1);
        Category catOfNoCat = expenseTracker.getCategoryOfNoCategory();
        assertEquals(1, catOfNoCat.getExpenses().size());
        assertEquals(expense1, catOfNoCat.getExpenses().get(0));
    }

    @Test
    public void testAddMultipleToNoCategory() {
        initiateExpensesAndCategories();
        expenseTracker.addExpenseToNoCategory(expense1);
        expenseTracker.addExpenseToNoCategory(expense2);
        expenseTracker.addExpenseToNoCategory(expense3);

        Category catOfNoCat = expenseTracker.getCategoryOfNoCategory();
        assertEquals(3, catOfNoCat.getExpenses().size());
        assertEquals(expense1, catOfNoCat.getExpenses().get(0));
        assertEquals(expense2, catOfNoCat.getExpenses().get(1));
        assertEquals(expense3, catOfNoCat.getExpenses().get(2));
    }

    @Test
    public void testDeleteExpenseNotInCategory() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        expense1.setNoCategory();
        expenseTracker.deleteExpense(expense1);

        List<Expense> allExpenses = expenseTracker.getAllExpenses();
        assertEquals(2, allExpenses.size());
        assertFalse(allExpenses.contains(expense1));
    }

    @Test
    public void testDeleteExpenseInCategory() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        addExpensesToCategories();
        expenseTracker.deleteExpense(expense1);

        List<Expense> allExpenses = expenseTracker.getAllExpenses();
        assertEquals(2, allExpenses.size());
        assertFalse(allExpenses.contains(expense1));

        List<Expense> categoryExpenses = category1.getExpenses();
        assertEquals(1, categoryExpenses.size());
        assertFalse(categoryExpenses.contains(expense1));
    }

    @Test
    public void testDeleteCategoryWithNoExpense() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        expenseTracker.deleteCategory(category1);

        List<Category> allCategories = expenseTracker.getAllCategories();
        assertEquals(2, allCategories.size());
        assertFalse(allCategories.contains(category1));
    }

    @Test
    public void testDeleteCategoryWithExpenses() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        addExpensesToCategories();
        expenseTracker.deleteCategory(category1);

        List<Category> allCategories = expenseTracker.getAllCategories();
        assertEquals(2, allCategories.size());
        assertFalse(allCategories.contains(category1));

        assertEquals(LABEL_OF_NO_CATEGORY, expense1.getCategory());
        assertEquals(LABEL_OF_NO_CATEGORY, expense2.getCategory());
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
        assertFalse(expenseTracker.categoryExists(LABEL_OF_NO_CATEGORY));
    }

    @Test
    public void testCategoryExistsTrue() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        assertTrue(expenseTracker.categoryExists("grocery"));
        assertTrue(expenseTracker.categoryExists("rent"));
        assertFalse(expenseTracker.categoryExists(LABEL_OF_NO_CATEGORY));
    }

    @Test
    public void testChangeCategoryForExpensesDifferent() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        addExpensesToCategories();
        expenseTracker.changeCategoryForExpenses("grocery", "food");

        assertEquals("food", expense1.getCategory());
        assertEquals("food", expense2.getCategory());
        assertEquals("clothing", expense3.getCategory());
    }

    @Test
    public void testChangeCategoryForExpensesSame() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        addExpensesToCategories();
        expenseTracker.changeCategoryForExpenses("grocery", "grocery");

        assertEquals("grocery", expense1.getCategory());
        assertEquals("grocery", expense2.getCategory());
        assertEquals("clothing", expense3.getCategory());
    }

    @Test
    public void testSortAllExpenses() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        expenseTracker.sortExpenses();

        List<Expense> allExpenses = expenseTracker.getAllExpenses();
        assertEquals(expense1, allExpenses.get(0));
        assertEquals(expense3, allExpenses.get(1));
        assertEquals(expense2, allExpenses.get(2));
    }

    @Test
    public void testSortGivenExpenses() {
        initiateExpensesAndCategories();
        addExpensesAndCategories();
        addExpensesToCategories();
        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense1);
        expenses.add(expense2);
        expenses.add(expense3);
        expenseTracker.sortExpenses(expenses);

        assertEquals(expense1, expenses.get(0));
        assertEquals(expense3, expenses.get(1));
        assertEquals(expense2, expenses.get(2));
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






    // EFFECTS: creates example expenses and categories containing them
    private void initiateExpensesAndCategories() {
        expense1 = new Expense();
        expense2 = new Expense();
        expense3 = new Expense();

        category1 = new Category("grocery");
        category2 = new Category("clothing");
        category3 = new Category("rent");

        expense1.setAmount(100);
        expense1.setDate("2023-10-01");

        expense2.setAmount(20);
        expense2.setDate("2023-01-01");

        expense3.setAmount(55);
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
        category1.add(expense1);
        category1.add(expense2);
        category2.add(expense3);
        expense1.setCategory("grocery");
        expense2.setCategory("grocery");
        expense3.setCategory("clothing");
    }
}
