package game.fate.services;

import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.interactionRules.AttackVsBreakRule;
import game.fate.models.interactionRules.BreakVsDefenseRule;
import game.fate.models.interactionRules.DefenseVsAttackRule;
import game.fate.models.interactionRules.InteractionRule;
import game.fate.models.servant.Servant;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage the interaction during a fight
 */
public class CombatEngine {
    private final List<InteractionRule> rules = new ArrayList<>();

    /**
     * Construct the game engine and initialize the interaction rules
     */
    public CombatEngine() {
        rules.add(new AttackVsBreakRule());
        rules.add(new BreakVsDefenseRule());
        rules.add(new DefenseVsAttackRule());
    }

    /**
     * Resolve one turn of the fight using the interaction rules
     *
     * @param servant1 One of the two servant
     * @param action1  Action of the first servant
     * @param servant2 The other servant
     * @param action2  Action of the second servant
     * @return The result(s) of this turn based on the rules
     */
    public ActionResult[] resolveTurn(Servant servant1, Action action1, Servant servant2, Action action2) {
        ActionResult[] results = new ActionResult[2]; // return up to 2 result

        for (InteractionRule rule : rules) {
            if (rule.matches(action1.getType(), action2.getType())) {
                results[0] = rule.resolve(servant1, action1, servant2, action2);
                return results;
            }
        }

        // Fallback for independent actions (NP vs NP, Skill vs Attack, Attack vs Attack, ...)
        // Both execute normally without interrupting each other.
        results[0] = action1.execute(servant1, servant2);
        if (servant2.isAlive()) {
            results[1] = action2.execute(servant2, servant1);
        }
        return results;
    }
}
