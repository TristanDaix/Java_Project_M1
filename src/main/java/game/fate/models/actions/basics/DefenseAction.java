package game.fate.models.actions.basics;

import game.fate.models.actions.Action;
import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class DefenseAction implements Action {
    @Override
    public String getName() {
        return "Defense";
    }

    @Override
    public int getMpCost() {
        return 0;
    }

    @Override
    public ActionType getType() {
        return ActionType.DEFENSE;
    }

    @Override
    public ActionResult execute(Servant source, Servant target) {
        // TODO: reduce damage against skill

        // Do nothing, except against attack (launch counter-attack in the game engine based on the rules)

        return new ActionResult(
                source.getName(),
                target.getName(),
                getName(),
                0,
                0,
                getMpCost(),
                0,
                false,
                !target.isAlive(),
                null
        );
    }
}
