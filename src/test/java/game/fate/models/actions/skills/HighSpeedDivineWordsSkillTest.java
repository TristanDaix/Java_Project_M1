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
 * Unit tests for HighSpeedDivineWordsSkill
 */
public class HighSpeedDivineWordsSkillTest {
    private HighSpeedDivineWordsSkill skill;
    private Servant source;
    private Servant target;

    /**
     * Creates the skill, a CASTER with 1000 HP / 100 MP and a 1000 HP target before each test.
     */
    @BeforeEach
    void setUp() {
        skill = new HighSpeedDivineWordsSkill();
        source = TestServants.create("Caster", ServantClass.CASTER, 1000, 100, 100);
        target = TestServants.defender();
    }

    /**
     * Name, type and MP cost.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("Skill: High-Speed Divine Words", skill.getName());
        assertEquals(ActionType.SKILL, skill.getType());
        assertEquals(0, skill.getMpCost());
    }

    /**
     * The user recovers 50 MP.
     */
    @Test
    void restoresFiftyMpToTheUser() {
        source.consumeMp(80);

        skill.execute(source, target);

        assertEquals(70, source.getMp());
    }

    /**
     * The result has no target name (self skill), reports +50 MP for the source and nothing else.
     */
    @Test
    void resultIsASelfEffectReportingTheMpGain() {
        ActionResult result = skill.execute(source, target);

        assertEquals("Caster", result.sourceName());
        assertNull(result.targetName());
        assertEquals("Skill: High-Speed Divine Words", result.actionName());
        assertEquals(50, result.sourceMpChange());
        assertEquals(0, result.targetHealthChange());
        assertEquals(0, result.sourceHealthChange());
        assertEquals(0, result.targetMpChange());
        assertFalse(result.isCritical());
        assertFalse(result.targetDefeated());
        assertNotNull(result.specialMessage());
    }
}
