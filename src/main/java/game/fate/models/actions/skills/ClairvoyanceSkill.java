package game.fate.models.actions.skills;

import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class ClairvoyanceSkill implements Action {
    @Override
    public String getName() {
        return "Skill: Clairvoyance";
    }

    @Override
    public int getMpCost() {
        return 15;
    }

    @Override
    public ActionResult execute(Servant src, Servant tgt) {
        src.consumeMp(getMpCost());
        int damage = (int) (src.getAtk() * src.getCriticalMultiplier() * src.getServantClass().getCriticalMultiplier());
        tgt.takeDamage(damage);

        return new ActionResult(
                src.getName(),
                tgt.getName(),
                getName(),
                -damage,
                0,
                -getMpCost(),
                0,
                true,
                !tgt.isAlive(),
                "The future was read. Critical hit guaranteed!"
        );
    }
}
