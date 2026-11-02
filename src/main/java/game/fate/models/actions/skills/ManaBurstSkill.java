package game.fate.models.actions.skills;

import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class ManaBurstSkill implements Action {
    @Override
    public String getName() {
        return "Skill: Mana Burst";
    }

    @Override
    public int getMpCost() {
        return 20;
    }

    @Override
    public ActionResult execute(Servant src, Servant tgt) {
        src.consumeMp(getMpCost());
        int damage = (int) (src.getAtk() * 1.8);
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
                "A burst of magical energy enhanced the strike!"
        );
    }
}
