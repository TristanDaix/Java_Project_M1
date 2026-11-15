package game.fate.models.actions.skills;

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
 * Unit tests for ClairvoyanceSkill
 */
public class ClairvoyanceSkillTest {
    private ServantClassSnapshot snapshot;
    private ClairvoyanceSkill skill;
    private Servant target;

    /**
     * Neutral class stats, a fresh skill and a 1000 HP target before each test.
     */
    @BeforeEach
    void setUp() {
        snapshot = ServantClassSnapshot.capture();
        ServantClassSnapshot.useNeutralStats();
        skill = new ClairvoyanceSkill();
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
     * Name, type and MP cost of the skill.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("Skill: Clairvoyance", skill.getName());
        assertEquals(ActionType.SKILL, skill.getType());
        assertEquals(15, skill.getMpCost());
    }

    /**
     * Damage = ATK x servant multiplier x class multiplier: 100 x 1.5 x 2.0 = 300.
     */
    @Test
    void damageCombinesServantAndClassCriticalMultipliers() {
        ServantClass.ARCHER.setStats(2, 1.0, 2.0, 0.0);
        Servant source = TestServants.create("Archer", ServantClass.ARCHER, 1000, 100, 100, 1.5, 0.0);

        skill.execute(source, target);

        assertEquals(700, target.getHp());
    }

    /**
     * With neutral multipliers the damage is exactly the ATK value: there is no random part.
     */
    @Test
    void damageIsExactlyAtkWithNeutralMultipliers() {
        Servant source = TestServants.create("Archer", ServantClass.ARCHER, 1000, 100, 100);

        skill.execute(source, target);

        assertEquals(900, target.getHp());
    }

    /**
     * The hit is always critical, even with a critical rate of 0.0, and the MP cost is charged.
     */
    @Test
    void alwaysCriticalAndChargesMp() {
        Servant source = TestServants.create("Archer", ServantClass.ARCHER, 1000, 100, 100);

        ActionResult result = skill.execute(source, target);

        assertTrue(result.isCritical());
        assertEquals(85, source.getMp());
        assertEquals(-15, result.sourceMpChange());
        assertEquals(-100, result.targetHealthChange());
        assertEquals(0, result.sourceHealthChange());
    }

    /**
     * A lethal hit flags the target as defeated.
     */
    @Test
    void lethalHitDefeatsTheTarget() {
        Servant weakTarget = TestServants.create("Weak", ServantClass.LANCER, 50, 10, 10);
        Servant source = TestServants.create("Archer", ServantClass.ARCHER, 1000, 100, 100);

        ActionResult result = skill.execute(source, weakTarget);

        assertTrue(result.targetDefeated());
        assertFalse(weakTarget.isAlive());
    }
}
