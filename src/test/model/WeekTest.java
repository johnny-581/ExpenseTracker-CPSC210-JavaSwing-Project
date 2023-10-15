package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WeekTest {
    List<Expense> allExpenses;
    List<Category> allCategories;
    LocalDate today;
    LocalDate startOfThisWeek;
    LocalDate startOfLastWeek;
    Week testThisWeek;
    Week testLastWeek;

    Expense E1;
    Expense E2;
    Expense E3;
    Expense E4;
    Expense E5;


    @BeforeEach
    public void runBefore() {
        allExpenses = new ArrayList<>();
        allCategories = new ArrayList<>();

        today = LocalDate.now();
        startOfThisWeek = today.minusDays(6);
        startOfLastWeek = today.minusDays(13);

        initi();

        testThisWeek = new Week(startOfThisWeek, allExpenses);
        testLastWeek = new Week(startOfLastWeek, allExpenses);
    }

    @Test
    // Also tests findExpensesInWeek(), which is called by the constructor
    public void testConstructorThisWeek() {
        assertEquals(startOfThisWeek.toString(), testThisWeek.getStartDate());
        assertEquals(today.toString(), testThisWeek.getEndDate());

        List<Expense> expenses = testThisWeek.getExpensesInWeek();
        assertEquals(3, expenses.size());
        assertTrue(expenses.contains(E1));
        assertTrue(expenses.contains(E4));
        assertTrue(expenses.contains(E5));
    }

    @Test
    // Also tests findExpensesInWeek(), which is called by the constructor
    public void testConstructorLastWeek() {
        assertEquals(startOfLastWeek.toString(), testLastWeek.getStartDate());
        assertEquals(startOfLastWeek.plusDays(6).toString(), testLastWeek.getEndDate());

        List<Expense> expenses = testLastWeek.getExpensesInWeek();
        assertEquals(1, expenses.size());
        assertTrue(expenses.contains(E3));
    }

    @Test
    public void testGetNumOfExpenses() {
        assertEquals(3, testThisWeek.getNumOfExpenses());
        assertEquals(1, testLastWeek.getNumOfExpenses());
    }

    @Test
    public void testGetTotalAmount() {
        assertEquals(100 + 210 + 1000, testThisWeek.getTotalAmount());
        assertEquals(55, testLastWeek.getTotalAmount());
    }

    @Test
    public void testIsInThisWeekThisWeek() {
        assertTrue(testThisWeek.isInThisWeek(E1));
        assertFalse(testThisWeek.isInThisWeek(E2));
        assertFalse(testThisWeek.isInThisWeek(E3));
        assertTrue(testThisWeek.isInThisWeek(E4));
        assertTrue(testThisWeek.isInThisWeek(E5));
    }

    @Test
    public void testIsInThisWeekLastWeek() {
        assertFalse(testLastWeek.isInThisWeek(E1));
        assertFalse(testLastWeek.isInThisWeek(E2));
        assertTrue(testLastWeek.isInThisWeek(E3));
        assertFalse(testLastWeek.isInThisWeek(E4));
        assertFalse(testLastWeek.isInThisWeek(E5));
    }


    public void initi() {
        E1 = new Expense();
        E2 = new Expense();
        E3 = new Expense();
        E4 = new Expense();
        E5 = new Expense();
        allExpenses.add(E1);
        allExpenses.add(E2);
        allExpenses.add(E3);
        allExpenses.add(E4);
        allExpenses.add(E5);

        E1.setAmount(100);
        E1.setPlace("no frills");
        E1.setCategory("grocery");

        E2.setAmount(20);
        E2.setDate(today.minusMonths(1).toString());
        E2.setPlace("save on food");

        E3.setAmount(55);
        E3.setDate(today.minusDays(7).toString());
        E3.setPlace("ubc book store");

        E4.setAmount(210);
        E4.setDate(today.minusDays(3).toString());
        E4.setPlace("lululemon");

        E5.setAmount(1000);
        E5.setDate(today.minusDays(6).toString());
        E5.setPlace("ubc housing");
    }
}
