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
 * Unit tests for RuleBreakerNP
 */
public class RuleBreakerNPTest {
    private RuleBreakerNP noblePhantasm;
    private Servant source;
    private Servant target;

    /**
     * Creates the noble phantasm, a CASTER (1000 HP, 100 MP, 100 ATK) and a 1000 HP / 100 MP target before each test.
     */
    @BeforeEach
    void setUp() {
        noblePhantasm = new RuleBreakerNP();
        source = TestServants.create("Caster", ServantClass.CASTER, 1000, 100, 100);
        target = TestServants.defender();
    }

    /**
     * Name, type and MP cost.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("NP: Rule Breaker", noblePhantasm.getName());
        assertEquals(ActionType.NP, noblePhantasm.getType());
        assertEquals(40, noblePhantasm.getMpCost());
    }

    /**
     * The target loses 1.5 x ATK HP (100 ATK gives 150 damage).
     */
    @Test
    void dealsTheExpectedDamage() {
        noblePhantasm.execute(source, target);

        assertEquals(850, target.getHp());
    }

    /**
     * The target loses 30 MP (100 becomes 70).
     */
    @Test
    void drainsMpFromTheTarget() {
        noblePhantasm.execute(source, target);

        assertEquals(70, target.getMp());
    }

    /**
     * The user pays 40 MP and recovers 30 MP: 100 MP becomes 90 MP.
     */
    @Test
    void userPaysFortyMpAndStealsThirty() {
        noblePhantasm.execute(source, target);

        assertEquals(90, source.getMp());
    }

    /**
     * The result describes the damage, the net MP change of the source (-10) and the MP loss of the target (-30).
     */
    @Test
    void resultDescribesDamageAndBothMpChanges() {
        ActionResult result = noblePhantasm.execute(source, target);

        assertEquals("Caster", result.sourceName());
        assertEquals("Defender", result.targetName());
        assertEquals("NP: Rule Breaker", result.actionName());
        assertEquals(-150, result.targetHealthChange());
        assertEquals(0, result.sourceHealthChange());
        assertEquals(-10, result.sourceMpChange());
        assertEquals(-30, result.targetMpChange());
        assertFalse(result.isCritical());
        assertFalse(result.targetDefeated());
        assertNotNull(result.specialMessage());
    }

    /**
     * A lethal hit defeats the target and the result flags it.
     */
    @Test
    void lethalHitDefeatsTheTarget() {
        Servant weakTarget = TestServants.create("Weak", ServantClass.ARCHER, 100, 100, 10);

        ActionResult result = noblePhantasm.execute(source, weakTarget);

        assertTrue(result.targetDefeated());
        assertFalse(weakTarget.isAlive());
    }
}
