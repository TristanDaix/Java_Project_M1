package game.fate.models.actions.noblephantasms;

import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;
import game.fate.models.servant.ServantClass;
import game.fate.testutils.ServantClassSnapshot;
import game.fate.testutils.TestServants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TsubameGaeshiNP.
 */
public class TsubameGaeshiNPTest {
    private ServantClassSnapshot snapshot;
    private TsubameGaeshiNP noblePhantasm;
    private Servant target;

    /**
     * Neutral class stats, a fresh noble phantasm and a 1000 HP target before each test.
     */
    @BeforeEach
    void setUp() {
        snapshot = ServantClassSnapshot.capture();
        ServantClassSnapshot.useNeutralStats();
        noblePhantasm = new TsubameGaeshiNP();
        target = TestServants.defender();
    }

    /**
     * Restores the original class statistics.
     */
    @AfterEach
    void tearDown() {
        snapshot.restore();
    }

    /**
     * Name, type and MP cost.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("NP: Tsubame Gaeshi", noblePhantasm.getName());
        assertEquals(ActionType.NP, noblePhantasm.getType());
        assertEquals(50, noblePhantasm.getMpCost());
    }

    /**
     * Damage = ATK x critical multiplier x 3: 100 x 1.5 x 3 = 450.
     */
    @Test
    void dealsTheExpectedDamage() {
        Servant source = TestServants.create("Assassin", ServantClass.ASSASSIN, 1000, 100, 100, 1.5, 0.0);

        noblePhantasm.execute(source, target);

        assertEquals(550, target.getHp());
    }

    /**
     * The result is always flagged as critical, even with a critical rate of 0.0.
     */
    @Test
    void resultDescribeCriticalAndMPCost() {
        Servant source = TestServants.create("Assassin", ServantClass.ASSASSIN, 1000, 100, 100, 1.5, 0.0);

        ActionResult result = noblePhantasm.execute(source, target);

        assertTrue(result.isCritical());
        assertEquals("Assassin", result.sourceName());
        assertEquals("Defender", result.targetName());
        assertEquals("NP: Tsubame Gaeshi", result.actionName());
        assertEquals(-450, result.targetHealthChange());
        assertEquals(0, result.sourceHealthChange());
        assertEquals(-50, result.sourceMpChange());
        assertEquals(0, result.targetMpChange());
        assertFalse(result.targetDefeated());
        assertNotNull(result.specialMessage());
    }

    /**
     * The user pays 50 MP and keeps its HP.
     */
    @Test
    void chargesMpAndKeepsHp() {
        Servant source = TestServants.create("Assassin", ServantClass.ASSASSIN, 1000, 100, 100, 1.5, 0.0);

        noblePhantasm.execute(source, target);

        assertEquals(50, source.getMp());
        assertEquals(1000, source.getHp());
    }

    /**
     * A lethal hit defeats the target and the result flags it.
     */
    @Test
    void lethalHitDefeatsTheTarget() {
        Servant weakTarget = TestServants.create("Weak", ServantClass.ARCHER, 100, 10, 10);
        Servant source = TestServants.create("Assassin", ServantClass.ASSASSIN, 1000, 100, 100, 1.5, 0.0);

        ActionResult result = noblePhantasm.execute(source, weakTarget);

        assertTrue(result.targetDefeated());
        assertFalse(weakTarget.isAlive());
    }
}
