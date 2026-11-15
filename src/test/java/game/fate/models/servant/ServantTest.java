package game.fate.models.servant;

import game.fate.models.actions.Action;
import game.fate.models.actions.basics.AttackAction;
import game.fate.models.actions.basics.DefenseAction;
import game.fate.models.dto.LevelUpResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Servant: construction, health and mana point management, level progression and the list of available actions.
 */
public class ServantTest {
    private Servant servant;

    /**
     * Creates a fresh servant before each test.
     */
    @BeforeEach
    void setUp() {
        servant = new Servant(440, "Lilith", ServantClass.BERSERKER, 1000, 100, 100, 1.2, 0.05);
    }

    // ------------------------------------------------------------------ construction

    /**
     * The constructor stores the identity and the base statistics given by the caller.
     */
    @Test
    void constructorStoresIdentityAndStats() {
        assertEquals(440, servant.getId());
        assertEquals("Lilith", servant.getName());
        assertEquals(ServantClass.BERSERKER, servant.getServantClass());
        assertEquals(1000, servant.getMaxHp());
        assertEquals(100, servant.getMaxMp());
        assertEquals(100, servant.getAtk());
        assertEquals(1.2, servant.getCriticalMultiplier());
        assertEquals(0.05, servant.getCriticalRate());
    }

    /**
     * A new servant starts with full health and full magic points.
     */
    @Test
    void newServantStartsWithFullHpAndMp() {
        assertEquals(1000, servant.getHp());
        assertEquals(100, servant.getMp());
    }

    /**
     * A new servant starts at level 1 with no experience.
     */
    @Test
    void newServantStartsAtLevelOneWithoutExperience() {
        assertEquals(1, servant.getLevel());
        assertEquals(0.0, servant.getExp());
    }

    /**
     * A new servant owns no action until some are added.
     */
    @Test
    void newServantHasNoAction() {
        assertTrue(servant.getAvailableActions().isEmpty());
    }

    /**
     * toString summarises class, name, maximum HP, maximum MP and ATK.
     */
    @Test
    void toStringSummarisesClassNameAndStats() {
        assertEquals("BERSERKER Lilith (1000 HP, 100 MP, 100 ATK)", servant.toString());
    }

    // ------------------------------------------------------------------ isAlive / takeDamage

    /**
     * A servant with positive HP is alive.
     */
    @Test
    void servantWithHpIsAlive() {
        assertTrue(servant.isAlive());
    }

    /**
     * takeDamage subtracts the damage from the current HP and leaves the maximum HP untouched.
     */
    @Test
    void takeDamageReducesCurrentHpOnly() {
        servant.takeDamage(250);

        assertEquals(750, servant.getHp());
        assertEquals(1000, servant.getMaxHp());
    }

    /**
     * Damage equal to the remaining HP kills the servant (edge case: exactly 0 HP).
     */
    @Test
    void damageEqualToHpKillsTheServant() {
        servant.takeDamage(1000);

        assertEquals(0, servant.getHp());
        assertFalse(servant.isAlive());
    }

    /**
     * Overkill damage stop at 0 hp.
     */
    @Test
    void overkillDamageStopsAtZeroHp() {
        servant.takeDamage(6666);

        assertEquals(0, servant.getHp());
        assertFalse(servant.isAlive());
    }

    /**
     * Damage of exactly one point less than the HP leaves the servant alive with 1 HP.
     */
    @Test
    void damageOfHpMinusOneLeavesOneHp() {
        servant.takeDamage(999);

        assertEquals(1, servant.getHp());
        assertTrue(servant.isAlive());
    }

    /**
     * Zero or negative damage is ignored.
     */
    @Test
    void zeroOrNegativeDamageIsIgnored() {
        servant.takeDamage(300);

        servant.takeDamage(0);
        servant.takeDamage(-50);

        assertEquals(700, servant.getHp());
    }

    // ------------------------------------------------------------------ heal

    /**
     * heal heal a servant.
     */
    @Test
    void healRestoresHp() {
        servant.takeDamage(500);

        servant.heal(200);

        assertEquals(700, servant.getHp());
    }

    /**
     * Healing never exceeds the maximum HP.
     */
    @Test
    void healIsCappedAtMaxHp() {
        servant.takeDamage(100);

        servant.heal(6666);

        assertEquals(1000, servant.getHp());
    }

