package game.fate.models.servantClass;

public class Caster extends Servant {

    public Caster(int id, String name, ServantClass baseClassStats, int maxHp, int atk, double criticalMultiplier, double criticalRate) {
        super(id, name, baseClassStats, maxHp, atk, criticalMultiplier, criticalRate);
    }
}
