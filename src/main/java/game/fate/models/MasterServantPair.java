package game.fate.models;

import game.fate.models.actions.Action;
import game.fate.models.master.Master;
import game.fate.models.servantClass.Servant;

/**
 * Represents a binding contract between a Master and a Servant during the Holy Grail War.
 * Wraps the master's decision-making logic and the servant's combat state into a single cohesive entity.
 */
public class MasterServantPair {
    private final Master master; // The Master responsible for issuing combat commands.
    private final Servant servant; // The Servant fighting on behalf of the Master.

    /**
     * Forms a new combat pairing between a Master and a Servant.
     *
     * @param master  The commanding Master.
     * @param servant The bound Servant.
     */
    public MasterServantPair(Master master, Servant servant) {
        this.master = master;
        this.servant = servant;
    }

    /**
     * Retrieves the Master of this pair.
     *
     * @return The Master object.
     */
    public Master getMaster() {
        return master;
    }

    /**
     * Retrieves the Servant of this pair.
     *
     * @return The Servant object.
     */
    public Servant getServant() {
        return servant;
    }

    /**
     * Delegates to the Master to select and issue a combat action based on the Servant's available skills.
     *
     * @return The Action chosen by the Master to be executed.
     */
    public Action issueCommand() {
        return master.selectAction(this.servant);
    }
}
