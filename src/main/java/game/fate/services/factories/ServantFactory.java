package game.fate.services.factories;

import game.fate.exceptions.UnknowServantClassException;
import game.fate.models.actions.AttackAction;
import game.fate.models.servantClass.*;

public class ServantFactory {
    public static Servant createServant(int id, String name, ServantClass servantClass,
                                        int maxHp, int atk, double criticalMultiplier, double criticalRate) {

        Servant servant = switch (servantClass) {
            case SABER -> new Saber(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
            case ARCHER -> new Archer(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
            case LANCER -> new Lancer(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
            case RIDER -> new Rider(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
            case CASTER -> new Caster(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
            case ASSASSIN -> new Assassin(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
            case BERSERKER -> new Berserker(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
            case RULER -> new Ruler(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
            case AVENGER -> new Avenger(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
            case MOONCANCER -> new MoonCancer(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
            case ALTEREGO -> new AlterEgo(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
            case FOREIGNER -> new Foreigner(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
            case PRETENDER -> new Pretender(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
            case SHIELDER -> new Shielder(id, name, servantClass, maxHp, atk, criticalMultiplier, criticalRate);
        };

        // Base actions
        servant.addAction(new AttackAction());

        // Noble Phantasm
        // TODO: add np action here

        return servant;
    }
}
