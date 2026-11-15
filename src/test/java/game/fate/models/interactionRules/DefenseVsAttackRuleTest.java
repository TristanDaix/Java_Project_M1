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
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DefenseVsAttackRule: when a defense meets an attack, the attack is blocked and the defender launches
 * a counter-attack with a AttackAction.
 */
public class DefenseVsAttackRuleTest {
    private ServantClassSnapshot snapshot;
    private DefenseVsAttackRule rule;
    private Servant defender;
    private Servant attacker;
    private RecordingAction defense;
    private RecordingAction attack;

    /**
     * Neutral class stats, a fresh rule, two servants and two recording actions.
     */
    @BeforeEach
    void setUp() {
        snapshot = ServantClassSnapshot.capture();
        ServantClassSnapshot.useNeutralStats();
        rule = new DefenseVsAttackRule();
        defender = TestServants.create("Defender", ServantClass.LANCER, 1000, 100, 100);
        attacker = TestServants.create("Attacker", ServantClass.SABER, 1000, 100, 100);
        defense = new RecordingAction(ActionType.DEFENSE, "Defense", 0, 0);
        attack = new RecordingAction(ActionType.ATTACK, "Attack", 0, 500);
    }

    /**
     * Restores the original class statistics.
     */
    @AfterEach
    void tearDown() {
        snapshot.restore();
    }

    /**
     * The rule applies to a defense against an attack, whatever the order of the two types.
     */
    @Test
    void matchesDefenseAgainstAttackInBothOrders() {
        assertTrue(rule.matches(ActionType.DEFENSE, ActionType.ATTACK));
        assertTrue(rule.matches(ActionType.ATTACK, ActionType.DEFENSE));
    }

    /**
     * The rule ignores every other combination of action types.
     */
    @Test
    void doesNotMatchOtherCombinations() {
        assertFalse(rule.matches(ActionType.ATTACK, ActionType.ATTACK));
        assertFalse(rule.matches(ActionType.ATTACK, ActionType.BREAK));
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
     * The defense is executed once (defender on attacker) and the blocked attack is never executed.
     */
    @Test
    void defenseIsExecutedAndTheAttackIsBlocked() {
        rule.resolve(defender, defense, attacker, attack);

        assertEquals(1, defense.getExecutionCount());
        assertSame(defender, defense.getLastSource());
        assertSame(attacker, defense.getLastTarget());
        assertEquals(0, attack.getExecutionCount());
    }

    /**
     * The blocked attack does not hurt the defender.
     */
    @Test
    void defenderIsNotHurt() {
        rule.resolve(defender, defense, attacker, attack);

        assertEquals(1000, defender.getHp());
    }

    /**
     * The counter-attack uses the ATK of the defender: 100 ATK deals 90 to 110 damage to the attacker.
     */
    @RepeatedTest(40)
    void counterAttackDamagesTheAttacker() {
        rule.resolve(defender, defense, attacker, attack);

        int lost = 1000 - attacker.getHp();
        assertTrue(lost >= 90 && lost <= 110, "counter-attack damage out of range: " + lost);
    }

    /**
     * The result is the one of the defense mixed with the counter-attack.
     */
    @RepeatedTest(20)
    void resultDescribesTheCounterAttack() {
        ActionResult result = rule.resolve(defender, defense, attacker, attack);

        assertEquals("Defender", result.sourceName());
        assertEquals("Attacker", result.targetName());
        assertEquals("Defense", result.actionName());
        assertEquals(attacker.getHp() - 1000, result.targetHealthChange());
        assertEquals(0, result.sourceHealthChange());
        assertFalse(result.isCritical());
        assertFalse(result.targetDefeated());
    }

    /**
     * The special message tells that the attack was blocked and names both servants.
     */
    @Test
    void messageNamesBothServants() {
        ActionResult result = rule.resolve(defender, defense, attacker, attack);

        assertNotNull(result.specialMessage());
        assertTrue(result.specialMessage().contains("Attacker"));
        assertTrue(result.specialMessage().contains("Defender"));
    }

    /**
     * A counter-attack that kills the attacker flags it as defeated.
     */
    @Test
    void lethalCounterAttackFlagsTheDefeat() {
        attacker.takeDamage(995);

        ActionResult result = rule.resolve(defender, defense, attacker, attack);

        assertTrue(result.targetDefeated());
        assertFalse(attacker.isAlive());
    }
}
