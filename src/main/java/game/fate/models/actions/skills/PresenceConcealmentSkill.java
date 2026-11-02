package game.fate.models.actions.skills;

import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class PresenceConcealmentSkill implements Action {
    @Override
    public String getName() {
        return "Skill: Presence Concealment";
    }

    @Override
    public int getMpCost() {
        return 10;
    }

    @Override
    public ActionResult execute(Servant src, Servant tgt) {
        src.consumeMp(getMpCost());
        int damage = 150;
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
                "Struck from the shadows with unmitigated precision!"
        );
    }
}
