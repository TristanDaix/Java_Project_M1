package game.fate.services;

import game.fate.exceptions.NotEnoughServantsException;
import game.fate.models.actions.Action;
import game.fate.models.dto.ActionResult;
import game.fate.models.dto.LevelUpResult;
import game.fate.models.servant.Servant;

import java.util.List;
import java.util.Scanner;

/**
 * Dedicated presentation layer utility handling terminal-based UI rendering,
 * stylized ANSI output formatting, and safe user input ingestion.
 */
public class ConsoleManager {
    private final Scanner scanner; // Input stream reader

    // Constant definitions for ANSI escape sequences to colorize text.
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
     * Initializes the UI manager and binds its scanner to standard system input.
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
     * Captures a safe integer from the user. Re-prompts continuously upon invalid string/character inputs
     * to prevent application crashes.
     *
     * @return The validated integer input.
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
     * Renders the primary application title screen and captures the user's intent to start or exit.
     *
     * @return The validated integer corresponding to the user's menu choice.
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
     * Displays an interactive roster for the player to select their participating Servant for the Holy Grail War.
     * Requires user confirmation to finalize the contract.
     *
     * @param servants The list of available Servants parsed from data sources.
     * @return The confirmed Servant chosen by the player.
     * @throws NotEnoughServantsException If the provided servant list is empty.
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
                printCharacterDialogue(servant.getName(), "Are you my master ?");
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
     * Renders the dynamic combat menu based on the acting Servant's loaded skills.
     *
     * @param availableActions The list of actions the current Servant is permitted to execute.
     * @param player The player's servant to check if he has enough MP to perform the selected action.
     * @return The validated index corresponding to the player's chosen attack.
     */
    public int showCombatMenu(List<Action> availableActions, Servant player) {
        printSystemDialogue("Your turn. Select an action:");
        for (int i = 0; i < availableActions.size(); i++) {
            Action action = availableActions.get(i);
            printOption(i + 1, action.getName() + " [" + action.getMpCost() + " MP]");
        }

        while (true) {
            int choice = readIntInput();
            if (choice >= 1 && choice <= availableActions.size()) {
                if (player.getMp() >= availableActions.get(choice - 1).getMpCost()) {
                    return choice;
                } else {
                    printSystemDialogue("Not enough MP for that action!");
                }
            } else printSystemDialogue("Invalid choice.");
        }
    }

    /**
     * Renders the current state of the battlefield, indicating turn progression and displaying
     * comparative health and attack values for both combatants.
     *
     * @param turn   The current chronological turn number.
     * @param player The Servant allied with the player.
     * @param enemy  The opposing enemy Servant.
     */
    public void printCombatStatus(int turn, Servant player, Servant enemy) {
        printSeparator();
        System.out.println("TURN " + turn);

        // Display player
        System.out.println(BLUE + BOLD + "[YOU] " + player.getName() + RESET + " (" + player.getServantClass() + ")");
        System.out.println(GREEN + player.getHp() + " HP " + RESET + " | " + PURPLE + player.getMp() + " MP " + RESET + " | " + RED + player.getAtk() + " ATK" + RESET);

        System.out.println(" VS ");

        // Display enemy
        System.out.println(BLUE + BOLD + "[ENEMY] " + enemy.getName() + RESET + " (" + enemy.getServantClass() + ")");
        System.out.println(GREEN + enemy.getHp() + " HP " + RESET + " | " + PURPLE + enemy.getMp() + " MP " + RESET + " | " + RED + enemy.getAtk() + " ATK" + RESET);

        System.out.print("\n");
    }

    /**
     * Displays a string describing the execution and consequences of a combat action
     * using the decoupled ActionResult payload.
     *
     * @param result The DTO containing the mathematical and status results of the action.
     */
    public void printActionResult(ActionResult result) {
        // Announce the action
        if (result.targetName() != null && !result.sourceName().equals(result.targetName())) {
            printActionDialogue(result.sourceName() + " used " + result.actionName() + " on " + result.targetName() + "!");
        } else {
            printActionDialogue(result.sourceName() + " used " + result.actionName() + "!");
        }

        // Display the special message
        if (result.specialMessage() != null && !result.specialMessage().isBlank()) {
            printActionDialogue(result.specialMessage());
        }

        // Display Critical Hit status
        if (result.isCritical()) {
            printActionDialogue(YELLOW + BOLD + "CRITICAL HIT !" + RESET);
        }

        // Display Target Health changes (Damage or Healing)
        if (result.targetHealthChange() < 0) {
            printActionDialogue(result.targetName() + " took " + RED + Math.abs(result.targetHealthChange()) + " damage" + RESET + ".");
        } else if (result.targetHealthChange() > 0) {
            printActionDialogue(result.targetName() + " was healed for " + GREEN + result.targetHealthChange() + " HP" + RESET + ".");
        }

        // Display Source Health changes (Recoil damage or Lifesteal/Self-heal)
        if (result.sourceHealthChange() < 0) {
            printActionDialogue(result.sourceName() + " took " + RED + Math.abs(result.sourceHealthChange()) + " recoil damage" + RESET + ".");
        } else if (result.sourceHealthChange() > 0) {
            printActionDialogue(result.sourceName() + " recovered " + GREEN + result.sourceHealthChange() + " HP" + RESET + ".");
        }

        // Display Source MP changes (MP Cost consumed or MP Restored)
        if (result.sourceMpChange() < 0) {
            printActionDialogue(result.sourceName() + " consumed " + PURPLE + Math.abs(result.sourceMpChange()) + " MP" + RESET + ".");
        } else if (result.sourceMpChange() > 0) {
            printActionDialogue(result.sourceName() + " restored " + PURPLE + result.sourceMpChange() + " MP" + RESET + ".");
        }

        // Display Target MP changes (MP Drain / MP Burn from enemies)
        if (result.targetMpChange() < 0) {
            printActionDialogue(result.targetName() + " lost " + PURPLE + Math.abs(result.targetMpChange()) + " MP" + RESET + ".");
        } else if (result.targetMpChange() > 0) {
            printActionDialogue(result.targetName() + " gained " + PURPLE + result.targetMpChange() + " MP" + RESET + ".");
        }

        // Display Death state
        if (result.targetDefeated()) {
            printActionDialogue(RED + BOLD + result.targetName() + " has been defeated !" + RESET);
        }
    }

    /**
     * Evaluates a level-up payload and visually announces statistical growth if a level boundary was crossed.
     *
     * @param result The DTO detailing pre and post-experience stats.
     */
    public void printLevelUp(LevelUpResult result) {
        if (!result.leveledUp()) return;
        printActionDialogue("LEVEL UP! Reached Level " + result.newLevel() + "!");
        printActionDialogue("ATK: " + result.oldAtk() + " -> " + result.newAtk());
        printActionDialogue("HP: " + result.oldHp() + " -> " + result.newHp());
        printActionDialogue("MP: " + result.oldMp() + " -> " + result.newMp());
    }
}
