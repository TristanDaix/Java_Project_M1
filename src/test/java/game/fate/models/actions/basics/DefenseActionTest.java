package game.fate.models.actions.basics;

import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;
import game.fate.testutils.TestServants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DefenseAction. On its own the defense does nothing: the counter-attack
 * against an attack is added by DefenseVsAttackRule.
 */
public class DefenseActionTest {
    private DefenseAction action;
    private Servant source;
    private Servant target;

    /**
     * Creates the action and two fresh servants before each test.
     */
    @BeforeEach
    void setUp() {
        action = new DefenseAction();
        source = TestServants.defender();
        target = TestServants.attacker();
    }

    /**
     * The action is called "Defense", is a DEFENSE type action and is free.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("Defense", action.getName());
        assertEquals(ActionType.DEFENSE, action.getType());
        assertEquals(0, action.getMpCost());
    }

    /**
     * Executing the defense changes neither servant (HP and MP stay the same).
     */
    @Test
    void executeDoesNotChangeAnyServant() {
        action.execute(source, target);

        assertEquals(1000, source.getHp());
        assertEquals(100, source.getMp());
        assertEquals(1000, target.getHp());
        assertEquals(100, target.getMp());
    }

    /**
     * The result contains the names, no health or MP change, no critical flag and no message.
     */
    @Test
    void resultIsAnEmptyOutcomeWithNames() {
        ActionResult result = action.execute(source, target);

        assertEquals("Defender", result.sourceName());
        assertEquals("Attacker", result.targetName());
        assertEquals("Defense", result.actionName());
        assertEquals(0, result.targetHealthChange());
        assertEquals(0, result.sourceHealthChange());
        assertEquals(0, result.sourceMpChange());
        assertEquals(0, result.targetMpChange());
        assertFalse(result.isCritical());
        assertNull(result.specialMessage());
    }

    /**
     * A defense does not flag the target as defeated.
     */
    @Test
    void DefenseDoesNotDefeatTheTarget() {
        ActionResult result = action.execute(source, target);

        assertTrue(target.isAlive());
        assertFalse(result.targetDefeated());
    }
}
