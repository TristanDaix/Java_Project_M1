package game.fate.services;

import game.fate.exceptions.NotEnoughServantsException;
import game.fate.models.MasterServantPair;
import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.dto.LevelUpResult;
import game.fate.models.master.AIMaster;
import game.fate.models.master.HumanMaster;
import game.fate.models.master.Master;
import game.fate.models.servant.Servant;
import game.fate.models.servant.ServantClass;
import game.fate.utils.CsvReader;

import java.util.*;

/**
 * The core orchestration service. Manages the overarching game lifecycle, opponent generation,
 * and the step-by-step resolution of the battle phase.
 */
public class GameManager {
    private final ConsoleManager console; // The console manager for UI operations.

    /**
     * Initializes the underlying game state and its UI dependencies.
     */
    public GameManager(ConsoleManager console) {
        this.console = console;
    }

    /**
     * Retrieves the UI manager currently serving the game instance.
     *
     * @return The active ConsoleManager.
     */
    public ConsoleManager getConsole() {
        return console;
    }

    /**
     * Executes the primary game pipeline: loading servant data, securing the player's choice,
     * generating an enemy roster, and looping sequentially through combat encounters.
     */
    public void start() {
        CsvReader.loadClasses("src/main/resources/classes.csv");
        List<Servant> servants = CsvReader.loadServants("src/main/resources/servants.csv");
        Servant playerServant;

        try {
            playerServant = this.console.showSelectServantMenu(servants);
        } catch (NotEnoughServantsException e) {
            System.err.println(e.getMessage());
            return;
        }

        Master playerMaster = new HumanMaster(this.console);
        MasterServantPair playerPair = new MasterServantPair(playerMaster, playerServant);

        console.printSystemDialogue("Generating the other 6 servant for the Holy Grail War...");
        List<MasterServantPair> enemyRoster;
        try {
            enemyRoster = generateEnemyRoster(playerServant, servants);
        } catch (NotEnoughServantsException e) {
            System.err.println(e.getMessage());
            return;
        }

        console.printSystemDialogue("The 6 enemy servants are: ");
        enemyRoster.forEach(p -> System.out.println(p.getServant()));

        for (MasterServantPair enemyPair : enemyRoster) {
            startFight(playerPair, enemyPair);

            if (!playerServant.isAlive()) {
                console.printSystemDialogue("Game Over. Your servant fell in battle.");
                break;
            } else {
                playerServant.heal(playerServant.getMaxHp());
                playerServant.restoreMp((playerServant.getMaxMp()));
                console.printActionDialogue(playerServant.getName() + " has been fully healed and restored MP for the next battle.");
            }
        }

    }

    /**
     * Compiles a randomized gauntlet of 6 unique enemy Master-Servant pairs.
     * Enforces the rule that all enemies must be of distinct classes and differ from the player's chosen class.
     *
     * @param playerServant The Servant designated to the player.
     * @param allServants   The full dictionary of available Servants.
     * @return A randomized list of exactly 6 unique enemy pairs.
     * @throws NotEnoughServantsException If the dictionary lacks sufficient class diversity to satisfy generation constraints.
     */
    private List<MasterServantPair> generateEnemyRoster(Servant playerServant, List<Servant> allServants) throws NotEnoughServantsException {
        List<Servant> availableServants = new ArrayList<>(allServants.stream()
                .filter(s -> !s.getServantClass().equals(playerServant.getServantClass()))
                .toList());

        Collections.shuffle(availableServants);

        List<MasterServantPair> enemyRoster = new ArrayList<>();
        Set<ServantClass> selectedClasses = new HashSet<>(); // to store unique servantClass

        for (Servant servant : availableServants) {
            if (!selectedClasses.contains(servant.getServantClass())) {

                for (int i = 0; i < enemyRoster.size(); i++) {
                    servant.levelUp();
                }

                enemyRoster.add(new MasterServantPair(new AIMaster(), servant));
                selectedClasses.add(servant.getServantClass());
            }

            if (enemyRoster.size() == 6) {
                break;
            }
        }

        if (enemyRoster.size() < 6) {
            throw new NotEnoughServantsException("Error : Not enough distinct servant class in the list of servants to launch the game");
        }

        return enemyRoster;
    }

    /**
     * Drives the execution loop of a localized battle between two Master-Servant pairs.
     * Alternates turns, triggers commands, tracks health pools, and resolves victory/defeat conditions.
     *
     * @param playerPair The player-controlled combat unit.
     * @param enemyPair  The AI-controlled opposition unit.
     */
    public void startFight(MasterServantPair playerPair, MasterServantPair enemyPair) {
        Servant playerServant = playerPair.getServant();
        Servant enemyServant = enemyPair.getServant();

        int turn = 1;
        this.console.printHeader("BATTLE START: \n" + playerServant.getName() + " VS " + enemyServant.getName());

        while (playerServant.isAlive() && enemyServant.isAlive()) {
            this.console.printCombatStatus(turn, playerServant, enemyServant);

            // Player Turn
            Action playerAction = playerPair.issueCommand();
            ActionResult playerResult = playerAction.execute(playerServant, enemyServant);
            this.console.printActionResult(playerResult);

            if (!enemyServant.isAlive()) {
                LevelUpResult levelResult = playerServant.gainExp(100);
                this.console.printSystemDialogue(playerServant.getName() + " gained 100 EXP!");
                this.console.printLevelUp(levelResult);
                break;
            }

            // Enemy Turn
            this.console.printSystemDialogue("Enemy's turn.");
            Action enemyAction = enemyPair.issueCommand();
            ActionResult enemyResult = enemyAction.execute(enemyServant, playerServant);
            this.console.printActionResult(enemyResult);

            turn++;
        }
        this.console.printHeader("BATTLE ENDED");
    }
}
