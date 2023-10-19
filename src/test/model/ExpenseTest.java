package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static model.Expense.LABEL_OF_NO_CATEGORY;
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
        assertEquals(LocalDate.now(), E1.getDate());
        assertNull(E1.getPlace());
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
}