package game.fate.models.actions;

import game.fate.models.servantClass.Servant;
import game.fate.models.dto.ActionResult;

public class AttackAction implements Action {
    @Override
    public String getName() {
        return "Attack";
    }

    @Override
    public ActionResult execute(Servant source, Servant target) {
        double totalCritRate = Math.min(1.0, source.getCriticalRate() + source.getServantClass().getCriticalRate());
        double totalCritMultiplier = source.getCriticalMultiplier() * source.getServantClass().getCriticalMultiplier();

        int damage = (int) (source.getAtk() * source.getServantClass().getAttackMultiplier());
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
                isCritical,
                !target.isAlive()
        );
    }
}
