package game.fate.models.master;

import game.fate.models.actions.Action;
import game.fate.models.servantClass.Servant;
import game.fate.services.ConsoleManager;

import java.util.List;

public class HumanMaster implements Master {
    private final ConsoleManager console;

    public HumanMaster(ConsoleManager console) {
        this.console = console;
    }

    @Override
    public Action selectAction(Servant servant) {
        List<Action> actions = servant.getAvailableActions();
        int choice = console.showCombatMenu(actions);
        return actions.get(choice - 1);
    }
}
