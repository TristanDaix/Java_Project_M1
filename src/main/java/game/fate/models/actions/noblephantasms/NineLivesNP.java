package game.fate.models.actions.noblephantasms;

import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class NineLivesNP implements Action {
    @Override
    public String getName() {
        return "NP: Nine Lives";
    }

    @Override
    public int getMpCost() {
        return 50;
    }

    @Override
    public ActionResult execute(Servant src, Servant tgt) {
        src.consumeMp(getMpCost());
        int damage = (int) (src.getAtk() * 3.0);
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
                "Shooting the Hundred Heads!"
        );
    }
}
