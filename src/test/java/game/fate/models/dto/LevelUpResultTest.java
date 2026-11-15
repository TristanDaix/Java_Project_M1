package game.fate.models.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the LevelUpResult record
 */
public class LevelUpResultTest {

    /**
     * Each "old" and "new" value must be returned by its own accessor
     */
    @Test
    void storesEveryComponentInTheRightAccessor() {
        LevelUpResult result = new LevelUpResult(true, 1, 2, 100, 120, 1000, 1200, 80, 96);

        assertTrue(result.leveledUp());
        assertEquals(1, result.oldLevel());
        assertEquals(2, result.newLevel());
        assertEquals(100, result.oldAtk());
        assertEquals(120, result.newAtk());
        assertEquals(1000, result.oldHp());
        assertEquals(1200, result.newHp());
        assertEquals(80, result.oldMp());
        assertEquals(96, result.newMp());
    }

    /**
     * A result built without any level boundary crossed reports.
     */
    @Test
    void canDescribeAnExperienceGainWithoutLevelUp() {
        LevelUpResult result = new LevelUpResult(false, 3, 3, 10, 10, 20, 20, 30, 30);

        assertFalse(result.leveledUp());
        assertEquals(result.oldLevel(), result.newLevel());
    }
}
