package game.fate.models.interactionRules;

import game.fate.models.actions.Action;
import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

public class BreakVsDefenseRule implements InteractionRule {
    @Override
    public boolean matches(ActionType type1, ActionType type2) {
        return (type1 == ActionType.BREAK && type2 == ActionType.DEFENSE) ||
                (type1 == ActionType.DEFENSE && type2 == ActionType.BREAK);
    }

    @Override
    public ActionResult resolve(Servant servant1, Action action1, Servant servant2, Action action2) {
        boolean servant1Breaks = action1.getType() == ActionType.BREAK;
        Servant breaker = servant1Breaks ? servant1 : servant2;
        Action breakAction = servant1Breaks ? action1 : action2;
        Servant defender = servant1Breaks ? servant2 : servant1;

        ActionResult result = breakAction.execute(breaker, defender);

        // Only the break executes, the defense fail
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
                result.sourceName() + " break the defense of " + result.targetName() + "!"
        );
    }
}
