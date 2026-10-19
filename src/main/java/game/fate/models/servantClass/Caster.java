package game.fate.models.servantClass;

import game.fate.models.Servant;
import game.fate.models.ServantClass;

public class Caster extends Servant {

    public Caster(int id, String name, ServantClass baseClassStats, int maxHp, int atk, double criticalMultiplier, double criticalRate) {
        super(id, name, baseClassStats, maxHp, atk, criticalMultiplier, criticalRate);
    }
}
