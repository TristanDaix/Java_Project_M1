package game.fate.models.interactionRules;

import game.fate.models.actions.Action;
import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class AttackVsBreakRule implements InteractionRule {
    @Override
    public boolean matches(ActionType type1, ActionType type2) {
        return (type1 == ActionType.ATTACK && type2 == ActionType.BREAK) ||
                (type1 == ActionType.BREAK && type2 == ActionType.ATTACK);
    }

    @Override
    public ActionResult resolve(Servant servant1, Action action1, Servant servant2, Action action2) {
        boolean servant1Attacked = action1.getType() == ActionType.ATTACK;
        Servant attacker = servant1Attacked ? servant1 : servant2;
        Action attackAction = servant1Attacked ? action1 : action2;
        Servant target = servant1Attacked ? servant2 : servant1;

        ActionResult result = attackAction.execute(attacker, target);

        // Only the attack executes, the break fail
        return new ActionResult(
                result.sourceName(),
                result.targetName(),
                result.actionName(),
                result.targetHealthChange(),
                result.sourceHealthChange(),
                result.sourceMpChange(),
                result.targetMpChange(),
                result.isCritical(),
                result.targetDefeated(),
                result.targetName() + " break couldn't do anything against " + result.sourceName() + " attack!"
        );
    }
}
