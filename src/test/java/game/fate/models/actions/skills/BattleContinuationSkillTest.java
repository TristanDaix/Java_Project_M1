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
 * Unit tests for BattleContinuationSkill
 */
public class BattleContinuationSkillTest {

    private BattleContinuationSkill skill;
    private Servant source;
    private Servant target;

    /**
     * Creates the skill, a LANCER with 1000 HP / 100 MP and a target before each test.
     */
    @BeforeEach
    void setUp() {
        skill = new BattleContinuationSkill();
        source = TestServants.create("Lancer", ServantClass.LANCER, 1000, 100, 100);
        target = TestServants.attacker();
    }

    /**
     * Name, type and MP cost of the skill.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("Skill: Battle Continuation", skill.getName());
        assertEquals(ActionType.SKILL, skill.getType());
        assertEquals(15, skill.getMpCost());
    }

    /**
     * The user recovers 30% of its maximum HP (300 of 1000) and pays 15 MP.
     */
    @Test
    void healsThirtyPercentOfMaxHpAndChargesMp() {
        source.takeDamage(600);

        skill.execute(source, target);

        assertEquals(700, source.getHp());
        assertEquals(85, source.getMp());
    }

    /**
     * The healing is based on the maximum HP, not on the current HP.
     */
    @Test
    void healingDependsOnMaxHpNotOnCurrentHp() {
        source.takeDamage(990);

        skill.execute(source, target);

        assertEquals(310, source.getHp());
    }

    /**
     * The result has no target name (self skill) and reports the healing and the MP cost of the source.
     */
    @Test
    void resultIsASelfEffectReportingHealingAndMpCost() {
        source.takeDamage(600);

        ActionResult result = skill.execute(source, target);

        assertEquals("Lancer", result.sourceName());
        assertNull(result.targetName());
        assertEquals("Skill: Battle Continuation", result.actionName());
        assertEquals(300, result.sourceHealthChange());
        assertEquals(-15, result.sourceMpChange());
        assertEquals(0, result.targetHealthChange());
        assertEquals(0, result.targetMpChange());
        assertFalse(result.isCritical());
        assertFalse(result.targetDefeated());
        assertNotNull(result.specialMessage());
    }
}
