package game.fate.models.master;

import game.fate.models.actions.Action;
import game.fate.models.servantClass.Servant;

import java.util.List;

public class AIMaster implements Master {
    public Action selectAction(Servant servant) {
        List<Action> actions = servant.getAvailableActions();
        return actions.get((int) (Math.random() * actions.size()));
    }
}
