package game.fate.models.servantClass;

import game.fate.models.actions.Action;
import game.fate.models.dto.LevelUpResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract foundation representing a summoned Heroic Spirit.
 * Manages core combat attributes, health lifecycle, progression, and available actions.
 */
public abstract class Servant {
    private final int id; // The Servant's unique identifier
    private final String name; // Lore identity
    private final ServantClass servantClass; // Class modifiers
    private int hp; // Current hp
    private int maxHp; // Maximum hp amount possible
    private int atk; // combat attack value
    private int level; // Level metric
    private double exp; // Experience metric
    private final double criticalMultiplier; // Multiplicator, multiply damage by this value and by his class criticalMultiplier if it's a critical hit (so be aware that it will be multiplied 2 times)
    private final double criticalRate;  // Percentage (between 0 & 1), addition this value to his class criticalRate to know the chance it will be a critical hit

    private final List<Action> availableActions; // Dynamic list of usable combat actions

    /**
     * Initializes a base Servant entity with initial stats. Sets default level to 1 and experience to 0.0.
     *
     * @param id                 The unique registry identifier for the Servant.
     * @param name               The identity of the Servant.
     * @param servantClass       The foundational class modifiers.
     * @param maxHp              The initial maximum health.
     * @param atk                The base attack power.
     * @param criticalMultiplier The personal damage multiplier upon landing a critical hit (stacks with class multiplier).
     * @param criticalRate       The personal chance addition to land a critical hit.
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

    /**
     * Returns a formatted summary string containing the Servant's class, name, maximum health, and attack power.
     *
     * @return Formatted character profile string.
     */
    @Override
    public String toString() {
        return servantClass + " " + this.name + " (" + this.maxHp + " HP, " + this.atk + " ATK)";
    }

    /**
     * Standard accessor methods returning the current value of the respective Servant attributes.
     */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ServantClass getServantClass() {
        return servantClass;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAtk() {
        return atk;
    }

    public int getLevel() {
        return level;
    }

    public double getExp() {
        return exp;
    }

    public double getCriticalMultiplier() {
        return criticalMultiplier;
    }

    public double getCriticalRate() {
        return criticalRate;
    }

    public List<Action> getAvailableActions() {
        return availableActions;
    }

    /**
     * Binds a new executable combat action to this Servant's arsenal.
     *
     * @param action The Action to append to the available actions list.
     */
    public void addAction(Action action) {
        this.availableActions.add(action);
    }

    /**
     * Determines if the Servant is currently capable of battling.
     *
     * @return True if current HP is greater than 0, false otherwise.
     */
    public boolean isAlive() {
        return hp > 0;
    }

    /**
     * Subtracts the specified amount from the Servant's current health.
     * Enforces a floor of 0 HP to prevent negative health states.
     *
     * @param amount The numerical damage to be applied. Ignores amounts <= 0.
     */
    public void takeDamage(int amount) {
        if (amount <= 0) return;
        this.hp -= amount;
        if (hp <= 0) this.hp = 0;
    }

    /**
     * Restores health to the Servant up to the strict limit of their maximum HP capacity.
     *
     * @param amount The numerical health to restore. Ignores amounts <= 0.
     */
    public void heal(int amount) {
        if (amount <= 0) return;
        this.hp += amount;
        if (this.hp > this.maxHp) this.hp = this.maxHp;
    }

    /**
     * Increments the Servant's level and scales base attack and maximum health by 20%.
     */
    public void levelUp() {
        this.level++;
        this.atk = (int) (this.atk * 1.2);
        this.maxHp = (int) (this.maxHp * 1.2);
    }

    /**
     * Adds experience to the Servant. Evaluates level-up thresholds (every 100 EXP)
     * and triggers stat scaling automatically if thresholds are crossed.
     *
     * @param exp The amount of experience gained.
     * @return A LevelUpResult detailing the statistical changes during the transaction.
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
