package game.fate.models.interactionRules;

import game.fate.models.actions.Action;
import game.fate.models.actions.ActionType;
import game.fate.models.actions.basics.AttackAction;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class DefenseVsAttackRule implements InteractionRule {
    @Override
    public boolean matches(ActionType type1, ActionType type2) {
        return (type1 == ActionType.DEFENSE && type2 == ActionType.ATTACK) ||
                (type1 == ActionType.ATTACK && type2 == ActionType.DEFENSE);
    }

    @Override
    public ActionResult resolve(Servant servant1, Action action1, Servant servant2, Action action2) {
        boolean servant1Defends = action1.getType() == ActionType.DEFENSE;
        Servant defender = servant1Defends ? servant1 : servant2;
        Servant attacker = servant1Defends ? servant2 : servant1;

        Action defenseAction = servant1Defends ? action1 : action2;
        ActionResult defenseResult = defenseAction.execute(defender, attacker);

        Action counterAttack = new AttackAction();
        ActionResult counterResult = counterAttack.execute(defender, attacker);

        // Only the defense/counter-attack executes, the attack fail
        return new ActionResult(
                defender.getName(),
                attacker.getName(),
                defenseResult.actionName(),
                counterResult.targetHealthChange(),
                0,
                0,
                0,
                counterResult.isCritical(),
                !attacker.isAlive(),
                attacker.getName() + " attack was blocked and " + defender.getName() + " launched a counter-attack!"
        );
    }
}
