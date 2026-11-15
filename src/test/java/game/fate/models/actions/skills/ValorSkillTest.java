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
 * Unit tests for ValorSkill
 */
public class ValorSkillTest {
    private ValorSkill skill;
    private Servant source;
    private Servant target;

    /**
     * Creates the skill, a 1000 HP / 100 MP / 100 ATK source and a 1000 HP target before each test.
     */
    @BeforeEach
    void setUp() {
        skill = new ValorSkill();
        source = TestServants.attacker();
        target = TestServants.defender();
    }

    /**
     * Name, type and MP cost of the skill.
     */
    @Test
    void hasTheExpectedNameTypeAndCost() {
        assertEquals("Skill: Valor", skill.getName());
        assertEquals(ActionType.SKILL, skill.getType());
        assertEquals(15, skill.getMpCost());
    }

    /**
     * The target loses 2.2 x ATK HP (100 ATK gives 220 damage).
     */
    @Test
    void dealsTheExpectedDamage() {
        skill.execute(source, target);

        assertEquals(780, target.getHp());
    }

    /**
     * The user pays the price: 100 HP and 15 MP.
     */
    @Test
    void costsHpMpToTheUser() {
        skill.execute(source, target);

        assertEquals(900, source.getHp());
        assertEquals(85, source.getMp());
    }

    /**
     * The result describes the damage, the recoil and the MP cost with the right sign.
     */
    @Test
    void resultDescribesDamageRecoilAndMpCost() {
        ActionResult result = skill.execute(source, target);

        assertEquals("Attacker", result.sourceName());
        assertEquals("Defender", result.targetName());
        assertEquals("Skill: Valor", result.actionName());
        assertEquals(-220, result.targetHealthChange());
        assertEquals(-100, result.sourceHealthChange());
        assertEquals(-15, result.sourceMpChange());
        assertEquals(0, result.targetMpChange());
        assertFalse(result.isCritical());
        assertFalse(result.targetDefeated());
        assertNotNull(result.specialMessage());
    }

    /**
     * A lethal blow flags the target as defeated.
     */
    @Test
    void lethalBlowDefeatsTheTarget() {
        Servant weakTarget = TestServants.create("Weak", ServantClass.ARCHER, 200, 10, 10);

        ActionResult result = skill.execute(source, weakTarget);

        assertFalse(weakTarget.isAlive());
        assertTrue(result.targetDefeated());
    }

    /**
     * Edge case: a user with 100 HP or less is brought to 0 HP by its own sacrifice.
     */
    @Test
    void sacrificeCanBringTheUserDownToZeroHp() {
        source.takeDamage(900);

        skill.execute(source, target);

        assertEquals(0, source.getHp());
        assertFalse(source.isAlive());
    }
}
