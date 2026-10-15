package game.fate.models;

/**
 * Enumeration to store different constant for each servant class
 */
public enum ServantClassStat { // TODO : load value from csv
    SABER(1.0, 1.0, 1.0),
    ARCHER(1.0, 1.0, 1.0),
    LANCER(1.0, 1.0, 1.0),
    RIDER(1.0, 1.0, 1.0),
    CASTER(1.0, 1.0, 1.0),
    ASSASSIN(1.0, 1.0, 1.0),
    BERSERKER(1.0, 1.0, 1.0),
    RULER(1.0, 1.0, 1.0),
    AVENGER(1.0, 1.0, 1.0),
    MOONCANCER(1.0, 1.0, 1.0),
    ALTEREGO(1.0, 1.0, 1.0),
    FOREIGNER(1.0, 1.0, 1.0),
    SHIELDER(1.0, 1.0, 1.0);

    private final double attackMultiplier;
    private final double criticalMultiplier;
    private final double criticalRate;

    /**
     * Construct a new ServantClassStat
     *
     * @param attackMultiplier   Attack multiplier value
     * @param criticalMultiplier Critical damage multiplier value
     * @param criticalRate       Critical rate value
     */
    ServantClassStat(double attackMultiplier, double criticalMultiplier, double criticalRate) {
        this.attackMultiplier = attackMultiplier;
        this.criticalMultiplier = criticalMultiplier;
        this.criticalRate = criticalRate;
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
