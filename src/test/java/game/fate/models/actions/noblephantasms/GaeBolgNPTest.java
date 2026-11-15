package game.fate.models.actions.noblephantasms;

import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;
import game.fate.models.servant.ServantClass;
import game.fate.testutils.TestServants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GaeBolgNP
 */
public class GaeBolgNPTest {
    private GaeBolgNP noblePhantasm;
    private Servant source;
    private Servant target;

    /**
     * Creates the noble phantasm, a LANCER (100 ATK, 100 MP) and a target with 1000 max HP before each test.
     */
    @BeforeEach
    void setUp() {
        noblePhantasm = new GaeBolgNP();
        source = TestServants.create("Lancer", ServantClass.LANCER, 1000, 100, 100);
        target = TestServants.create("Target", ServantClass.ARCHER, 1000, 100, 100);
    }

    /**
     * Name, type and MP cost.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("NP: Gae Bolg", noblePhantasm.getName());
        assertEquals(ActionType.NP, noblePhantasm.getType());
        assertEquals(30, noblePhantasm.getMpCost());
    }

    /**
     * Against a healthy target the damage is 1.5 x ATK (150) and the message does not announce a kill.
     */
    @Test
    void dealsTheExpectedDamageAgainstAHealthyTarget() {
        ActionResult result = noblePhantasm.execute(source, target);

        assertEquals(850, target.getHp());
        assertEquals(-150, result.targetHealthChange());
        assertFalse(result.targetDefeated());
        assertFalse(result.specialMessage().contains("Instant Kill"));
    }

    /**
     * A target below 30% of its max HP (200 of 1000) is killed instantly, whatever the ATK of the user.
     */
    @Test
    void instantlyKillsATargetBelowThirtyPercentHp() {
        Servant weakAttacker = TestServants.create("Weak", ServantClass.LANCER, 1000, 100, 1);
        target.takeDamage(800);

        ActionResult result = noblePhantasm.execute(weakAttacker, target);

        assertEquals(0, target.getHp());
        assertFalse(target.isAlive());
        assertEquals(-200, result.targetHealthChange());
        assertTrue(result.targetDefeated());
        assertTrue(result.specialMessage().contains("Instant Kill"));
    }

    /**
     * Edge case: a target with exactly 30% of its max HP (300 of 1000) is still killed instantly.
     */
    @Test
    void instantlyKillsATargetAtExactlyThirtyPercentHp() {
        target.takeDamage(700);

        ActionResult result = noblePhantasm.execute(source, target);

        assertFalse(target.isAlive());
        assertTrue(result.targetDefeated());
    }

    /**
     * Edge case: a target with 301 of 1000 HP (just above the threshold) only takes the normal damage.
     */
    @Test
    void doesNotInstantlyKillATargetJustAboveThirtyPercentHp() {
        target.takeDamage(699);

        ActionResult result = noblePhantasm.execute(source, target);

        assertEquals(151, target.getHp());
        assertTrue(target.isAlive());
        assertFalse(result.targetDefeated());
    }

    /**
     * A target with 1 HP is killed instantly too.
     */
    @Test
    void instantlyKillsATargetWithOneHp() {
        target.takeDamage(999);

        ActionResult result = noblePhantasm.execute(source, target);

        assertTrue(result.targetDefeated());
        assertEquals(-1, result.targetHealthChange());
    }

    /**
     * A normal (non-instant kill) hit can still be lethal if the damage is high enough.
     */
    @Test
    void normalDamageCanStillBeLethal() {
        Servant strongLancer = TestServants.create("Strong", ServantClass.LANCER, 1000, 100, 1000);
        Servant tank = TestServants.create("Tank", ServantClass.ARCHER, 1000, 100, 10);

        ActionResult result = noblePhantasm.execute(strongLancer, tank);

        assertTrue(result.targetDefeated());
        assertEquals(0, tank.getHp());
    }

    /**
     * The user pays 30 MP and keeps its HP.
     */
    @Test
    void resultDescribesDamageAndMpCost() {
        ActionResult result = noblePhantasm.execute(source, target);

        assertEquals(70, source.getMp());
        assertEquals(1000, source.getHp());
        assertEquals(-30, result.sourceMpChange());
        assertEquals(0, result.sourceHealthChange());
        assertEquals(0, result.targetMpChange());
        assertFalse(result.isCritical());
        assertEquals("Lancer", result.sourceName());
        assertEquals("Target", result.targetName());
        assertEquals("NP: Gae Bolg", result.actionName());
    }
}
