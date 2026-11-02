package game.fate.models.actions.skills;

import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class HighSpeedDivineWordsSkill implements Action {
    @Override
    public String getName() {
        return "Skill: High-Speed Divine Words";
    }

    @Override
    public int getMpCost() {
        return 0;
    }

    @Override
    public ActionResult execute(Servant src, Servant tgt) {
        src.restoreMp(50);

        return new ActionResult(
                src.getName(),
                null,
                getName(),
                0,
                0,
                50,
                0,
                false,
                false,
                "Chanted ancient words, rapidly restoring MP!"
        );
    }
}
