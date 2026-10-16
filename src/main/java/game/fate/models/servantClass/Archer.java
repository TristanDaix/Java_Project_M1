package game.fate.models.servantClass;

import game.fate.models.Servant;
import game.fate.models.ServantClass;

public class Archer extends Servant {

    public Archer(int id, String name, ServantClass baseClassStats, int maxHp, int atk) {
        super(id, name, baseClassStats, maxHp, atk);
    }
}
