package game.fate.models.actions.skills;

import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class VoyagerOfTheStormSkill implements Action {
    @Override
    public String getName() {
        return "Skill: Voyager of the Storm";
    }

    @Override
    public int getMpCost() {
        return 20;
    }

    @Override
    public ActionResult execute(Servant src, Servant tgt) {
        src.consumeMp(getMpCost());
        int damage = (int) (src.getAtk() * 1.5);
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
                "The storm inflicted damage to the enemy!"
        );
    }
}
