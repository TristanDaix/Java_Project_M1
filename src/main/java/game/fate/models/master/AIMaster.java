package game.fate.models.master;

import game.fate.models.actions.Action;
import game.fate.models.servantClass.Servant;

import java.util.List;

/**
 * An artificial intelligence implementation of a Master that selects combat actions automatically.
 */
public class AIMaster implements Master {
    /**
     * Selects an action at random from the Servant's available combat actions.
     *
     * @param servant The Servant awaiting orders.
     * @return A randomly selected Action.
     */
    public Action selectAction(Servant servant) {
        List<Action> actions = servant.getAvailableActions();
        return actions.get((int) (Math.random() * actions.size()));
    }
}
