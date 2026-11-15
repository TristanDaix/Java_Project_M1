package game.fate.models;

import game.fate.models.actions.ActionType;
import game.fate.models.master.PatternAIMaster;
import game.fate.models.servant.Servant;
import game.fate.testutils.RecordingAction;
import game.fate.testutils.TestServants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Unit tests for MasterServantPair: the pair only wires a Master to a Servant
 * and delegates the choice of the action to the master.
 */
public class MasterServantPairTest {
    private Servant servant;
    private PatternAIMaster master;
    private MasterServantPair pair;

    /**
     * Builds a pair
     */
    @BeforeEach
    void setUp() {
        servant = TestServants.attacker();
        servant.addAction(new RecordingAction(ActionType.ATTACK));
        master = new PatternAIMaster(new ActionType[]{ActionType.ATTACK});
        pair = new MasterServantPair(master, servant);
    }

    /**
     * The getters return exactly the objects given to the constructor.
     */
    @Test
    void getMasterAndServant() {
        assertSame(master, pair.getMaster());
        assertSame(servant, pair.getServant());
    }

    /**
     * issueCommand returns the action chosen by the master.
     */
    @Test
    void issueCommandReturnsTheActionChosenByTheMaster() {
        assertSame(servant.getAvailableActions().getFirst(), pair.issueCommand());
    }
}
