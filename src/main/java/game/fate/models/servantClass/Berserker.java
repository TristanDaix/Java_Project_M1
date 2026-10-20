package game.fate.models.servantClass;

/**
 * A concrete implementation of the Servant base class representing the designated class in the Holy Grail War.
 */
public class Berserker extends Servant {

    /**
     * Constructs a new Servant belonging to this specific class.
     *
     * @param id                 The unique registry identifier for the Servant.
     * @param name               The true name or alias of the heroic spirit.
     * @param baseClassStats     The enumerated base stat modifiers for this class.
     * @param maxHp              The base maximum health capacity.
     * @param atk                The base attack power rating.
     * @param criticalMultiplier The inherent damage multiplier applied on a critical hit.
     * @param criticalRate       The inherent probability modifier for landing a critical hit.
     */
    public Berserker(int id, String name, ServantClass baseClassStats, int maxHp, int atk, double criticalMultiplier, double criticalRate) {
        super(id, name, baseClassStats, maxHp, atk, criticalMultiplier, criticalRate);
    }
}
