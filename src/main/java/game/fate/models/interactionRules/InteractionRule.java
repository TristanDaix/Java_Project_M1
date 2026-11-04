package game.fate.models.interactionRules;

import game.fate.models.actions.Action;
import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

/**
 * Interface representing the interaction between different type of action
 */
public interface InteractionRule {
    /**
     * Evaluates if this rule applies to the clash
     *
     * @param type1 The action of one of the two servant
     * @param type2 The action of the other one
     * @return True if his rule applies to the clash, false otherwise
     */
    boolean matches(ActionType type1, ActionType type2);

    /**
     * Resolves the outcome of the clash
     *
     * @param servant1 One of the two servant
     * @param action1  Action of the first servant
     * @param servant2 The other servant
     * @param action2  Action of the second servant
     * @return The result of this clash
     */
    ActionResult resolve(Servant servant1, Action action1, Servant servant2, Action action2);
}
