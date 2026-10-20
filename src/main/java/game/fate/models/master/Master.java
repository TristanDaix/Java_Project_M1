package game.fate.models.master;

import game.fate.models.actions.Action;
import game.fate.models.servantClass.Servant;

public interface Master {
    Action selectAction(Servant servant);
}
