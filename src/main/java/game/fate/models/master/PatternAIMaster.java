package game.fate.models.master;

import game.fate.models.actions.Action;
import game.fate.models.actions.ActionType;
import game.fate.models.servant.Servant;

import java.util.List;

/**
 * An artificial intelligence implementation of a Master that selects combat actions automatically based on a predefined pattern.
 */
public class PatternAIMaster implements Master {
    private final ActionType[] pattern; // The sequence of actions to follow
    private int currentTurnIndex = 0; // Tracks which step of the pattern we are on

    /**
     * Construct a AI master with a predefined pattern
     *
     * @param pattern Array of Action names
     */
    public PatternAIMaster(ActionType[] pattern) {
        this.pattern = pattern;
    }

    /**
     * Evaluates available combat options and selects an action for the bound Servant to perform.
     *
     * @param servant The Servant awaiting orders.
     * @return The chosen combat action based on the pattern.
     */
    @Override
    public Action selectAction(Servant servant) {
        List<Action> availableActions = servant.getAvailableActions();

        ActionType desiredAction = pattern[currentTurnIndex % pattern.length];

        // Search the servant's available actions for the desired move
        Action chosenAction = availableActions.stream()
                .filter(a -> a.getType().equals(desiredAction) && a.getMpCost() <= servant.getMp())
                .findFirst()
                .orElse(null);

        // Fallback: If the action isn't found (or they lack MP)
        if (chosenAction == null) {
            chosenAction = availableActions.stream()
                    .filter(a -> a.getMpCost() <= servant.getMp())
                    .findFirst()
                    .get();
        }

        currentTurnIndex++;

        return chosenAction;
    }
}
