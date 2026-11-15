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
 * Unit tests for BreakAction.
 * Same approach as AttackActionTest: the damage has a random part (-10 to +10), so tests check ranges.
 * Unlike a normal attack, a break can never be critical.
 */
public class BreakActionTest {
    private ServantClassSnapshot snapshot;
    private BreakAction action;
    private Servant target;

    /**
     * Prepares neutral class statistics, a fresh action and a target with 1000 HP.
     */
    @BeforeEach
    void setUp() {
        snapshot = ServantClassSnapshot.capture();
        ServantClassSnapshot.useNeutralStats();
        action = new BreakAction();
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
     * The action is called "Break", is a BREAK type action and is free.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("Break", action.getName());
        assertEquals(ActionType.BREAK, action.getType());
        assertEquals(0, action.getMpCost());
    }

    /**
     * The damage is the ATK value plus a random variation of -10 to +10.
     */
    @RepeatedTest(50)
    void damageIsAtkPlusOrMinusTen() {
        action.execute(TestServants.attacker(), target);

        assertTrue(hpLost() >= 90 && hpLost() <= 110, "damage out of range: " + hpLost());
    }

    /**
     * The attack multiplier of the servant class scales the damage (x2.0 gives 200 +/- 10).
     */
    @RepeatedTest(20)
    void damageUsesTheClassAttackMultiplier() {
        ServantClass.SABER.setStats(1, 2.0, 1.0, 0.0);

        action.execute(TestServants.attacker(), target);

        assertTrue(hpLost() >= 190 && hpLost() <= 210, "damage out of range: " + hpLost());
    }

    /**
     * Even a servant with a critical rate of 1.0 and a x5 multiplier never deals a critical break.
     */
    @RepeatedTest(30)
    void neverCriticalEvenWithMaximalCriticalStats() {
        Servant source = TestServants.create("Source", ServantClass.SABER, 1000, 100, 100, 5.0, 1.0);

        ActionResult result = action.execute(source, target);

        assertFalse(result.isCritical());
        assertTrue(hpLost() <= 110, "no critical bonus expected: " + hpLost());
    }

    /**
     * The result reports the damage as a negative health change equal to the real HP lost, with names and no message.
     */
    @RepeatedTest(20)
    void resultReportsDamageNamesAndNoMessage() {
        ActionResult result = action.execute(TestServants.attacker(), target);

        assertEquals(-hpLost(), result.targetHealthChange());
        assertEquals("Attacker", result.sourceName());
        assertEquals("Defender", result.targetName());
        assertEquals("Break", result.actionName());
        assertNull(result.specialMessage());
    }

    /**
     * The break costs nothing and does not change the source servant.
     */
    @Test
    void breakDoesNotChangeTheSource() {
        Servant source = TestServants.attacker();

        ActionResult result = action.execute(source, target);

        assertEquals(1000, source.getHp());
        assertEquals(100, source.getMp());
        assertEquals(0, result.sourceHealthChange());
        assertEquals(0, result.sourceMpChange());
    }

    /**
     * A lethal break defeats the target and the result flags it.
     */
    @Test
    void lethalBreakDefeatsTheTarget() {
        Servant weakTarget = TestServants.create("Weak", ServantClass.LANCER, 5, 10, 10);

        ActionResult result = action.execute(TestServants.attacker(), weakTarget);

        assertFalse(weakTarget.isAlive());
        assertTrue(result.targetDefeated());
    }
}
