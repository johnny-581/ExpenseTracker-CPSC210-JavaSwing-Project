package model;

import exceptions.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.time.LocalDate;

import static model.Expense.NAME_OF_NO_PLACE;
import static model.ExpenseTracker.LABEL_OF_NO_CATEGORY;
import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    Expense E1;
    Category C1;
    Category C2;
    public Category categoryOfNoCategory;

    @BeforeEach
    public void runBefore() {
        categoryOfNoCategory = new Category(LABEL_OF_NO_CATEGORY);
        E1 = new Expense(categoryOfNoCategory, 100);
        C1 = new Category("clothing");
        C2 = new Category("grocery");
    }

    @Test
    public void testConstructor() {
        assertEquals(100, E1.getAmount());
        assertEquals(LocalDate.now(), E1.getDate().toLocalDate());
        assertEquals(NAME_OF_NO_PLACE, E1.getPlace());
        assertEquals(categoryOfNoCategory, E1.getCategory());
        assertEquals(categoryOfNoCategory, E1.getCoNC());
    }

    @Test
    public void testSetAmountValid() {
        try {
            E1.setAmount("200");
            assertEquals(200, E1.getAmount());
            // expected
        } catch (InvalidAmountException e) {
            fail("unexpected");
        }
    }

    @Test
    public void testSetAmountWrongFormat() {
        try {
            E1.setAmount("idd");
            fail("should throw exception");
        } catch (InvalidAmountException e) {
            // expected
        }
        assertEquals(100, E1.getAmount());
    }

    @Test
    public void testSetAmountNegativeAmount() {
        try {
            E1.setAmount("-39");
            fail("should throw exception");
        } catch (InvalidAmountException e) {
            // expected
        }
        assertEquals(100, E1.getAmount());
    }

    @Test
    public void testSetCategory() {
        E1.setCategory(C1);
        assertEquals(C1, E1.getCategory());
        assertTrue(C1.contains(E1));
        assertFalse(categoryOfNoCategory.contains(E1));
    }

    @Test
    public void testSetCategoryInAnotherCategory() {
        E1.setCategory(C1);
        E1.setCategory(C2);
        assertEquals(C2, E1.getCategory());
        assertFalse(C1.contains(E1));
        assertTrue(C2.contains(E1));
        assertFalse(categoryOfNoCategory.contains(E1));
    }

    @Test
    public void testRemoveCategory() {
        E1.setCategory(C1);
        E1.removeCategory();
        assertEquals(categoryOfNoCategory, E1.getCategory());
        assertFalse(C1.contains(E1));
    }

    @Test
    public void testSetNoPlace() {
        E1.setNoPlace();
        assertEquals(NAME_OF_NO_PLACE, E1.getPlace());
    }

    @Test
    public void testGetDaysPriorToTodaySameDay() {
        assertEquals(0, E1.getDaysPriorToToday());
    }

    @Test
    public void testGetDaysPriorToToday3DaysAgo() {
        String threeDaysPrior = LocalDate.now().minusDays(3).toString();

        E1.setDate(threeDaysPrior);
        assertEquals(3, E1.getDaysPriorToToday());
    }

    @Test
    public void testGetSummaryToday() {
        LocalDate today = LocalDate.now();
        E1.setDate(today.toString());
        E1.setPlace("ubc");
        E1.setCategory(C1);
        assertEquals("  today you spent $100.0 at \"ubc\"", E1.getSummary());
    }

    @Test
    public void testGetSummaryYesterday() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        E1.setDate(yesterday.toString());
        E1.setPlace("ubc");
        E1.setCategory(C1);
        assertEquals("  yesterday you spent $100.0 at \"ubc\"", E1.getSummary());
    }

    @Test
    public void testGetSummaryThreeDaysAgo() {
        LocalDate threeDaysAgo = LocalDate.now().minusDays(3);
        E1.setDate(threeDaysAgo.toString());
        E1.setPlace("ubc");
        E1.setCategory(C1);
        assertEquals("  3 days ago you spent $100.0 at \"ubc\"", E1.getSummary());
    }

    @Test
    public void testGetIcon() {
        assertTrue(E1.getIcon() instanceof ImageIcon);
    }
}