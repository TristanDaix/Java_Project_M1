package game.fate.models.actions.basics;

import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

/**
 * Represents a standard physical attack action.
 * Calculates base damage and determines critical hits based on the source Servant's stats and base class modifiers.
 */
public class AttackAction implements Action {
    /**
     * Returns the generic identifier for a base attack.
     *
     * @return The string "Attack".
     */
    @Override
    public String getName() {
        return "Attack";
    }

    /**
     * Return the cost for a base attack
     *
     * @return 0
     */
    @Override
    public int getMpCost() {
        return 0;
    }

    /**
     * Computes total attack power and critical probabilities, applies damage to the target,
     * and compiles the results.
     *
     * @param source The Servant delivering the attack.
     * @param target The Servant receiving the attack.
     * @return The structured outcome of the attack, including damage dealt and critical status.
     */
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
                0,
                0,
                isCritical,
                !target.isAlive(),
                null
        );
    }
}
