package game.fate.models.actions.noblephantasms;

import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class RuleBreakerNP implements Action {
    @Override
    public String getName() {
        return "NP: Rule Breaker";
    }

    @Override
    public int getMpCost() {
        return 40;
    }

    @Override
    public ActionResult execute(Servant src, Servant tgt) {
        src.consumeMp(getMpCost());
        int damage = (int) (src.getAtk() * 1.5);
        tgt.takeDamage(damage);
        tgt.consumeMp(30);
        src.restoreMp(30);

        return new ActionResult(
                src.getName(),
                tgt.getName(),
                getName(),
                -damage,
                0,
                -getMpCost() + 30,
                -30,
                false,
                !tgt.isAlive(),
                "All magical contracts severed! Stole 30 MP!"
        );
    }
}
