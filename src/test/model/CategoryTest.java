package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static model.ExpenseTracker.LABEL_OF_NO_CATEGORY;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {
    Category categoryOfNoCategory;
    Category testCategory;
    Expense E1;
    Expense E2;
    Expense E3;

    @BeforeEach
    public void runBefore() {
        categoryOfNoCategory = new Category(LABEL_OF_NO_CATEGORY);
        testCategory = new Category("food");
        E1 = new Expense(categoryOfNoCategory, 100);
        E2 = new Expense(categoryOfNoCategory, 20);
        E3 = new Expense(categoryOfNoCategory, 5.5);

    }

    @Test
    public void testConstructor() {
        assertEquals("food", testCategory.getLabel());
        assertEquals(0, testCategory.getExpenses().size());
        assertNotNull(testCategory.getIconColor());
    }

    @Test
    public void testSetLabel() {
        testCategory.setLabel("new label");
        assertEquals("new label", testCategory.getLabel());
    }

    @Test
    public void testHasLabelTrue() {
        assertTrue(testCategory.hasLabel("food"));
    }

    @Test
    public void testHasLabelFalse() {
        assertFalse(testCategory.hasLabel("abc"));
    }

    @Test
    public void testAddOneExpense() {
        testCategory.addExpense(E1);

        List<Expense> expenses = testCategory.getExpenses();
        assertEquals(1, expenses.size());
        assertTrue(testCategory.contains(E1));
        assertFalse(categoryOfNoCategory.contains(E1));
        assertEquals(testCategory, E1.getCategory());
    }

    @Test
    public void testAddMultipleExpenses() {
        testCategory.addExpense(E1);
        testCategory.addExpense(E2);
        testCategory.addExpense(E3);

        List<Expense> expenses = testCategory.getExpenses();
        assertEquals(3, expenses.size());
        assertTrue(testCategory.contains(E1));
        assertTrue(testCategory.contains(E2));
        assertTrue(testCategory.contains(E3));
        assertFalse(categoryOfNoCategory.contains(E1));
        assertFalse(categoryOfNoCategory.contains(E2));
        assertFalse(categoryOfNoCategory.contains(E3));
        assertEquals(testCategory, E1.getCategory());
        assertEquals(testCategory, E2.getCategory());
        assertEquals(testCategory, E3.getCategory());
    }

    @Test
    public void testRemoveOneExpense() {
        testCategory.addExpense(E1);
        testCategory.addExpense(E2);
        testCategory.addExpense(E3);
        testCategory.removeExpense(E2);

        List<Expense> expenses = testCategory.getExpenses();
        assertEquals(2, expenses.size());
        assertTrue(testCategory.contains(E1));
        assertFalse(testCategory.contains(E2));
        assertTrue(testCategory.contains(E3));
        assertEquals(testCategory, E1.getCategory());
        assertEquals(categoryOfNoCategory, E2.getCategory());
        assertEquals(testCategory, E3.getCategory());
    }

    @Test
    public void testContainsTrue() {
        testCategory.addExpense(E1);
        assertTrue(testCategory.contains(E1));
    }

    @Test
    public void testContainsFalse() {
        testCategory.addExpense(E1);
        assertFalse(testCategory.contains(E2));
    }

    @Test
    public void testGetTotalOne() {
        testCategory.addExpense(E1);
        assertEquals(100, testCategory.totalAmount());
    }

    @Test
    public void testGetTotalMultiple() {
        testCategory.addExpense(E1);
        testCategory.addExpense(E2);
        testCategory.addExpense(E3);
        assertEquals(100 + 20 + 5.5, testCategory.totalAmount());
    }
}
