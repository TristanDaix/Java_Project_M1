package game.fate.services;

import game.fate.exceptions.NotEnoughServantsException;
import game.fate.models.Servant;
import game.fate.models.ServantClass;
import game.fate.utils.CsvReader;

import java.util.*;

/**
 * Class to manage the game
 */
public class GameManager {
    private final ConsoleManager console;


    /**
     * Construct a new GameManager
     */
    public GameManager() {
        this.console = new ConsoleManager();
    }

    /**
     * Get the console manager
     *
     * @return The console manager
     */
    public ConsoleManager getConsole() {
        return console;
    }

    /**
     * Start the game
     */
    public void start() {
        List<Servant> servants = CsvReader.loadServants("src/main/resources/servants.csv");

        Servant playerServant = this.console.showSelectServantMenu(servants);

        console.printSystemDialogue("Generating the other 6 servant for the Holy Grail War...");
        List<Servant> enemyRoster;
        try {
            enemyRoster = generateEnemyRoster(playerServant, servants);
        } catch (NotEnoughServantsException e) {
            System.err.println(e.getMessage());
            return;
        }

        console.printSystemDialogue("The 6 enemy servants are ");
        enemyRoster.forEach(System.out::println);

        for (Servant enemyServant : enemyRoster) {
            startFight(playerServant, enemyServant); // TODO: stop if lost
        }


    }

    /**
     * Select 6 random enemy servants with distinct classes,
     * and different from the player's servant class.
     *
     * @param playerServant The servant chosen by the player
     * @param allServants   The full list of loaded servants
     * @return A list of 6 distinct enemy servants
     * @throws NotEnoughServantsException Error in the number of servant available
     */
    private List<Servant> generateEnemyRoster(Servant playerServant, List<Servant> allServants) throws NotEnoughServantsException {
        List<Servant> availableServants = new ArrayList<>(allServants.stream()
                .filter(s -> !s.getServantClass().equals(playerServant.getServantClass()))
                .toList());

        Collections.shuffle(availableServants);

        List<Servant> enemyRoster = new ArrayList<>();
        Set<ServantClass> selectedClasses = new HashSet<>(); // to store unique servantClass

        for (Servant servant : availableServants) {
            System.out.println(servant.getServantClass() + " - " + !selectedClasses.contains(servant.getServantClass()));
            if (!selectedClasses.contains(servant.getServantClass())) {
                enemyRoster.add(servant);
                selectedClasses.add(servant.getServantClass());
            }

            System.out.println(enemyRoster.size());
            if (enemyRoster.size() == 6) {
                break;
            }
        }

        if (enemyRoster.size() < 6) {
            throw new NotEnoughServantsException("Error : Not enough different servant class in the list of servants to launch the game");
        }

        return enemyRoster;
    }

    /**
     * Start the fight with two servants
     *
     * @param playerServant The player servant
     * @param enemyServant  The enemy servant
     */
    public void startFight(Servant playerServant, Servant enemyServant) {
        int turn = 1;

        this.console.printHeader("BATTLE START: \n" + playerServant.getName() + " VS " + enemyServant.getName());

        while (playerServant.isAlive() && enemyServant.isAlive()) {
            this.console.printCombatStatus(turn, playerServant, enemyServant);

            int choice = this.console.showCombatMenu();
            if (choice == 1) {
                this.console.printActionDialogue(playerServant.getName() + " attack " + enemyServant.getName() + " !");
                playerServant.performBasicAttack(enemyServant);
            }
            if (choice == 2) {
                // TODO np
                this.console.printActionDialogue(playerServant.getName() + " prepares Noble Phantasm ! (Not implemented yet)");
            }

            if (!enemyServant.isAlive()) {
                this.console.printActionDialogue(enemyServant.getName() + " is defeated !");
                break;
            }

            this.console.printSystemDialogue("Enemy's turn.");

            this.console.printActionDialogue(enemyServant.getName() + " attack " + playerServant.getName() + " !");
            enemyServant.performBasicAttack(playerServant);

            if (!playerServant.isAlive()) {
                this.console.printSystemDialogue(playerServant.getName() + " was defeated !");
            }

            turn++;
        }

        this.console.printHeader("BATTLE ENDED");
    }
}
