package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {
    Category testCategory;
    Expense E1;
    Expense E2;
    Expense E3;

    @BeforeEach
    public void runBefore() {
        testCategory = new Category("food");
        E1 = new Expense();
        E2 = new Expense();
        E3 = new Expense();
        E1.setAmount(100);
        E2.setAmount(20);
        E3.setAmount(5.5);

    }

    @Test
    public void testConstructor() {
        assertEquals("food", testCategory.getLabel());
        assertEquals(0, testCategory.getExpenses().size());
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
    public void testAddOne() {
        testCategory.add(E1);

        List<Expense> expenses = testCategory.getExpenses();
        assertEquals(1, expenses.size());
        assertEquals(E1, expenses.get(0));
    }

    @Test
    public void testAddMultiple() {
        testCategory.add(E1);
        testCategory.add(E2);
        testCategory.add(E3);

        List<Expense> expenses = testCategory.getExpenses();
        assertEquals(3, expenses.size());
        assertEquals(E1, expenses.get(0));
        assertEquals(E2, expenses.get(1));
        assertEquals(E3, expenses.get(2));
    }

    @Test
    public void testAddToIndex() {
        testCategory.add(E1);
        testCategory.add(0, E2);
        testCategory.add(1, E3);

        List<Expense> expenses = testCategory.getExpenses();
        assertEquals(3, expenses.size());
        assertEquals(E2, expenses.get(0));
        assertEquals(E3, expenses.get(1));
        assertEquals(E1, expenses.get(2));
    }

    @Test
    public void testRemoveOne() {
        testCategory.add(E1);
        testCategory.add(E2);
        testCategory.add(E3);

        testCategory.remove(E2);

        List<Expense> expenses = testCategory.getExpenses();
        assertEquals(2, expenses.size());
        assertEquals(E1, expenses.get(0));
        assertEquals(E3, expenses.get(1));
    }

    @Test
    public void testRemoveAll() {
        testCategory.add(E1);
        testCategory.add(E2);
        testCategory.add(E3);

        testCategory.remove(E1);
        testCategory.remove(E2);
        testCategory.remove(E3);

        List<Expense> expenses = testCategory.getExpenses();
        assertEquals(0, expenses.size());
    }

    @Test
    public void testGetTotalOne() {
        testCategory.add(E1);

        assertEquals(100, testCategory.totalAmount());
    }

    @Test
    public void testGetTotalMultiple() {
        testCategory.add(E1);
        testCategory.add(E2);
        testCategory.add(E3);

        assertEquals(100 + 20 + 5.5, testCategory.totalAmount());
    }
}
