package game.fate.models.interactionRules;

import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;
import game.fate.models.servant.ServantClass;
import game.fate.testutils.RecordingAction;
import game.fate.testutils.TestServants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BreakVsDefenseRule: when a break meets a defense, only the break is executed and the defense fails.
 * The action is a RecordingAction so that the tests can verify which one was really executed.
 */
public class BreakVsDefenseRuleTest {
    private BreakVsDefenseRule rule;
    private Servant breaker;
    private Servant defender;
    private RecordingAction breakAction;
    private RecordingAction defense;

    /**
     * A fresh rule, two servants and two recording actions (the break deals 120 damage).
     */
    @BeforeEach
    void setUp() {
        rule = new BreakVsDefenseRule();
        breaker = TestServants.create("Breaker", ServantClass.SABER, 1000, 100, 100);
        defender = TestServants.create("Defender", ServantClass.LANCER, 1000, 100, 100);
        breakAction = new RecordingAction(ActionType.BREAK, "Break", 0, 120);
        defense = new RecordingAction(ActionType.DEFENSE, "Defense", 0, 0);
    }

    /**
     * The rule applies to a break against a defense, whatever the order of the two types.
     */
    @Test
    void matchesBreakAgainstDefenseInBothOrders() {
        assertTrue(rule.matches(ActionType.BREAK, ActionType.DEFENSE));
        assertTrue(rule.matches(ActionType.DEFENSE, ActionType.BREAK));
    }

    /**
     * The rule ignores every other combination of action types.
     */
    @Test
    void doesNotMatchOtherCombinations() {
        assertFalse(rule.matches(ActionType.ATTACK, ActionType.ATTACK));
        assertFalse(rule.matches(ActionType.ATTACK, ActionType.BREAK));
        assertFalse(rule.matches(ActionType.ATTACK, ActionType.DEFENSE));
        assertFalse(rule.matches(ActionType.BREAK, ActionType.BREAK));
        assertFalse(rule.matches(ActionType.DEFENSE, ActionType.DEFENSE));

        assertFalse(rule.matches(ActionType.SKILL, ActionType.ATTACK));
        assertFalse(rule.matches(ActionType.SKILL, ActionType.BREAK));
        assertFalse(rule.matches(ActionType.SKILL, ActionType.DEFENSE));
        assertFalse(rule.matches(ActionType.SKILL, ActionType.SKILL));
        assertFalse(rule.matches(ActionType.SKILL, ActionType.NP));

        assertFalse(rule.matches(ActionType.NP, ActionType.ATTACK));
        assertFalse(rule.matches(ActionType.NP, ActionType.BREAK));
        assertFalse(rule.matches(ActionType.NP, ActionType.DEFENSE));
        assertFalse(rule.matches(ActionType.NP, ActionType.SKILL));
        assertFalse(rule.matches(ActionType.NP, ActionType.NP));
    }

    /**
     * The break is executed once (breaker on defender) and the defense is not executed at all.
     */
    @Test
    void onlyTheBreakIsExecuted() {
        rule.resolve(breaker, breakAction, defender, defense);

        assertEquals(1, breakAction.getExecutionCount());
        assertSame(breaker, breakAction.getLastSource());
        assertSame(defender, breakAction.getLastTarget());
        assertEquals(0, defense.getExecutionCount());
    }

    /**
     * The defender takes the damage and the breaker is untouched.
     */
    @Test
    void theDefenderTakesTheDamage() {
        rule.resolve(breaker, breakAction, defender, defense);

        assertEquals(880, defender.getHp());
        assertEquals(1000, breaker.getHp());
    }

    /**
     * The result is the one of the break: names, action name and damage.
     */
    @Test
    void resultIsTheOneOfTheBreak() {
        ActionResult result = rule.resolve(breaker, breakAction, defender, defense);

        assertEquals("Breaker", result.sourceName());
        assertEquals("Defender", result.targetName());
        assertEquals("Break", result.actionName());
        assertEquals(-120, result.targetHealthChange());
        assertEquals(0, result.sourceHealthChange());
        assertFalse(result.isCritical());
        assertFalse(result.targetDefeated());
    }

    /**
     * The special message announces that the defense was broken and names both servants.
     */
    @Test
    void messageNamesBothServants() {
        ActionResult result = rule.resolve(breaker, breakAction, defender, defense);

        assertNotNull(result.specialMessage());
        assertTrue(result.specialMessage().contains("Breaker"));
        assertTrue(result.specialMessage().contains("Defender"));
    }

    /**
     * A lethal break flags the defender as defeated.
     */
    @Test
    void lethalBreakFlagsTheDefeat() {
        defender.takeDamage(950);

        ActionResult result = rule.resolve(breaker, breakAction, defender, defense);

        assertTrue(result.targetDefeated());
        assertFalse(defender.isAlive());
    }
}
