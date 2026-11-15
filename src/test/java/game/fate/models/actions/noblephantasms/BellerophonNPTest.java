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
 * Unit tests for BellerophonNP
 */
public class BellerophonNPTest {
    private BellerophonNP noblePhantasm;
    private Servant source;
    private Servant target;

    /**
     * Creates the noble phantasm, a RIDER (1000 HP, 100 MP, 100 ATK) and a 1000 HP target before each test.
     */
    @BeforeEach
    void setUp() {
        noblePhantasm = new BellerophonNP();
        source = TestServants.create("Rider", ServantClass.RIDER, 1000, 100, 100);
        target = TestServants.defender();
    }

    /**
     * Name, type and MP cost.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("NP: Bellerophon", noblePhantasm.getName());
        assertEquals(ActionType.NP, noblePhantasm.getType());
        assertEquals(40, noblePhantasm.getMpCost());
    }

    /**
     * The target loses 2.5 x ATK HP (100 ATK gives 250 damage).
     */
    @Test
    void dealsTheExpectedDamage() {
        noblePhantasm.execute(source, target);

        assertEquals(750, target.getHp());
    }

    /**
     * The user recovers half of the damage dealt (125) and pays 40 MP.
     */
    @Test
    void healsTheUserForHalfOfTheDamageAndChargesMp() {
        source.takeDamage(500);

        noblePhantasm.execute(source, target);

        assertEquals(625, source.getHp());
        assertEquals(60, source.getMp());
    }

    /**
     * The result describes damage, healing of the source and MP cost with the right sign.
     */
    @Test
    void resultDescribesDamageHealingAndMpCost() {
        source.takeDamage(500);

        ActionResult result = noblePhantasm.execute(source, target);

        assertEquals("Rider", result.sourceName());
        assertEquals("Defender", result.targetName());
        assertEquals("NP: Bellerophon", result.actionName());
        assertEquals(-250, result.targetHealthChange());
        assertEquals(125, result.sourceHealthChange());
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
        Servant weakTarget = TestServants.create("Weak", ServantClass.ARCHER, 100, 10, 10);

        ActionResult result = noblePhantasm.execute(source, weakTarget);

        assertTrue(result.targetDefeated());
        assertFalse(weakTarget.isAlive());
    }
}