    /**
     * Zero or negative healing is ignored.
     */
    @Test
    void zeroOrNegativeHealIsIgnored() {
        servant.takeDamage(400);

        servant.heal(0);
        servant.heal(-100);

        assertEquals(600, servant.getHp());
    }

    // ------------------------------------------------------------------ MP

    /**
     * consumeMp subtracts magic points from the current MP and leaves the maximum untouched.
     */
    @Test
    void consumeMpReducesCurrentMpOnly() {
        servant.consumeMp(30);

        assertEquals(70, servant.getMp());
        assertEquals(100, servant.getMaxMp());
    }

    /**
     * Consuming more MP than available stops at 0.
     */
    @Test
    void consumeMpStopsAtZero() {
        servant.consumeMp(666);

        assertEquals(0, servant.getMp());
    }

    /**
     * Consuming exactly all the MP leaves 0 (edge case).
     */
    @Test
    void consumingExactlyAllMpLeavesZero() {
        servant.consumeMp(100);

        assertEquals(0, servant.getMp());
    }

    /**
     * Zero or negative consumption is ignored.
     */
    @Test
    void zeroOrNegativeMpConsumptionIsIgnored() {
        servant.consumeMp(20);

        servant.consumeMp(0);
        servant.consumeMp(-40);

        assertEquals(80, servant.getMp());
    }

    /**
     * restoreMp restore a servant MP.
     */
    @Test
    void restoreMpAddsMagicPoints() {
        servant.consumeMp(60);

        servant.restoreMp(25);

        assertEquals(65, servant.getMp());
    }

    /**
     * Restoring MP never exceeds the maximum MP.
     */
    @Test
    void restoreMpIsCappedAtMaxMp() {
        servant.consumeMp(10);

        servant.restoreMp(666);

        assertEquals(100, servant.getMp());
    }

    /**
     * Zero or negative restoration is ignored.
     */
    @Test
    void zeroOrNegativeMpRestorationIsIgnored() {
        servant.consumeMp(50);

        servant.restoreMp(0);
        servant.restoreMp(-30);

        assertEquals(50, servant.getMp());
    }

    // ------------------------------------------------------------------ level up

    /**
     * levelUp adds one level and multiplies ATK, max HP and max MP by 1.2.
     */
    @Test
    void levelUpRaisesLevelAndScalesStatsByTwentyPercent() {
        servant.levelUp();

        assertEquals(2, servant.getLevel());
        assertEquals(120, servant.getAtk());
        assertEquals(1200, servant.getMaxHp());
        assertEquals(120, servant.getMaxMp());
    }

    /**
     * levelUp fully heals the servant and refills its MP, using the new maximums.
     */
    @Test
    void levelUpRefillsHpAndMpToTheNewMaximums() {
        servant.takeDamage(900);
        servant.consumeMp(90);

        servant.levelUp();

        assertEquals(1200, servant.getHp());
        assertEquals(120, servant.getMp());
    }

    /**
     * Two successive level ups scale the stat 2 times (100 ATK becomes 120, then 144).
     */
    @Test
    void successiveLevelUpsCompound() {
        servant.levelUp();
        servant.levelUp();

        assertEquals(3, servant.getLevel());
        assertEquals(144, servant.getAtk());
        assertEquals(1440, servant.getMaxHp());
    }

    /**
     * Identity and critical statistics are not affected by a level up.
     */
    @Test
    void levelUpDoesNotChangeIdentityOrCriticalStats() {
        servant.levelUp();

        assertEquals("Lilith", servant.getName());
        assertEquals(ServantClass.BERSERKER, servant.getServantClass());
        assertEquals(1.2, servant.getCriticalMultiplier());
        assertEquals(0.05, servant.getCriticalRate());
    }

    // ------------------------------------------------------------------ experience

    /**
     * Experience below 100 is stored but does not change the level.
     */
    @Test
    void gainExpBelowThresholdOnlyStoresExperience() {
        LevelUpResult result = servant.gainExp(40);

        assertFalse(result.leveledUp());
        assertEquals(1, servant.getLevel());
        assertEquals(40.0, servant.getExp());
        assertEquals(1, result.oldLevel());
        assertEquals(1, result.newLevel());
    }

