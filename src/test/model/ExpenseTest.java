package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static model.Expense.LABEL_OF_NO_CATEGORY;
import static model.Expense.NAME_OF_NO_PLACE;
import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    Expense E1;

    @BeforeEach
    public void runBefore() {
        E1 = new Expense();
        E1.setAmount(100);
    }

    @Test
    public void testConstructor() {
        assertEquals(100, E1.getAmount());
        assertEquals(LocalDate.now(), E1.getDate().toLocalDate());
        assertNull(E1.getPlace());
    }

    @Test
    public void testSetNoPlace() {
        E1.setNoPlace();
        assertEquals(NAME_OF_NO_PLACE, E1.getPlace());
    }

    @Test
    public void testSetNoCategory() {
        E1.setNoCategory();
        assertEquals(LABEL_OF_NO_CATEGORY, E1.getCategory());
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
        E1.setCategory("clothing");
        assertEquals("today (" + today + ") you spent $100.0 at \"ubc\"" +
                " in the category \"clothing\"", E1.getSummary());
    }

    @Test
    public void testGetSummaryYesterday() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        E1.setDate(yesterday.toString());
        E1.setPlace("ubc");
        E1.setCategory("clothing");
        assertEquals("yesterday (" + yesterday + ") you spent $100.0 at \"ubc\"" +
                " in the category \"clothing\"", E1.getSummary());
    }

    @Test
    public void testGetSummaryThreeDaysAgo() {
        LocalDate threeDaysAgo = LocalDate.now().minusDays(3);
        E1.setDate(threeDaysAgo.toString());
        E1.setPlace("ubc");
        E1.setCategory("clothing");
        assertEquals("3 days ago (" + threeDaysAgo + ") you spent $100.0 at \"ubc\"" +
                " in the category \"clothing\"", E1.getSummary());
    }
}