package game.fate.models.actions.noblephantasms;

import game.fate.models.actions.Action;
import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class ExcaliburNP implements Action {
    @Override
    public String getName() {
        return "NP: Excalibur";
    }

    @Override
    public int getMpCost() {
        return 50;
    }

    @Override
    public ActionType getType() {
        return ActionType.NP;
    }

    @Override
    public ActionResult execute(Servant src, Servant tgt) {
        src.consumeMp(getMpCost());
        int damage = (int) (src.getAtk() * 3.5);
        tgt.takeDamage(damage);

        return new ActionResult(
                src.getName(),
                tgt.getName(),
                getName(),
                -damage,
                0,
                -getMpCost(),
                0,
                false,
                !tgt.isAlive(),
                "The Sword of Promised Victory obliterated the field!"
        );
    }
}