    /**
     * Reaching exactly 100 experience triggers a level up and resets the experience to 0 (edge case).
     */
    @Test
    void gainingExactlyOneHundredExpLevelsUp() {
        LevelUpResult result = servant.gainExp(100);

        assertTrue(result.leveledUp());
        assertEquals(2, servant.getLevel());
        assertEquals(0.0, servant.getExp());
    }

    /**
     * Just below the threshold (99) there is no level up (edge case).
     */
    @Test
    void gainingNinetyNineExpDoesNotLevelUp() {
        LevelUpResult result = servant.gainExp(99);

        assertFalse(result.leveledUp());
        assertEquals(1, servant.getLevel());
    }

    /**
     * Experience carries over: the part above 100 stays as experience of the new level.
     */
    @Test
    void surplusExperienceIsKept() {
        servant.gainExp(130);

        assertEquals(2, servant.getLevel());
        assertEquals(30.0, servant.getExp());
    }

    /**
     * Experience accumulates over several calls: 60 + 60 crosses the threshold on the second call.
     */
    @Test
    void experienceAccumulatesAcrossCalls() {
        LevelUpResult first = servant.gainExp(60);
        LevelUpResult second = servant.gainExp(60);

        assertFalse(first.leveledUp());
        assertTrue(second.leveledUp());
        assertEquals(2, servant.getLevel());
        assertEquals(20.0, servant.getExp());
    }

    /**
     * A big gain can level up several times at once: 250 EXP gives two levels and 50 EXP left.
     */
    @Test
    void largeExperienceGainCanLevelUpSeveralTimes() {
        servant.gainExp(250);

        assertEquals(3, servant.getLevel());
        assertEquals(50.0, servant.getExp());
        assertEquals(144, servant.getAtk());
    }

    /**
     * The result of a level up describes the statistics before and after.
     */
    @Test
    void levelUpResultDescribesOldAndNewStats() {
        LevelUpResult result = servant.gainExp(250);

        assertTrue(result.leveledUp());
        assertEquals(1, result.oldLevel());
        assertEquals(3, result.newLevel());
        assertEquals(100, result.oldAtk());
        assertEquals(144, result.newAtk());
        assertEquals(1000, result.oldHp());
        assertEquals(1440, result.newHp());
        assertEquals(100, result.oldMp());
        assertEquals(144, result.newMp());
    }

    /**
     * Zero experience changes nothing and reports no level up.
     */
    @Test
    void gainingZeroExpChangesNothing() {
        LevelUpResult result = servant.gainExp(0);

        assertFalse(result.leveledUp());
        assertEquals(1, servant.getLevel());
        assertEquals(0.0, servant.getExp());
    }

    /**
     * Negative experience is rejected.
     */
    @Test
    void negativeExperienceIsIgnored() {
        servant.gainExp(30);

        LevelUpResult result = servant.gainExp(-50);

        assertFalse(result.leveledUp());
        assertEquals(30.0, servant.getExp());
        assertEquals(1, servant.getLevel());
        assertEquals(result.oldAtk(), result.newAtk());
        assertEquals(result.oldHp(), result.newHp());
        assertEquals(result.oldMp(), result.newMp());
    }

    // ------------------------------------------------------------------ actions

    /**
     * Actions are kept in the order they were added.
     */
    @Test
    void addActionKeepsInsertionOrder() {
        Action attack = new AttackAction();
        Action defense = new DefenseAction();

        servant.addAction(attack);
        servant.addAction(defense);

        List<Action> actions = servant.getAvailableActions();
        assertEquals(2, actions.size());
        assertSame(attack, actions.get(0));
        assertSame(defense, actions.get(1));
    }

    /**
     * Two servants never share their action list.
     */
    @Test
    void actionListsAreNotSharedBetweenServants() {
        Servant other = new Servant(2, "Mash", ServantClass.SABER, 1000, 100, 100, 1.0, 0.0); // Don't have a SHIELDER class for Mash so i put a saber by default, apparently Mash and Lilith hate each other but i'm not up do date with the fgo story, and it's not even on the NA server yet. Why am i talking about that here ? It's too long to write test i think i need a break ... i hope i will have enough sq when Lilith will be available on NA, but i also need to save for Olga Marie, Merlin, Caster Skadi, Ruler Skadi and a lot of other servant, aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa, pls fgo reduce the pity, enough break go back to work

        servant.addAction(new AttackAction());

        assertEquals(1, servant.getAvailableActions().size());
        assertTrue(other.getAvailableActions().isEmpty());
    }
}
