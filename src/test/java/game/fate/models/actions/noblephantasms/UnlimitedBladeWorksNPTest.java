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
 * Unit tests for UnlimitedBladeWorksNP
 */
public class UnlimitedBladeWorksNPTest {
    private UnlimitedBladeWorksNP noblePhantasm;
    private Servant target;

    /**
     * Creates the noble phantasm and a 1000 HP target before each test.
     */
    @BeforeEach
    void setUp() {
        noblePhantasm = new UnlimitedBladeWorksNP();
        target = TestServants.defender();
    }

    /**
     * Name, type and MP cost.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("NP: Unlimited Blade Works", noblePhantasm.getName());
        assertEquals(ActionType.NP, noblePhantasm.getType());
        assertEquals(40, noblePhantasm.getMpCost());
    }

    /**
     * With 100 ATK the target loses 360 HP (3.6 x ATK).
     */
    @Test
    void dealsTheExpectedDamage() {
        Servant source = TestServants.create("Archer", ServantClass.ARCHER, 1000, 100, 100);

        noblePhantasm.execute(source, target);

        assertEquals(640, target.getHp());
    }

    /**
     * The user pays 40 MP and keeps its HP.
     */
    @Test
    void chargesMpAndKeepsHp() {
        Servant source = TestServants.create("Archer", ServantClass.ARCHER, 1000, 100, 100);

        noblePhantasm.execute(source, target);

        assertEquals(60, source.getMp());
        assertEquals(1000, source.getHp());
    }

    /**
     * The result describes the damage and the MP cost with the right sign, without critical hit.
     */
    @Test
    void resultDescribesDamageAndMpCost() {
        Servant source = TestServants.create("Archer", ServantClass.ARCHER, 1000, 100, 100);

        ActionResult result = noblePhantasm.execute(source, target);

        assertEquals("Archer", result.sourceName());
        assertEquals("Defender", result.targetName());
        assertEquals("NP: Unlimited Blade Works", result.actionName());
        assertEquals(-360, result.targetHealthChange());
        assertEquals(0, result.sourceHealthChange());
        assertEquals(-40, result.sourceMpChange());
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
        Servant weakTarget = TestServants.create("Weak", ServantClass.LANCER, 100, 10, 10);
        Servant source = TestServants.create("Archer", ServantClass.ARCHER, 1000, 100, 100);

        ActionResult result = noblePhantasm.execute(source, weakTarget);

        assertTrue(result.targetDefeated());
        assertFalse(weakTarget.isAlive());
    }
}
