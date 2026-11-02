package game.fate.models.actions.noblephantasms;

import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class GaeBolgNP implements Action {
    @Override
    public String getName() {
        return "NP: Gae Bolg";
    }

    @Override
    public int getMpCost() {
        return 30;
    }

    @Override
    public ActionResult execute(Servant src, Servant tgt) {
        src.consumeMp(getMpCost());
        int damage;
        String msg;
        if (tgt.getHp() <= tgt.getMaxHp() * 0.3) {
            damage = tgt.getHp();
            msg = "The spear reversed causality and pierced the heart! Instant Kill!";
        } else {
            damage = (int) (src.getAtk() * 1.5);
            msg = "The spear struck with deadly force!";
        }
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
                msg
        );
    }
}
