package game.fate.testutils;

import game.fate.models.actions.basics.AttackAction;
import game.fate.models.actions.basics.BreakAction;
import game.fate.models.actions.basics.DefenseAction;
import game.fate.models.actions.noblephantasms.ExcaliburNP;
import game.fate.models.actions.skills.ManaBurstSkill;
import game.fate.models.servant.Servant;
import game.fate.models.servant.ServantClass;

/**
 * Small factory of plain Servant objects used by the unit tests.
 * The servants created here are built with the real constructor (no ServantFactory), so they own no actions until a test adds some.
 * Critical hits are disabled by default (multiplier 1.0, rate 0.0) so that damage stays predictable.
 */
public class TestServants {
    private TestServants() {
    }

    /**
     * Creates a servant without any critical hit potential.
     *
     * @param name         display name of the servant
     * @param servantClass class of the servant
     * @param hp           maximum (and starting) health
     * @param mp           maximum (and starting) magic points
     * @param atk          base attack value
     * @return a new servant with id 1, critical multiplier 1.0 and critical rate 0.0
     */
    public static Servant create(String name, ServantClass servantClass, int hp, int mp, int atk) {
        return create(name, servantClass, hp, mp, atk, 1.0, 0.0);
    }

    /**
     * Creates a servant with explicit critical hit stats.
     *
     * @param name               display name of the servant
     * @param servantClass       class of the servant
     * @param hp                 maximum (and starting) health
     * @param mp                 maximum (and starting) magic points
     * @param atk                base attack value
     * @param criticalMultiplier personal critical damage multiplier
     * @param criticalRate       personal critical chance (0.0 to 1.0)
     * @return a new servant with id 1
     */
    public static Servant create(String name, ServantClass servantClass, int hp, int mp, int atk,
                                 double criticalMultiplier, double criticalRate) {
        return new Servant(1, name, servantClass, hp, mp, atk, criticalMultiplier, criticalRate);
    }

    /**
     * @return a SABER named "Attacker" with 1000 HP, 100 MP and 100 ATK
     */
    public static Servant attacker() {
        return create("Attacker", ServantClass.SABER, 1000, 100, 100);
    }

    /**
     * @return a LANCER named "Defender" with 1000 HP, 100 MP and 100 ATK
     */
    public static Servant defender() {
        return create("Defender", ServantClass.LANCER, 1000, 100, 100);
    }

    /**
     * @return a servant with 4 action, 1000 HP, 100 MP and 100 ATK
     */
    public static Servant servantWithAction() {
        Servant servant = create("SaberLion", ServantClass.SABER, 1000, 100, 100);
        servant.addAction(new AttackAction());
        servant.addAction(new DefenseAction());
        servant.addAction(new BreakAction());
        servant.addAction(new ManaBurstSkill());
        servant.addAction(new ExcaliburNP());
        return servant;
    }
}
