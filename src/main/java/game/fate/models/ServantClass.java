package game.fate.models;

/**
 * Enumeration to store different constant for each servant class
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
