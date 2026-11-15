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
 * Unit tests for PresenceConcealmentSkill
 */
public class PresenceConcealmentSkillTest {
    private PresenceConcealmentSkill skill;
    private Servant target;

    /**
     * Creates the skill and a 1000 HP target before each test.
     */
    @BeforeEach
    void setUp() {
        skill = new PresenceConcealmentSkill();
        target = TestServants.defender();
    }

    /**
     * Name, type and MP cost of the skill.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("Skill: Presence Concealment", skill.getName());
        assertEquals(ActionType.SKILL, skill.getType());
        assertEquals(10, skill.getMpCost());
    }

    /**
     * The target loses exactly 150 HP and the user pays 10 MP.
     */
    @Test
    void dealsFixedDamageAndChargesMp() {
        Servant source = TestServants.create("Assassin", ServantClass.ASSASSIN, 1000, 100, 100);

        skill.execute(source, target);

        assertEquals(850, target.getHp());
        assertEquals(90, source.getMp());
    }

    /**
     * The result describes the damage and the MP cost; the user keeps its HP.
     */
    @Test
    void resultDescribesDamageAndMpCost() {
        Servant source = TestServants.create("Assassin", ServantClass.ASSASSIN, 1000, 100, 100);

        ActionResult result = skill.execute(source, target);

        assertEquals("Assassin", result.sourceName());
        assertEquals("Defender", result.targetName());
        assertEquals("Skill: Presence Concealment", result.actionName());
        assertEquals(-150, result.targetHealthChange());
        assertEquals(0, result.sourceHealthChange());
        assertEquals(-10, result.sourceMpChange());
        assertEquals(0, result.targetMpChange());
        assertFalse(result.isCritical());
        assertFalse(result.targetDefeated());
        assertEquals(1000, source.getHp());
    }

    /**
     * A target with 150 HP or less is killed and reported as defeated.
     */
    @Test
    void lethalStrikeDefeatsTheTarget() {
        Servant weakTarget = TestServants.create("Weak", ServantClass.LANCER, 150, 10, 10);
        Servant source = TestServants.create("Assassin", ServantClass.ASSASSIN, 1000, 100, 100);

        ActionResult result = skill.execute(source, weakTarget);

        assertTrue(result.targetDefeated());
        assertFalse(weakTarget.isAlive());
    }
}
