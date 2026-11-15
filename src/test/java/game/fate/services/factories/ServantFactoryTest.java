package game.fate.services.factories;

import game.fate.models.actions.Action;
import game.fate.models.actions.ActionType;
import game.fate.models.servant.Servant;
import game.fate.models.servant.ServantClass;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for ServantFactory, which builds a fully equipped servant:
 * three basic actions (Attack, Defense, Break), then the skill of its class, then the noble phantasm of its id
 */
public class ServantFactoryTest {

    private Servant create(int id, ServantClass servantClass) {
        return ServantFactory.createServant(id, "Test", servantClass, 1000, 100, 100, 1.0, 0.0);
    }

    private String classSkillOf(ServantClass servantClass) {
        return create(42, servantClass).getAvailableActions().get(3).getName();
    }

    private String noblePhantasmOf(int id) {
        return create(id, ServantClass.SABER).getAvailableActions().get(4).getName();
    }

    /**
     * The servant receives exactly the identity and statistics given to the factory.
     */
    @Test
    void createsAServantWithTheGivenIdentityAndStats() {
        Servant servant = ServantFactory.createServant(3, "Cu Chulainn", ServantClass.LANCER, 1100, 90, 95, 1.1, 0.2);

        assertEquals(3, servant.getId());
        assertEquals("Cu Chulainn", servant.getName());
        assertEquals(ServantClass.LANCER, servant.getServantClass());
        assertEquals(1100, servant.getMaxHp());
        assertEquals(90, servant.getMaxMp());
        assertEquals(95, servant.getAtk());
        assertEquals(1.1, servant.getCriticalMultiplier());
        assertEquals(0.2, servant.getCriticalRate());
        assertEquals(1, servant.getLevel());
    }

    /**
     * The first three actions are always the free basic actions, in this order: Attack, Defense, Break.
     */
    @Test
    void firstThreeActionsAreTheBasicOnes() {
        List<Action> actions = create(42, ServantClass.ARCHER).getAvailableActions();

        assertEquals("Attack", actions.get(0).getName());
        assertEquals(ActionType.ATTACK, actions.get(0).getType());
        assertEquals("Defense", actions.get(1).getName());
        assertEquals(ActionType.DEFENSE, actions.get(1).getType());
        assertEquals("Break", actions.get(2).getName());
        assertEquals(ActionType.BREAK, actions.get(2).getType());
        assertEquals(0, actions.get(0).getMpCost() + actions.get(1).getMpCost() + actions.get(2).getMpCost());
    }

    /**
     * Class gets correct skill.
     */
    @Test
    void classGetsCorrectSkill() {
        assertEquals("Skill: Mana Burst", classSkillOf(ServantClass.SABER));
        assertEquals("Skill: Clairvoyance", classSkillOf(ServantClass.ARCHER));
        assertEquals("Skill: Battle Continuation", classSkillOf(ServantClass.LANCER));
        assertEquals("Skill: Voyager of the Storm", classSkillOf(ServantClass.RIDER));
        assertEquals("Skill: High-Speed Divine Words", classSkillOf(ServantClass.CASTER));
        assertEquals("Skill: Presence Concealment", classSkillOf(ServantClass.ASSASSIN));
        assertEquals("Skill: Valor", classSkillOf(ServantClass.BERSERKER));
    }

    /**
     * Id gets correct NP.
     */
    @Test
    void idGetsCorrectNP() {
        assertEquals("NP: Excalibur", noblePhantasmOf(1));
        assertEquals("NP: Unlimited Blade Works", noblePhantasmOf(2));
        assertEquals("NP: Gae Bolg", noblePhantasmOf(3));
        assertEquals("NP: Bellerophon", noblePhantasmOf(4));
        assertEquals("NP: Rule Breaker", noblePhantasmOf(5));
        assertEquals("NP: Tsubame Gaeshi", noblePhantasmOf(6));
        assertEquals("NP: Nine Lives", noblePhantasmOf(7));
    }
}
