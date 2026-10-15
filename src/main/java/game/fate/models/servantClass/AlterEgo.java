package game.fate.models.servantClass;

import game.fate.models.Servant;
import game.fate.models.ServantClassStat;

public class AlterEgo extends Servant {

    public AlterEgo(int id, String name, ServantClassStat baseClassStats, int maxHp, int atk) {
        super(id, name, baseClassStats, maxHp, atk);
    }
}
