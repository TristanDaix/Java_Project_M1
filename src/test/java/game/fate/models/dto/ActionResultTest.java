package game.fate.models.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ActionResult record.
 */
public class ActionResultTest {
    /**
     * Every constructor argument must be readable through the accessor with the matching name.
     */
    @Test
    void storesEveryComponentInTheRightAccessor() {
        ActionResult result = new ActionResult("Artoria", "Emiya", "Attack", -120, 15, -20, -30, true, false, "eeeeexxxxxcccccaaaaallllliiiiibbbbbuuuuurrrrr !!!");

        assertEquals("Artoria", result.sourceName());
        assertEquals("Emiya", result.targetName());
        assertEquals("Attack", result.actionName());
        assertEquals(-120, result.targetHealthChange());
        assertEquals(15, result.sourceHealthChange());
        assertEquals(-20, result.sourceMpChange());
        assertEquals(-30, result.targetMpChange());
        assertTrue(result.isCritical());
        assertFalse(result.targetDefeated());
        assertEquals("eeeeexxxxxcccccaaaaallllliiiiibbbbbuuuuurrrrr !!!", result.specialMessage());
    }

    /**
     * Actions without a message or target.
     */
    @Test
    void acceptsNullTargetNameAndNullMessage() {
        ActionResult result = new ActionResult("Medea", null, "Skill", 0, 0, 50, 0, false, false, null);

        assertNull(result.targetName());
        assertNull(result.specialMessage());
    }
}
