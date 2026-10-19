package game.fate.models;

/**
 * Abstract class for servant
 */
public abstract class Servant {
    private final int id;
    private final String name;
    private final ServantClass servantClass;
    private int hp;
    private int maxHp;
    private int atk;
    private int level;
    private double exp;
    private double criticalMultiplier; // Multiplicator, multiply damage by this value and by his class criticalMultiplier if it's a critical hit (so be aware that it will be multiplied 2 times)
    private double criticalRate;  // Percentage (between 0 & 1), addition this value to his class criticalRate to know the chance it will be a critical hit
    private boolean lastAttackWasCritical;


    /**
     * Construct a new servant
     *
     * @param id Servant's id
     * @param name Servant's name
     * @param servantClass Servant's class stats
     * @param maxHp Servant's max hp
     * @param atk Servant's atk
     */
    public Servant(int id, String name, ServantClass servantClass, int maxHp, int atk, double criticalMultiplier, double criticalRate) {
        this.id = id;
        this.name = name;
        this.servantClass = servantClass;
        this.hp = maxHp;
        this.maxHp = maxHp;
        this.atk = atk;
        this.level = 1;
        this.exp = 0.0;
        this.criticalMultiplier = criticalMultiplier;
        this.criticalRate = criticalRate;
        this.lastAttackWasCritical = false;
    }

    @Override
    public String toString() {
        return servantClass + " " + this.name + " (" + this.maxHp + " HP, " + this.atk + " ATK)";
    }

    /**
     * Get the servant's id
     *
     * @return The servant's id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the servant's name
     * @return The servant's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the servant's class stats
     *
     * @return The servant's class stats
     */
    public ServantClass getServantClass() {
        return servantClass;
    }

    /**
     * Get the servant's hp
     * @return The servant's hp
     */
    public int getHp() {
        return hp;
    }

    /**
     * Get the servant's max hp
     * @return The servant's max hp
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Get the servant's atk
     * @return The servant's atk
     */
    public int getAtk() {
        return atk;
    }

    /**
     * Get the servant's level
     * @return The servant's level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get the servant's exp
     * @return The servant's exp
     */
    public double getExp() {
        return exp;
    }

    /**
     * Get the servant's critical multiplier
     *
     * @return The critical multiplier
     */
    public double getCriticalMultiplier() {
        return criticalMultiplier;
    }

    /**
     * Get the servant's critical rate
     *
     * @return The critical rate
     */
    public double getCriticalRate() {
        return criticalRate;
    }

    /**
     * Notify if the last attack was critical
     *
     * @return True if it was, false if not
     */
    public boolean wasLastAttackCritical() {
        return lastAttackWasCritical;
    }

    /**
     * Check if the servant is alive
     *
     * @return if the servant is alive
     */
    public boolean isAlive() {
        return hp > 0;
    }

    /**
     * Reduce the hp based on the damage taken
     *
     * @param amount The amount of damage
     */
    public void takeDamage(int amount) {
        if (amount <= 0) return;
        this.hp -= amount;
        if (hp <= 0) this.hp = 0;
    }

    /**
     * Increase the hp based on the amount of heal received
     *
     * @param amount The amount of heal
     */
    public void heal(int amount) {
        if (amount <= 0) return;
        this.hp += amount;
        if (this.hp > this.maxHp) this.hp = this.maxHp;
    }

    /**
     * Deal damage to a target and return the damage deal
     *
     * @param target The target to attack
     * @return The damage deal
     */
    public int performBasicAttack(Servant target) {
        double totalCritRate = Math.min(1.0, this.criticalRate + this.servantClass.getCriticalRate());
        double totalCritMultiplier = this.criticalMultiplier * this.servantClass.getCriticalMultiplier();

        int damage = (int) (this.atk * servantClass.getAttackMultiplier());
        if (Math.random() < totalCritRate) {
            damage = (int) (damage * totalCritMultiplier);
            this.lastAttackWasCritical = true;
        } else this.lastAttackWasCritical = false;

        target.takeDamage(damage);
        return damage;
    }

    /**
     * Gain a level and increase stat accordingly
     */
    public void levelUp() {
        this.level++;
        this.atk = (int) (this.atk * 1.2);
        this.maxHp = (int)(this.maxHp * 1.2);
    }

    /**
     * Increase the amount of exp possessed (+1 level if >100)
     *
     * @param exp The amount of exp gained
     */
    public void gainExp(double exp) {
        if (exp < 0) return;
        this.exp += exp;

        while (this.exp >= 100) {
            this.exp -= 100;
            levelUp();
        }
    }
}
