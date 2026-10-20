package game.fate.models.servantClass;

import game.fate.models.actions.Action;
import game.fate.models.dto.LevelUpResult;

import java.util.ArrayList;
import java.util.List;

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

    private final List<Action> availableActions;

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
        this.availableActions = new ArrayList<>();
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

    public void addAction(Action action) {
        this.availableActions.add(action);
    }

    public List<Action> getAvailableActions() {
        return availableActions;
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
    public LevelUpResult gainExp(double exp) {
        if (exp < 0) return new LevelUpResult(false, level, level, atk, atk, maxHp, maxHp);

        int startLevel = this.level;
        int startAtk = this.atk;
        int startHp = this.maxHp;

        this.exp += exp;
        while (this.exp >= 100) {
            this.exp -= 100;
            levelUp();
        }

        boolean leveledUp = this.level > startLevel;
        return new LevelUpResult(leveledUp, startLevel, this.level, startAtk, this.atk, startHp, this.maxHp);
    }
}
