package game.fate.models.interactionRules;

import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;
import game.fate.models.servant.ServantClass;
import game.fate.testutils.RecordingAction;
import game.fate.testutils.ServantClassSnapshot;
import game.fate.testutils.TestServants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AttackVsBreakRule: when an attack meets a break, only the attack is executed and the break fails.
 * The action is a RecordingAction so that the tests can verify which one was really executed.
 */
public class AttackVsBreakRuleTest {
    private ServantClassSnapshot snapshot;
    private AttackVsBreakRule rule;
    private Servant attacker;
    private Servant breaker;
    private RecordingAction attack;
    private RecordingAction breakAction;

    /**
     * Neutral class stats, a fresh rule, two servants and two recording actions (the attack deals 120 damage).
     */
    @BeforeEach
    void setUp() {
        snapshot = ServantClassSnapshot.capture();
        ServantClassSnapshot.useNeutralStats();
        rule = new AttackVsBreakRule();
        attacker = TestServants.create("Attacker", ServantClass.SABER, 1000, 100, 100);
        breaker = TestServants.create("Breaker", ServantClass.LANCER, 1000, 100, 100);
        attack = new RecordingAction(ActionType.ATTACK, "Attack", 0, 120);
        breakAction = new RecordingAction(ActionType.BREAK, "Break", 0, 120);
    }

    /**
     * Restores the original class statistics.
     */
    @AfterEach
    void tearDown() {
        snapshot.restore();
    }

    /**
     * The rule applies to an attack against a break, whatever the order of the two types.
     */
    @Test
    void matchesAttackAgainstBreakInBothOrders() {
        assertTrue(rule.matches(ActionType.ATTACK, ActionType.BREAK));
        assertTrue(rule.matches(ActionType.BREAK, ActionType.ATTACK));
    }

    /**
     * The rule ignores every other combination of action types.
     */
    @Test
    void doesNotMatchOtherCombinations() {
        assertFalse(rule.matches(ActionType.ATTACK, ActionType.ATTACK));
        assertFalse(rule.matches(ActionType.ATTACK, ActionType.DEFENSE));
        assertFalse(rule.matches(ActionType.BREAK, ActionType.BREAK));
        assertFalse(rule.matches(ActionType.BREAK, ActionType.DEFENSE));
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
     * The attack is executed once (attacker on breaker) and the break is not executed at all.
     */
    @Test
    void onlyTheAttackIsExecuted() {
        rule.resolve(attacker, attack, breaker, breakAction);

        assertEquals(1, attack.getExecutionCount());
        assertSame(attacker, attack.getLastSource());
        assertSame(breaker, attack.getLastTarget());
        assertEquals(0, breakAction.getExecutionCount());
    }

    /**
     * The servant who used the break takes the damage and the attacker is untouched.
     */
    @Test
    void theBreakerTakesTheDamage() {
        rule.resolve(attacker, attack, breaker, breakAction);

        assertEquals(880, breaker.getHp());
        assertEquals(1000, attacker.getHp());
    }

    /**
     * The result is the one of the attack: names, action name and damage.
     */
    @Test
    void resultIsTheOneOfTheAttack() {
        ActionResult result = rule.resolve(attacker, attack, breaker, breakAction);

        assertEquals("Attacker", result.sourceName());
        assertEquals("Breaker", result.targetName());
        assertEquals("Attack", result.actionName());
        assertEquals(-120, result.targetHealthChange());
        assertEquals(0, result.sourceHealthChange());
        assertFalse(result.targetDefeated());
    }

    /**
     * The special message explains that the break failed and names both servants.
     */
    @Test
    void messageNamesBothServants() {
        ActionResult result = rule.resolve(attacker, attack, breaker, breakAction);

        assertNotNull(result.specialMessage());
        assertTrue(result.specialMessage().contains("Breaker"));
        assertTrue(result.specialMessage().contains("Attacker"));
    }

    /**
     * A lethal attack flags the servant who used the break as defeated.
     */
    @Test
    void lethalAttackFlagsTheDefeat() {
        breaker.takeDamage(950);

        ActionResult result = rule.resolve(attacker, attack, breaker, breakAction);

        assertTrue(result.targetDefeated());
        assertFalse(breaker.isAlive());
    }
}
