package game.fate.models.actions.noblephantasms;

import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class UnlimitedBladeWorksNP implements Action {
    @Override
    public String getName() {
        return "NP: Unlimited Blade Works";
    }

    @Override
    public int getMpCost() {
        return 40;
    }

    @Override
    public ActionResult execute(Servant src, Servant tgt) {
        src.consumeMp(getMpCost());
        int damage = (int) (src.getAtk() * 0.0036 * 1000);
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
                "I am the bone of my sword. Steel is my body, and fire is my blood. I have created over a thousand blades. Unknown to Death. Nor known to Life. Have withstood pain to create many weapons. Yet, those hands will never hold anything. So as I pray, Unlimited Blade Works!"
        );
    }
}
