package game.fate.models.actions.basics;

import game.fate.models.actions.Action;
import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class AttackAction implements Action {
    @Override
    public String getName() {
        return "Attack";
    }

    @Override
    public int getMpCost() {
        return 0;
    }

    @Override
    public ActionType getType() {
        return ActionType.ATTACK;
    }

    @Override
    public ActionResult execute(Servant source, Servant target) {
        double totalCritRate = Math.min(1.0, source.getCriticalRate() + source.getServantClass().getCriticalRate());
        double totalCritMultiplier = source.getCriticalMultiplier() * source.getServantClass().getCriticalMultiplier();

        int damage = (int) (source.getAtk() * source.getServantClass().getAttackMultiplier());
        damage += (int) (Math.random() * 20) - 10; // add random value between -10 and 10 to damage

        boolean isCritical = Math.random() < totalCritRate;
        if (isCritical) {
            damage = (int) (damage * totalCritMultiplier);
        }

        target.takeDamage(damage);

        return new ActionResult(
                source.getName(),
                target.getName(),
                getName(),
                -damage,
                0,
                getMpCost(),
                0,
                isCritical,
                !target.isAlive(),
                null
        );
    }
}
