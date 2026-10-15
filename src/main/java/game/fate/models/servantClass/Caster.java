package game.fate.models.servantClass;

import game.fate.models.Servant;
import game.fate.models.ServantClassStats;

public class Caster extends Servant {

    public Caster(int id, String name, ServantClassStats baseClassStats, int maxHp, int atk) {
        super(id, name, baseClassStats, maxHp, atk);
    }
}
