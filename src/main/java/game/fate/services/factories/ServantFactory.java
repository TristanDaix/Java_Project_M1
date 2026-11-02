package game.fate.services.factories;

import game.fate.models.actions.basics.AttackAction;
import game.fate.models.actions.noblephantasms.*;
import game.fate.models.actions.skills.*;
import game.fate.models.servant.*;

/**
 * Creational pattern implementation responsible for abstracting the instantiation of specific
 * Servant subclasses and dynamically attaching default combat actions to them.
 */
public class ServantFactory {
    /**
     * Assembles and returns a specific, fully-equipped Servant derivative based on the provided class literal.
     * Automatically binds universal actions (e.g., AttackAction) to the generated entity.
     *
     * @param id                 The unique registry identifier.
     * @param name               The heroic identity.
     * @param servantClass       The enumeration defining the class wrapper to instantiate.
     * @param maxHp              The base max health.
     * @param atk                The base attack value.
     * @param criticalMultiplier Inherent critical damage multiplier.
     * @param criticalRate       Inherent critical chance addition.
     * @return A constructed and combat-ready Servant object.
     */
    public static Servant createServant(int id, String name, ServantClass servantClass,
                                        int maxHp, int maxMP, int atk, double criticalMultiplier, double criticalRate) {

        Servant servant = new Servant(id, name, servantClass, maxHp, maxMP, atk, criticalMultiplier, criticalRate);

        // Add Basics Actions
        servant.addAction(new AttackAction());

        // Add Class Skill
        switch (servantClass) {
            case SABER -> servant.addAction(new ManaBurstSkill());
            case ARCHER -> servant.addAction(new ClairvoyanceSkill());
            case LANCER -> servant.addAction(new BattleContinuationSkill());
            case RIDER -> servant.addAction(new VoyagerOfTheStormSkill());
            case CASTER -> servant.addAction(new HighSpeedDivineWordsSkill());
            case ASSASSIN -> servant.addAction(new PresenceConcealmentSkill());
            case BERSERKER -> servant.addAction(new ValorSkill());
        }

        // Add Unique Noble Phantasm based on ID
        switch (id) {
            case 1 -> servant.addAction(new ExcaliburNP());
            case 2 -> servant.addAction(new UnlimitedBladeWorksNP());
            case 3 -> servant.addAction(new GaeBolgNP());
            case 4 -> servant.addAction(new BellerophonNP());
            case 5 -> servant.addAction(new RuleBreakerNP());
            case 6 -> servant.addAction(new TsubameGaeshiNP());
            case 7 -> servant.addAction(new NineLivesNP());
        }

        return servant;
    }
}
