package game.fate.models.actions.basics;

import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;
import game.fate.models.servant.ServantClass;
import game.fate.testutils.ServantClassSnapshot;
import game.fate.testutils.TestServants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AttackAction.
 * The damage contains a random part (-10 to +10) and the critical hit is random, so the tests work with
 * ranges and with critical rates of exactly 0.0 (never critical) or 1.0 (always critical).
 * Repeated tests run the same check many times to be sure the whole random range stays valid.
 * All class statistics are set to neutral values and restored after each test.
 */
public class AttackActionTest {
    private ServantClassSnapshot snapshot;
    private AttackAction action;
    private Servant target;

    /**
     * Prepares neutral class statistics, a fresh action and a target with 1000 HP.
     */
    @BeforeEach
    void setUp() {
        snapshot = ServantClassSnapshot.capture();
        ServantClassSnapshot.useNeutralStats();
        action = new AttackAction();
        target = TestServants.defender();
    }

    /**
     * Restores the original class statistics.
     */
    @AfterEach
    void tearDown() {
        snapshot.restore();
    }

    private int hpLost() {
        return 1000 - target.getHp();
    }

    /**
     * The action is called Attack, is a ATTACK type action and is free.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("Attack", action.getName());
        assertEquals(ActionType.ATTACK, action.getType());
        assertEquals(0, action.getMpCost());
    }

    /**
     * Without bonus the damage is the ATK value plus a random variation of -10 to +10.
     */
    @RepeatedTest(50)
    void damageIsAtkPlusOrMinusTenWhenNotCritical() {
        Servant source = TestServants.create("Source", ServantClass.SABER, 1000, 100, 100);

        action.execute(source, target);

        assertTrue(hpLost() >= 90 && hpLost() <= 110, "damage out of range: " + hpLost());
    }

    /**
     * The attack multiplier of the servant class scales the base damage (x2.0 gives 200 +/- 10).
     */
    @RepeatedTest(20)
    void damageUsesTheClassAttackMultiplier() {
        ServantClass.SABER.setStats(1, 2.0, 1.0, 0.0);
        Servant source = TestServants.create("Source", ServantClass.SABER, 1000, 100, 100);

        action.execute(source, target);

        assertTrue(hpLost() >= 190 && hpLost() <= 210, "damage out of range: " + hpLost());
    }

    /**
     * The result describes the damage as a negative health change of the target, equal to the real HP lost.
     */
    @RepeatedTest(20)
    void resultReportsTheDamageDealtAsNegativeHealthChange() {
        Servant source = TestServants.attacker();

        ActionResult result = action.execute(source, target);

        assertEquals(-hpLost(), result.targetHealthChange());
    }

    /**
     * The result carries the names of both servants and of the action, and no special message.
     */
    @Test
    void resultCarriesNamesAndNoSpecialMessage() {
        ActionResult result = action.execute(TestServants.attacker(), target);

        assertEquals("Attacker", result.sourceName());
        assertEquals("Defender", result.targetName());
        assertEquals("Attack", result.actionName());
        assertNull(result.specialMessage());
    }

    /**
     * An attack costs nothing and does not change the attacker (no recoil, no MP use, no target MP change).
     */
    @Test
    void attackDoesNotChangeTheAttacker() {
        Servant source = TestServants.attacker();

        ActionResult result = action.execute(source, target);

        assertEquals(1000, source.getHp());
        assertEquals(100, source.getMp());
        assertEquals(0, result.sourceHealthChange());
        assertEquals(0, result.sourceMpChange());
        assertEquals(0, result.targetMpChange());
    }

    /**
     * With a total critical rate of 0.0 the attack is never critical.
     */
    @RepeatedTest(50)
    void neverCriticalWhenCriticalRateIsZero() {
        Servant source = TestServants.create("Source", ServantClass.SABER, 1000, 100, 100, 3.0, 0.0);

        ActionResult result = action.execute(source, target);

        assertFalse(result.isCritical());
        assertTrue(hpLost() <= 110, "no critical bonus expected: " + hpLost());
    }

    /**
     * With a servant critical rate of 1.0 the attack is always critical and the damage is multiplied by
     * the critical multiplier (x2.0: 90-110 becomes 180-220).
     */
    @RepeatedTest(50)
    void alwaysCriticalWhenCriticalRateIsOne() {
        Servant source = TestServants.create("Source", ServantClass.SABER, 1000, 100, 100, 2.0, 1.0);

        ActionResult result = action.execute(source, target);

        assertTrue(result.isCritical());
        assertTrue(hpLost() >= 180 && hpLost() <= 220, "critical damage out of range: " + hpLost());
    }

    /**
     * The critical rate of the class is added to the critical rate of the servant
     * (0.0 + 1.0 = always critical).
     */
    @RepeatedTest(30)
    void classCriticalRateIsAddedToTheServantRate() {
        ServantClass.SABER.setStats(1, 1.0, 1.0, 1.0);
        Servant source = TestServants.create("Source", ServantClass.SABER, 1000, 100, 100, 1.5, 0.0);

        ActionResult result = action.execute(source, target);

        assertTrue(result.isCritical());
    }

    /**
     * The critical multiplier of the servant and the one of its class are multiplied together
     * (1.5 x 2.0 = x3.0: 90-110 becomes 270-330).
     */
    @RepeatedTest(30)
    void servantAndClassCriticalMultipliersStack() {
        ServantClass.SABER.setStats(1, 1.0, 2.0, 0.0);
        Servant source = TestServants.create("Source", ServantClass.SABER, 1000, 100, 100, 1.5, 1.0);

        ActionResult result = action.execute(source, target);

        assertTrue(result.isCritical());
        assertTrue(hpLost() >= 270 && hpLost() <= 330, "stacked critical damage out of range: " + hpLost());
    }

    /**
     * A lethal attack kills the target and the result flags the target as defeated.
     */
    @Test
    void lethalAttackDefeatsTheTarget() {
        Servant weakTarget = TestServants.create("Weak", ServantClass.LANCER, 5, 10, 10);

        ActionResult result = action.execute(TestServants.attacker(), weakTarget);

        assertFalse(weakTarget.isAlive());
        assertTrue(result.targetDefeated());
    }

    /**
     * A non-lethal attack does not flag the target as defeated.
     */
    @Test
    void nonLethalAttackDoesNotDefeatTheTarget() {
        ActionResult result = action.execute(TestServants.attacker(), target);

        assertTrue(target.isAlive());
        assertFalse(result.targetDefeated());
    }
}
