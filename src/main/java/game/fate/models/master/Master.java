package game.fate.models.master;

import game.fate.models.actions.Action;
import game.fate.models.servant.Servant;

/**
 * Defines the decision-making entity capable of commanding a Servant during a turn.
 */
public interface Master {
    /**
     * Evaluates available combat options and selects an action for the bound Servant to perform.
     *
     * @param servant The Servant awaiting orders.
     * @return The chosen combat action.
     */
    Action selectAction(Servant servant);
}
