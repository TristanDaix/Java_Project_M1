package game.fate.models.actions;

import game.fate.models.servantClass.Servant;
import game.fate.models.dto.ActionResult;

/**
 * Command interface representing a usable combat action.
 */
public interface Action {
    String getName();

    ActionResult execute(Servant source, Servant target);
}
