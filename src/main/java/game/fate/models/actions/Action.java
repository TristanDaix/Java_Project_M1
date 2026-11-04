package game.fate.models.actions;

import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

/**
 * Command interface representing a usable combat action or skill executable during a battle.
 */
public interface Action {
    /**
     * Retrieves the display name of this combat action.
     *
     * @return The string representation of the action's name.
     */
    String getName();

    /**
     * Retrieves the MP cost of this combat action
     *
     * @return The MP needed to perform the action
     */
    int getMpCost();

    /**
     * Allow the engine to identify the type of move
     *
     * @return The type of action
     */
    ActionType getType();

    /**
     * Executes the combat calculations and side effects of this action.
     *
     * @param source The Servant performing the action.
     * @param target The Servant targeted by the action.
     * @return An ActionResult encapsulating the numerical and status outcomes for UI rendering.
     */
    ActionResult execute(Servant source, Servant target);
}
