package game.fate.models.master;

import game.fate.models.actions.ActionType;
import game.fate.models.actions.basics.AttackAction;
import game.fate.models.actions.noblephantasms.ExcaliburNP;
import game.fate.models.servant.Servant;
import game.fate.testutils.TestServants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for PatternAIMaster: an AI that replays a fixed sequence of action types, cycling when the
 * sequence is over, and falling back to the first affordable action when it cannot do what the pattern asks.
 */
public class PatternAIMasterTest {
    /**
     * The master returns the pattern steps in the given order, one per call.
     */
    @Test
    void followsThePatternInOrder() {
        PatternAIMaster master = new PatternAIMaster(new ActionType[]{ActionType.ATTACK, ActionType.DEFENSE, ActionType.BREAK});
        Servant servant = TestServants.servantWithAction();

        assertEquals(ActionType.ATTACK, master.selectAction(servant).getType());
        assertEquals(ActionType.DEFENSE, master.selectAction(servant).getType());
        assertEquals(ActionType.BREAK, master.selectAction(servant).getType());
    }

    /**
     * When the pattern is over, the master starts again from its first step.
     */
    @Test
    void repeatsThePatternWhenItIsOver() {
        PatternAIMaster master = new PatternAIMaster(new ActionType[]{ActionType.ATTACK, ActionType.DEFENSE});
        Servant servant = TestServants.servantWithAction();

        assertEquals(ActionType.ATTACK, master.selectAction(servant).getType());
        assertEquals(ActionType.DEFENSE, master.selectAction(servant).getType());
        assertEquals(ActionType.ATTACK, master.selectAction(servant).getType());
        assertEquals(ActionType.DEFENSE, master.selectAction(servant).getType());
        assertEquals(ActionType.ATTACK, master.selectAction(servant).getType());
    }

    /**
     * A pattern with a single step is played again and again.
     */
    @Test
    void aSingleStepPatternIsRepeatedForever() {
        PatternAIMaster master = new PatternAIMaster(new ActionType[]{ActionType.DEFENSE});
        Servant servant = TestServants.servantWithAction();

        assertEquals(ActionType.DEFENSE, master.selectAction(servant).getType());
        assertEquals(ActionType.DEFENSE, master.selectAction(servant).getType());
        assertEquals(ActionType.DEFENSE, master.selectAction(servant).getType());
    }

    /**
     * Skill and noble phantasm steps select the matching actions of the servant when the MP is sufficient.
     */
    @Test
    void selectsSkillAndNoblePhantasmWhenAffordable() {
        PatternAIMaster master = new PatternAIMaster(new ActionType[]{ActionType.SKILL, ActionType.NP});
        Servant servant = TestServants.servantWithAction();

        assertEquals("Skill: Mana Burst", master.selectAction(servant).getName());
        assertEquals("NP: Excalibur", master.selectAction(servant).getName());
    }

    /**
     * Edge case: with exactly the MP required (50 for Excalibur) the noble phantasm is still chosen.
     */
    @Test
    void choosesTheNoblePhantasmWithExactlyEnoughMp() {
        PatternAIMaster master = new PatternAIMaster(new ActionType[]{ActionType.NP});
        Servant servant = TestServants.servantWithAction();
        servant.consumeMp(50);

        assertEquals("NP: Excalibur", master.selectAction(servant).getName());
    }

    /**
     * Not enough MP for the noble phantasm: the master falls back to the first affordable action, Attack.
     */
    @Test
    void fallsBackToTheFirstAffordableActionWhenTheNoblePhantasmIsTooExpensive() {
        PatternAIMaster master = new PatternAIMaster(new ActionType[]{ActionType.NP});
        Servant servant = TestServants.servantWithAction();
        servant.consumeMp(51);

        assertEquals(ActionType.ATTACK, master.selectAction(servant).getType());
    }

    /**
     * If the servant has no action of the requested type at all, the master falls back to the first affordable action.
     */
    @Test
    void fallsBackWhenTheServantHasNoActionOfTheRequestedType() {
        Servant servant = TestServants.attacker();
        servant.addAction(new AttackAction());
        PatternAIMaster master = new PatternAIMaster(new ActionType[]{ActionType.SKILL});

        assertEquals(ActionType.ATTACK, master.selectAction(servant).getType());
    }

    /**
     * Using the fallback still consumes the pattern step: the next call plays the next step, it does not retry the old one.
     */
    @Test
    void patternAdvancesEvenWhenTheFallbackIsUsed() {
        PatternAIMaster master = new PatternAIMaster(new ActionType[]{ActionType.NP, ActionType.DEFENSE});
        Servant servant = TestServants.servantWithAction();
        servant.consumeMp(100);

        assertEquals(ActionType.ATTACK, master.selectAction(servant).getType());
        assertEquals(ActionType.DEFENSE, master.selectAction(servant).getType());
    }

    /**
     * Edge case: when no action is affordable the master returns null
     */
    @Test
    void returnsNullWhenNothingIsAffordable() {
        Servant servant = TestServants.attacker();
        servant.addAction(new ExcaliburNP());
        servant.consumeMp(100);
        PatternAIMaster master = new PatternAIMaster(new ActionType[]{ActionType.ATTACK});

        assertNull(master.selectAction(servant));
    }
}
