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
 * Unit tests for NineLivesNP
 */
public class NineLivesNPTest {
    private NineLivesNP noblePhantasm;
    private Servant target;

    /**
     * Creates the noble phantasm and a 1000 HP target before each test.
     */
    @BeforeEach
    void setUp() {
        noblePhantasm = new NineLivesNP();
        target = TestServants.defender();
    }

    /**
     * Name, type and MP cost.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("NP: Nine Lives", noblePhantasm.getName());
        assertEquals(ActionType.NP, noblePhantasm.getType());
        assertEquals(50, noblePhantasm.getMpCost());
    }

    /**
     * The target loses 3.0 x ATK HP (100 ATK gives 300 damage).
     */
    @Test
    void dealsTheExpectedDamage() {
        Servant source = TestServants.create("User", ServantClass.BERSERKER, 1000, 100, 100);

        noblePhantasm.execute(source, target);

        assertEquals(700, target.getHp());
    }

    /**
     * The user pays the MP cost and keeps its HP.
     */
    @Test
    void chargesMpAndKeepsHp() {
        Servant source = TestServants.create("User", ServantClass.BERSERKER, 1000, 100, 100);

        noblePhantasm.execute(source, target);

        assertEquals(50, source.getMp());
        assertEquals(1000, source.getHp());
    }

    /**
     * The result describes the damage and the MP cost with the right sign, without critical hit.
     */
    @Test
    void resultDescribesDamageAndMpCost() {
        Servant source = TestServants.create("User", ServantClass.BERSERKER, 1000, 100, 100);

        ActionResult result = noblePhantasm.execute(source, target);

        assertEquals("User", result.sourceName());
        assertEquals("Defender", result.targetName());
        assertEquals("NP: Nine Lives", result.actionName());
        assertEquals(-300, result.targetHealthChange());
        assertEquals(0, result.sourceHealthChange());
        assertEquals(-50, result.sourceMpChange());
        assertEquals(0, result.targetMpChange());
        assertFalse(result.isCritical());
        assertFalse(result.targetDefeated());
        assertNotNull(result.specialMessage());
    }

    /**
     * A lethal hit defeats the target and the result flags it.
     */
    @Test
    void lethalHitDefeatsTheTarget() {
        Servant weakTarget = TestServants.create("Weak", ServantClass.ARCHER, 100, 10, 10);
        Servant source = TestServants.create("User", ServantClass.BERSERKER, 1000, 100, 100);

        ActionResult result = noblePhantasm.execute(source, weakTarget);

        assertTrue(result.targetDefeated());
        assertFalse(weakTarget.isAlive());
    }
}
