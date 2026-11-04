package game.fate.models.actions.basics;

import game.fate.models.actions.Action;
import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class BreakAction implements Action {
    @Override
    public String getName() {
        return "Break";
    }

    @Override
    public int getMpCost() {
        return 0;
    }

    @Override
    public ActionType getType() {
        return ActionType.BREAK;
    }

    @Override
    public ActionResult execute(Servant source, Servant target) {
        int damage = (int) (source.getAtk() * source.getServantClass().getAttackMultiplier());
        damage += (int) (Math.random() * 20) - 10; // add random value between -10 and 10 to damage

        target.takeDamage(damage);

        return new ActionResult(
                source.getName(),
                target.getName(),
                getName(),
                -damage,
                0,
                getMpCost(),
                0,
                false,
                !target.isAlive(),
                null
        );
    }
}
