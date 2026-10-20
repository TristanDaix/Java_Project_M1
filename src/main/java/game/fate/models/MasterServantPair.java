package game.fate.models;

import game.fate.models.actions.Action;
import game.fate.models.master.Master;
import game.fate.models.servantClass.Servant;

public class MasterServantPair {
    private final Master master;
    private final Servant servant;

    public MasterServantPair(Master master, Servant servant) {
        this.master = master;
        this.servant = servant;
    }


    public Master getMaster() {
        return master;
    }

    public Servant getServant() {
        return servant;
    }

    public Action issueCommand() {
        return master.selectAction(this.servant);
    }
}
