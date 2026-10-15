package game.fate.models.servantClass;

import game.fate.models.Servant;
import game.fate.models.ServantClassStat;

public class Assassin extends Servant {

    public Assassin(int id, String name, ServantClassStat baseClassStats, int maxHp, int atk) {
        super(id, name, baseClassStats, maxHp, atk);
    }
}
