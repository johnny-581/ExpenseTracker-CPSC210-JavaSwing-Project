package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryTest {
    Category testCategory;
    Expense SE1;
    Expense SE2;
    Expense SE3;

    @BeforeEach
    public void runBefore() {
        testCategory = new Category("food");
        SE1 = new Expense(100);
        SE2 = new Expense(20);
        SE3 = new Expense(5.5);
    }

    @Test
    public void testConstructor() {
        assertEquals("food", testCategory.getLabel());
        assertEquals(0, testCategory.getExpenses().size());
    }

    @Test
    public void testAddOne() {
        testCategory.add(SE1);

        List<Expense> entries = testCategory.getExpenses();
        assertEquals(1, entries.size());
        assertEquals(SE1, entries.get(0));
    }

    @Test
    public void testAddMultiple() {
        testCategory.add(SE1);
        testCategory.add(SE2);
        testCategory.add(SE3);

        List<Expense> entries = testCategory.getExpenses();
        assertEquals(3, entries.size());
        assertEquals(SE1, entries.get(0));
        assertEquals(SE2, entries.get(1));
        assertEquals(SE3, entries.get(2));
    }

    @Test
    public void testRemoveOne() {
        testCategory.add(SE1);
        testCategory.add(SE2);
        testCategory.add(SE3);

        testCategory.remove(SE2);

        List<Expense> entries = testCategory.getExpenses();
        assertEquals(2, entries.size());
        assertEquals(SE1, entries.get(0));
        assertEquals(SE3, entries.get(1));
    }

    @Test
    public void testRemoveAll() {
        testCategory.add(SE1);
        testCategory.add(SE2);
        testCategory.add(SE3);

        testCategory.remove(SE1);
        testCategory.remove(SE2);
        testCategory.remove(SE3);

        List<Expense> entries = testCategory.getExpenses();
        assertEquals(0, entries.size());
    }

    @Test
    public void testGetTotalOne() {
        testCategory.add(SE1);

        assertEquals(100, testCategory.totalAmount());
    }

    @Test
    public void testGetTotalMultiple() {
        testCategory.add(SE1);
        testCategory.add(SE2);
        testCategory.add(SE3);

        assertEquals(100 + 20 + 5.5, testCategory.totalAmount());
    }
}
