package game.fate.models;

/**
 *
 */
public class Servant {
    private int id;
    private String name;
    private ServantClassStat baseClassStats;
    private int hp;
    private int maxHp;
    private int atk;
    private int level;
    private double exp;


    /**
     * @param id
     * @param name
     * @param baseClassStats
     * @param maxHp
     * @param atk
     */
    public Servant(int id, String name, ServantClassStat baseClassStats, int maxHp, int atk) {
        this.id = id;
        this.name = name;
        this.baseClassStats = baseClassStats;
        this.hp = maxHp;
        this.maxHp = maxHp;
        this.atk = atk;
        this.level = 1;
        this.exp = 0.0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ServantClassStat getBaseClassStats() {
        return baseClassStats;
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
}
