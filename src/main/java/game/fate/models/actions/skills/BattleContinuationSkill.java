package game.fate.models.actions.skills;

import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class BattleContinuationSkill implements Action {
    @Override
    public String getName() {
        return "Skill: Battle Continuation";
    }

    @Override
    public int getMpCost() {
        return 15;
    }

    @Override
    public ActionResult execute(Servant src, Servant tgt) {
        src.consumeMp(getMpCost());
        int heal = (int) (src.getMaxHp() * 0.3);
        src.heal(heal);

        return new ActionResult(
                src.getName(),
                null,
                getName(),
                0,
                heal,
                -getMpCost(),
                0,
                false,
                false,
                "Refused to yield to death, restoring HP!"
        );
    }
}
