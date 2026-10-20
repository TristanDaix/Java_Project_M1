package game.fate.models.servantClass;

/**
 * Enumeration containing the foundational parameters, combat scaling multipliers, and ordering properties
 * for the standard and extra Servant classes within the Fate universe.
 */
public enum ServantClass { // TODO : load value from csv
    SABER(1, 1.0, 1.5, 0.1),
    ARCHER(2, 1.0, 1.5, 0.1),
    LANCER(3, 1.0, 1.5, 0.1),
    RIDER(4, 1.0, 1.5, 0.1),
    CASTER(5, 1.0, 1.5, 0.1),
    ASSASSIN(6, 1.0, 1.5, 0.1),
    BERSERKER(7, 1.0, 1.5, 0.1),
    RULER(8, 1.0, 1.5, 0.1),
    AVENGER(9, 1.0, 1.5, 0.1),
    MOONCANCER(10, 1.0, 1.5, 0.1),
    ALTEREGO(11, 1.0, 1.5, 0.1),
    FOREIGNER(12, 1.0, 1.5, 0.1),
    PRETENDER(13, 1.0, 1.5, 0.1),
    SHIELDER(14, 1.0, 1.5, 0.1);

    private final int sortOrder; // To order the presented servant list based on class
    private final double attackMultiplier; // Multiplicator, multiply the servant attack by this value
    private final double criticalMultiplier; // Multiplicator, multiply damage by this value and the servant own criticalMultiplier if it's a critical hit (so be aware that it will be multiplied 2 times)
    private final double criticalRate; // Percentage (between 0 & 1), addition this value to the servant own criticalRate to know the chance it will be a critical hit

    /**
     * Initializes a class constant with specific gameplay balancing multipliers.
     *
     * @param sortOrder          The integer used to group and sort Servants by class.
     * @param attackMultiplier   The scalar value modifying raw attack power.
     * @param criticalMultiplier The scalar value modifying damage during a critical strike.
     * @param criticalRate       The flat percentage addition to critical strike probability.
     */
    ServantClass(int sortOrder, double attackMultiplier, double criticalMultiplier, double criticalRate) {
        this.sortOrder = sortOrder;
        this.attackMultiplier = attackMultiplier;
        this.criticalMultiplier = criticalMultiplier;
        this.criticalRate = criticalRate;
    }

    /**
     * Standard accessor methods returning the current value of the respective ServantClass modificator.
     */
    public double getSortOrder() {
        return sortOrder;
    }

    public double getAttackMultiplier() {
        return attackMultiplier;
    }

    public double getCriticalMultiplier() {
        return criticalMultiplier;
    }

    public double getCriticalRate() {
        return criticalRate;
    }
}
