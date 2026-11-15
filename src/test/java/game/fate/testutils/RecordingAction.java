package game.fate.testutils;

import game.fate.models.actions.Action;
import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;

/**
 * It has a configurable type, name, MP cost and fixed damage, and remembers how many times it was
 * executed and with which servants. This makes it possible to check which action an
 * interaction rule or the combat engine decided to run.
 */
public class RecordingAction implements Action {

    private final ActionType type;
    private final String name;
    private final int mpCost;
    private final int damage;

    private int executionCount;
    private Servant lastSource;
    private Servant lastTarget;

    /**
     * @param type type reported by the action (no cost, no damage)
     */
    public RecordingAction(ActionType type) {
        this(type, type.name(), 0, 0);
    }

    /**
     * @param type   type reported by the action
     * @param name   name reported by the action
     * @param mpCost MP cost reported by the action
     * @param damage fixed damage inflicted on the target when executed
     */
    public RecordingAction(ActionType type, String name, int mpCost, int damage) {
        this.type = type;
        this.name = name;
        this.mpCost = mpCost;
        this.damage = damage;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMpCost() {
        return mpCost;
    }

    @Override
    public ActionType getType() {
        return type;
    }

    @Override
    public ActionResult execute(Servant source, Servant target) {
        executionCount++;
        lastSource = source;
        lastTarget = target;
        target.takeDamage(damage);
        return new ActionResult(source.getName(), target.getName(), name, -damage, 0, 0, 0,
                false, !target.isAlive(), null);
    }

    /**
     * @return how many times execute was called
     */
    public int getExecutionCount() {
        return executionCount;
    }

    /**
     * @return the source of the last execution, or null if never executed
     */
    public Servant getLastSource() {
        return lastSource;
    }

    /**
     * @return the target of the last execution, or null if never executed
     */
    public Servant getLastTarget() {
        return lastTarget;
    }
}