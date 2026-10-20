package game.fate.models.master;

import game.fate.models.actions.Action;
import game.fate.models.servantClass.Servant;
import game.fate.services.ConsoleManager;

import java.util.List;

/**
 * A player-controlled Master to selects combat action through the console UI.
 */
public class HumanMaster implements Master {
    private final ConsoleManager console; // The console manager used to prompt the user for input.

    /**
     * Constructs a human-controlled Master linked to the provided UI interface.
     *
     * @param console The active UI manager.
     */
    public HumanMaster(ConsoleManager console) {
        this.console = console;
    }

    /**
     * Prompts the user via the console to manually select an action from the Servant's available options.
     *
     * @param servant The Servant awaiting orders.
     * @return The Action manually chosen by the player.
     */
    @Override
    public Action selectAction(Servant servant) {
        List<Action> actions = servant.getAvailableActions();
        int choice = console.showCombatMenu(actions);
        return actions.get(choice - 1);
    }
}
