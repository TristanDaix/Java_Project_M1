package game.fate.models.servant;

/**
 * Enumeration containing the foundational parameters, combat scaling multipliers, and ordering properties
 * for the standard and extra Servant classes within the Fate universe.
 */
public enum ServantClass {
    SABER(1, 1.0, 1.0, 0.0),
    ARCHER(2, 1.0, 1.0, 0.0),
    LANCER(3, 1.0, 1.0, 0.0),
    RIDER(4, 1.0, 1.0, 0.0),
    CASTER(5, 1.0, 1.05, 0.0),
    ASSASSIN(6, 1.0, 1.0, 0.0),
    BERSERKER(7, 1.0, 1.0, 0.0);

    private int sortOrder; // To order the presented servant list based on class
    private double attackMultiplier; // Multiplicator, multiply the servant attack by this value
    private double criticalMultiplier; // Multiplicator, multiply damage by this value and the servant own criticalMultiplier if it's a critical hit (so be aware that it will be multiplied 2 times)
    private double criticalRate; // Percentage (between 0 & 1), addition this value to the servant own criticalRate to know the chance it will be a critical hit

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
     * Setter to apply the different statistic based on data from a csv file.
     *
     * @param sortOrder          The integer used to group and sort Servants by class.
     * @param attackMultiplier   The scalar value modifying raw attack power.
     * @param criticalMultiplier The scalar value modifying damage during a critical strike.
     * @param criticalRate       The flat percentage addition to critical strike probability.
     */
    public void setStats(int sortOrder, double attackMultiplier, double criticalMultiplier, double criticalRate) {
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
