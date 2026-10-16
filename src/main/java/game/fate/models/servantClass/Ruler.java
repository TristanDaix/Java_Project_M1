package game.fate.models.servantClass;

import game.fate.models.Servant;
import game.fate.models.ServantClass;

public class Ruler extends Servant {

    public Ruler(int id, String name, ServantClass baseClassStats, int maxHp, int atk) {
        super(id, name, baseClassStats, maxHp, atk);
    }
}
