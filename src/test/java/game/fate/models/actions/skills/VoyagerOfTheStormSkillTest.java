package game.fate.models.actions.skills;

import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;
import game.fate.models.servant.ServantClass;
import game.fate.testutils.TestServants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for VoyagerOfTheStormSkill
 */
public class VoyagerOfTheStormSkillTest {
    private VoyagerOfTheStormSkill skill;
    private Servant target;

    /**
     * Creates the skill and a 1000 HP target before each test.
     */
    @BeforeEach
    void setUp() {
        skill = new VoyagerOfTheStormSkill();
        target = TestServants.defender();
    }

    /**
     * Name, type and MP cost of the skill.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("Skill: Voyager of the Storm", skill.getName());
        assertEquals(ActionType.SKILL, skill.getType());
        assertEquals(20, skill.getMpCost());
    }

    /**
     * The target loses 1.5 x ATK HP (100 ATK gives 150 damage).
     */
    @Test
    void dealsTheExpectedDamage() {
        Servant source = TestServants.create("User", ServantClass.RIDER, 1000, 100, 100);

        skill.execute(source, target);

        assertEquals(850, target.getHp());
    }

    /**
     * The user pays the MP cost and keeps its HP.
     */
    @Test
    void chargesMpAndKeepsHp() {
        Servant source = TestServants.create("User", ServantClass.RIDER, 1000, 100, 100);

        skill.execute(source, target);

        assertEquals(80, source.getMp());
        assertEquals(1000, source.getHp());
    }

    /**
     * The result describes the damage and the MP cost with the right sign and no critical hit.
     */
    @Test
    void resultDescribesDamageAndMpCost() {
        Servant source = TestServants.create("User", ServantClass.RIDER, 1000, 100, 100);

        ActionResult result = skill.execute(source, target);

        assertEquals("User", result.sourceName());
        assertEquals("Defender", result.targetName());
        assertEquals("Skill: Voyager of the Storm", result.actionName());
        assertEquals(-150, result.targetHealthChange());
        assertEquals(0, result.sourceHealthChange());
        assertEquals(-20, result.sourceMpChange());
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
        Servant weakTarget = TestServants.create("Weak", ServantClass.ARCHER, 50, 10, 10);
        Servant source = TestServants.create("User", ServantClass.RIDER, 1000, 100, 100);

        ActionResult result = skill.execute(source, weakTarget);

        assertTrue(result.targetDefeated());
        assertFalse(weakTarget.isAlive());
    }
}
