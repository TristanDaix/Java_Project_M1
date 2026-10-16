package game.fate.models;

/**
 * Enumeration to store different constant for each servant class
 */
public enum ServantClass { // TODO : load value from csv
    SABER(1, 1.0, 1.0, 1.0),
    ARCHER(2, 1.0, 1.0, 1.0),
    LANCER(3, 1.0, 1.0, 1.0),
    RIDER(4, 1.0, 1.0, 1.0),
    CASTER(5, 1.0, 1.0, 1.0),
    ASSASSIN(6, 1.0, 1.0, 1.0),
    BERSERKER(7, 1.0, 1.0, 1.0),
    RULER(8, 1.0, 1.0, 1.0),
    AVENGER(9, 1.0, 1.0, 1.0),
    MOONCANCER(10, 1.0, 1.0, 1.0),
    ALTEREGO(11, 1.0, 1.0, 1.0),
    FOREIGNER(12, 1.0, 1.0, 1.0),
    PRETENDER(13, 1.0, 1.0, 1.0),
    SHIELDER(14, 1.0, 1.0, 1.0);

    private final int sortOrder;
    private final double attackMultiplier;
    private final double criticalMultiplier;
    private final double criticalRate;

    /**
     * Construct a new ServantClass
     *
     * @param sortOrder          Order to sort the servant by class
     * @param attackMultiplier   Attack multiplier value
     * @param criticalMultiplier Critical damage multiplier value
     * @param criticalRate       Critical rate value
     */
    ServantClass(int sortOrder, double attackMultiplier, double criticalMultiplier, double criticalRate) {
        this.sortOrder = sortOrder;
        this.attackMultiplier = attackMultiplier;
        this.criticalMultiplier = criticalMultiplier;
        this.criticalRate = criticalRate;
    }

    /**
     * Get the order to sort the servant by class
     *
     * @return Order to sort the servant by class
     */
    public double getSortOrder() {
        return sortOrder;
    }

    /**
     * Get attack multiplier value
     *
     * @return Attack multiplier value
     */
    public double getAttackMultiplier() {
        return attackMultiplier;
    }

    /**
     * Get critical damage multiplier value
     *
     * @return Critical damage multiplier value
     */
    public double getCriticalMultiplier() {
        return criticalMultiplier;
    }

    /**
     * Get critical rate value
     *
     * @return Critical rate value
     */
    public double getCriticalRate() {
        return criticalRate;
    }
}
