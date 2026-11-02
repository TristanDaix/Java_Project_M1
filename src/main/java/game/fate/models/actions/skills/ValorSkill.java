package game.fate.models.actions.skills;

import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class ValorSkill implements Action {
    @Override
    public String getName() {
        return "Skill: Valor";
    }

    @Override
    public int getMpCost() {
        return 15;
    }

    @Override
    public ActionResult execute(Servant src, Servant tgt) {
        src.consumeMp(getMpCost());
        src.takeDamage(100);
        int damage = (int) (src.getAtk() * 2.2);
        tgt.takeDamage(damage);

        return new ActionResult(
                src.getName(),
                tgt.getName(),
                getName(),
                -damage,
                -100,
                -getMpCost(),
                0,
                false,
                !tgt.isAlive(),
                "Sacrificed HP to unleash a devastating blow!"
        );
    }
}
