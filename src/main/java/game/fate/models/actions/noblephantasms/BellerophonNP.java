package game.fate.models.actions.noblephantasms;

import game.fate.models.actions.Action;
import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class BellerophonNP implements Action {
    @Override
    public String getName() {
        return "NP: Bellerophon";
    }

    @Override
    public int getMpCost() {
        return 40;
    }

    @Override
    public ActionType getType() {
        return ActionType.NP;
    }

    @Override
    public ActionResult execute(Servant src, Servant tgt) {
        src.consumeMp(getMpCost());
        int damage = (int) (src.getAtk() * 2.5);
        tgt.takeDamage(damage);
        int heal = damage / 2;
        src.heal(heal);

        return new ActionResult(
                src.getName(),
                tgt.getName(),
                getName(),
                -damage,
                heal,
                -getMpCost(),
                0,
                false,
                !tgt.isAlive(),
                "The mythical pegasus crushed the enemy and drained their life!"
        );
    }
}
