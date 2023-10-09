package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SpendingEntryTest {
    SpendingEntry testSE;

    @BeforeEach
    public void runBefore() {
        testSE = new SpendingEntry(100);
    }

    @Test
    public void testConstructor() {
        assertEquals(100, testSE.getAmount());
        assertEquals(LocalDate.now(), testSE.getDate());
        assertNull(testSE.getPlace());
    }

    @Test
    public void getDaysPriorToTodaySameDay() {
        assertEquals(0, testSE.getDaysPriorToToday());
    }

    @Test
    public void getDaysPriorToToday3DaysAgo() {
        String threeDaysPrior = LocalDate.now().minusDays(3).toString();

        testSE.setDate(threeDaysPrior);
        assertEquals(3, testSE.getDaysPriorToToday());
    }
}