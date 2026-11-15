package game.fate.models.actions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ActionType enumeration used by the interaction rules and by the combat patterns .
 */
public class ActionTypeTest {
    /**
     * The game knows exactly five kinds of actions, declared in this order.
     */
    @Test
    void declaresTheFiveActionTypesInOrder() {
        ActionType[] expected = {ActionType.ATTACK, ActionType.DEFENSE, ActionType.BREAK, ActionType.SKILL, ActionType.NP};

        assertArrayEquals(expected, ActionType.values());
    }

    /**
     * valueOf finds a constant from its exact upper-case name.
     */
    @Test
    void valueOfFindsConstantByExactName() {
        assertEquals(ActionType.ATTACK, ActionType.valueOf("ATTACK"));
        assertEquals(ActionType.DEFENSE, ActionType.valueOf("DEFENSE"));
        assertEquals(ActionType.BREAK, ActionType.valueOf("BREAK"));
        assertEquals(ActionType.SKILL, ActionType.valueOf("SKILL"));
        assertEquals(ActionType.NP, ActionType.valueOf("NP"));
    }

    /**
     * valueOf do not find a constant from a case-sensitive name.
     */
    @Test
    void valueOfDoNotFindsConstantByCaseSensitiveName() {
        assertThrows(IllegalArgumentException.class, () -> ActionType.valueOf("Attack"));
    }

    /**
     * An unknown name is rejected.
     */
    @Test
    void valueOfRejectsUnknownName() {
        assertThrows(IllegalArgumentException.class, () -> ActionType.valueOf("PutANoteOf20/20"));
    }
}
