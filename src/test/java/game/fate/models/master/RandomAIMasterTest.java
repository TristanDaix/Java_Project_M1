package game.fate.models.master;

import game.fate.models.actions.ActionType;
import game.fate.models.servant.Servant;
import game.fate.testutils.TestServants;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for the RandomAIMaster: it picks, at random, among the actions the servant can afford.
 */
@SuppressWarnings("deprecation")
public class RandomAIMasterTest {
    /**
     * Without MP only the free actions can be chosen: the 50 MP noble phantasm is never returned.
     */
    @RepeatedTest(50)
    void neverChoosesAnActionTheServantCannotAfford() {
        RandomAIMaster master = new RandomAIMaster();
        Servant servant = TestServants.servantWithAction();
        servant.consumeMp(100);

        assertNotEquals(ActionType.NP, master.selectAction(servant).getType());
        assertNotEquals(ActionType.SKILL, master.selectAction(servant).getType());
    }

    /**
     * With enough MP, every action can be chosen.
     */
    @Test
    void canChooseEveryAffordableAction() {
        RandomAIMaster master = new RandomAIMaster();
        Servant servant = TestServants.servantWithAction();
        Set<ActionType> seen = new HashSet<>();

        for (int draw = 0; draw < 500; draw++) {
            seen.add(master.selectAction(servant).getType());
            servant.restoreMp(100);
        }

        assertEquals(Set.of(ActionType.ATTACK, ActionType.DEFENSE, ActionType.BREAK, ActionType.SKILL, ActionType.NP), seen);
    }
}
