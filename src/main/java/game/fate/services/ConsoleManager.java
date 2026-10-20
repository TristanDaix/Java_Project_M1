package game.fate.services;

import game.fate.exceptions.NotEnoughServantsException;
import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.dto.LevelUpResult;
import game.fate.models.servantClass.Servant;

import java.util.List;
import java.util.Scanner;

/**
 * Class to manage the UI of the game
 */
public class ConsoleManager {
    private final Scanner scanner;

    // Color ANSI
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";
    public static final String ITALIC = "\u001B[3m";

    /**
     * Construct a new ConsoleManager
     */
    public ConsoleManager() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Display a dialogue
     *
     * @param dialogue The dialogue to display
     */
    public void printActionDialogue(String dialogue) {
        System.out.println(CYAN + ITALIC + "> " + dialogue + RESET);
    }

    /**
     * Display a system dialogue
     *
     * @param dialogue The dialogue
     */
    public void printSystemDialogue(String dialogue) {
        System.out.println(YELLOW + "> [SYSTEM] : \"" + dialogue + "\"" + RESET);
    }

    /**
     * Display a character dialogue
     *
     * @param characterName The name of the character
     * @param dialogue      The dialogue
     */
    public void printCharacterDialogue(String characterName, String dialogue) {
        System.out.println(CYAN + "> [" + characterName + "] : \"" + dialogue + "\"" + RESET);
    }

    /**
     * Display a dialogue option
     *
     * @param nb     The option number
     * @param option The option dialogue
     */
    public void printOption(int nb, String option) {
        System.out.println(GREEN + "(" + nb + ") " + RESET + option);
    }

    /**
     * Display the game header
     *
     * @param title Title to put inside the header
     */
    public void printHeader(String title) {
        System.out.println(RED + BOLD + "\n========================================");
        System.out.println(title);
        System.out.println("========================================" + RESET);
    }

    /**
     * Display a simple separator
     */
    public void printSeparator() {
        System.out.println("----------------------------------------");
    }

    /**
     * Avoid crash if the player enter a string instead of an int
     *
     * @return The player input
     */
    private int readIntInput() {
        System.out.print(GREEN + "> " + RESET);

        while (!scanner.hasNextInt()) {
            System.out.print(GREEN + "> " + RESET);
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    /**
     * Display the main menu of the game
     *
     * @return The player input
     */
    public int showMainMenu() {
        printHeader("ISEN HOLY GRAIL WAR - MAIN MENU");
        System.out.println(" * This game was inspired by the universe * \n * of Fate/Stay Night & Fate/Grand Order. * ");
        printOption(1, "Start game");
        printOption(2, "Quit game");

        while (true) {
            int choice = readIntInput();
            if (choice == 1 || choice == 2) return choice;
            else printSystemDialogue("Invalid choice.");
        }
    }

    /**
     * Display the menu to choose a servant
     *
     * @param servants The servant list
     * @return The selected servant
     */
    public Servant showSelectServantMenu(List<Servant> servants) throws NotEnoughServantsException {
        printHeader("SERVANT SELECTION MENU");

        if (servants.isEmpty()) throw new NotEnoughServantsException("Error: Not enough servant to choose from");

        for (int i = 0; i < servants.size(); i++) {
            printOption(i + 1, servants.get(i).toString());
        }

        while (true) {
            int choice = readIntInput();
            if (choice >= 1 && choice <= servants.size()) {
                Servant servant = servants.get(choice - 1);
                printCharacterDialogue(servant.getName(), "Are you my master ?"); // TODO: class or character specific
                printOption(1, "Yes");
                printOption(2, "No");

                if (readIntInput() == 1) {
                    printActionDialogue(servant.getName() + " is now your servant.");
                    return servant;
                } else printSystemDialogue("Choose another servant.");

            } else printSystemDialogue("Invalid choice.");
        }
    }

    /**
     * Display the action possible during a fight
     *
     * @param availableActions List of action available to use
     * @return The player input
     */
    public int showCombatMenu(List<Action> availableActions) {
        printSystemDialogue("Your turn. Select an action:");
        for (int i = 0; i < availableActions.size(); i++) {
            printOption(i + 1, availableActions.get(i).getName());
        }

        while (true) {
            int choice = readIntInput();
            if (choice >= 1 && choice <= availableActions.size()) {
                return choice;
            }
            printSystemDialogue("Invalid choice. Please select a valid action.");
        }
    }

    /**
     * Display the current status of both servants during a fight
     *
     * @param turn   The current turn number
     * @param player The player's servant
     * @param enemy  The enemy's servant
     */
    public void printCombatStatus(int turn, Servant player, Servant enemy) {
        printSeparator();
        System.out.println("TURN " + turn);

        // Display player
        System.out.println(BLUE + BOLD + "[YOU] " + player.getName() + RESET + " (" + player.getServantClass() + ")");
        System.out.println(GREEN + player.getHp() + " HP " + RESET + " | " + RED + player.getAtk() + " ATK" + RESET);

        System.out.println(" VS ");

        // Display enemy
        System.out.println(BLUE + BOLD + "[ENEMY] " + enemy.getName() + RESET + " (" + enemy.getServantClass() + ")");
        System.out.println(GREEN + enemy.getHp() + " HP " + RESET + " | " + RED + enemy.getAtk() + " ATK" + RESET);

        System.out.print("\n");
    }

    /**
     * Displays the outcome of an Action
     */
    public void printActionResult(ActionResult result) {
        if (result.isCritical()) {
            printActionDialogue("CRITICAL HIT !");
        }

        String effectType = result.healthChange() < 0 ? "damage" : "healing";
        int amount = Math.abs(result.healthChange());

        printActionDialogue(result.sourceName() + " used " + result.actionName() +
                " on " + result.targetName() + " causing " + amount + " " + effectType + " !");

        if (result.targetDefeated()) {
            printActionDialogue(result.targetName() + " has been defeated !");
        }
    }

    public void printLevelUp(LevelUpResult result) {
        if (!result.leveledUp()) return;

        printActionDialogue("LEVEL UP! Reached Level " + result.newLevel() + "!");
        printActionDialogue("ATK: " + result.oldAtk() + " -> " + result.newAtk());
        printActionDialogue("HP: " + result.oldHp() + " -> " + result.newHp());
    }
}
