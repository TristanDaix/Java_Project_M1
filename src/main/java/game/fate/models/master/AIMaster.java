package game.fate.models.master;

import game.fate.models.actions.Action;
import game.fate.models.servant.Servant;

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
        List<Action> validActions = servant.getAvailableActions().stream()
                .filter(a -> a.getMpCost() <= servant.getMp())
                .toList();

        return validActions.get((int) (Math.random() * validActions.size()));
    }
}
