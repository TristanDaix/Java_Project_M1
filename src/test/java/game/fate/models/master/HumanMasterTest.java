package game.fate.models.master;

import game.fate.models.actions.Action;
import game.fate.models.actions.ActionType;
import game.fate.models.servant.Servant;
import game.fate.testutils.ConsoleTestSupport;
import game.fate.testutils.TestServants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for HumanMaster, the master driven by the keyboard.
 * The keyboard is simulated with ConsoleTestSupport: the text given to it is what the "player" types.
 */
public class HumanMasterTest {
    private ConsoleTestSupport io;

    /**
     * Gives the real System.in / System.out back after each test.
     */
    @AfterEach
    void tearDown() {
        if (io != null) {
            io.close();
        }
    }

    private HumanMaster masterTyping(String keyboardInput) {
        io = new ConsoleTestSupport(keyboardInput);
        return new HumanMaster(io.createConsole());
    }

    /**
     * Typing 1 selects the first action of the servant.
     */
    @Test
    void choiceOneSelectsTheFirstAction() {
        Servant servant = TestServants.servantWithAction();

        Action chosen = masterTyping("1\n").selectAction(servant);

        assertEquals(ActionType.ATTACK, chosen.getType());
    }

    /**
     * Typing the last number selects the last action.
     */
    @Test
    void lastChoiceSelectsTheLastAction() {
        Servant servant = TestServants.servantWithAction();

        Action chosen = masterTyping("5\n").selectAction(servant);

        assertEquals(ActionType.NP, chosen.getType());
    }

    /**
     * A number outside the menu is refused with "Invalid choice." and the master asks again.
     */
    @Test
    void asksAgainAfterAnOutOfRangeChoice() {
        Servant servant = TestServants.servantWithAction();

        Action chosen = masterTyping("9\n0\n3\n").selectAction(servant);

        assertEquals(ActionType.BREAK, chosen.getType());
        assertTrue(io.output().contains("Invalid choice."));
    }

    /**
     * Text that is not a number is ignored and the master keeps waiting for a number.
     */
    @Test
    void ignoresInputThatIsNotANumber() {
        Servant servant = TestServants.servantWithAction();

        Action chosen = masterTyping("Maybe it's hungry\nLet's feed it Lancer\n1\n").selectAction(servant);

        assertEquals(ActionType.ATTACK, chosen.getType());
    }

    /**
     * An action the servant cannot pay is refused ("Not enough MP") and the master asks again.
     */
    @Test
    void refusesAnActionTheServantCannotAfford() {
        Servant servant = TestServants.servantWithAction();
        servant.consumeMp(90);

        Action chosen = masterTyping("4\n1\n").selectAction(servant);

        assertEquals(ActionType.ATTACK, chosen.getType());
        assertTrue(io.output().contains("Not enough MP for that action!"));
    }

    /**
     * The menu displays every action with its MP cost.
     */
    @Test
    void displaysEveryActionWithItsMpCost() {
        masterTyping("1\n").selectAction(TestServants.servantWithAction());

        assertTrue(io.output().contains("(1) Attack [0 MP]"));
        assertTrue(io.output().contains("(2) Defense [0 MP]"));
        assertTrue(io.output().contains("(3) Break [0 MP]"));
        assertTrue(io.output().contains("(4) Skill: Mana Burst [20 MP]"));
        assertTrue(io.output().contains("(5) NP: Excalibur [50 MP]"));
    }
}
