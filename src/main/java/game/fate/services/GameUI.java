package game.fate.services;

import game.fate.models.Servant;

import java.util.List;
import java.util.Scanner;

/**
 * Class to manage the UI of the game
 */
public class GameUI {
    private final Scanner scanner;

    /**
     * Construct a new GameUI
     */
    public GameUI() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Display the main menu of the game
     *
     * @return The player input
     */
    public int showMainMenu() {
        printHeader("ISEN HOLY GRAIL WAR - MAIN MENU");
        System.out.println(" * This game was inspired by the universe * \n * of Fate/stay night & Fate/Grand Order. * ");
        System.out.println("(1) Start game");
        System.out.println("(2) Continue game");
        System.out.println("(3) Quit game");
        System.out.print("\n> ");

        return readIntInput();
    }

    /**
     * Display the menu to choose a servant
     *
     * @param servants The servant list
     * @return The selected servant
     */
    public Servant showSelectServantMenu(List<Servant> servants) {
        for (int i = 0; i < servants.size(); i++) {
            Servant s = servants.get(i);
            System.out.println(s);
        }
        System.out.print("\nChoose your servant : ");

        while (true) {
            int choice = readIntInput();
            if (choice >= 1 && choice <= servants.size()) {
                return servants.get(choice - 1);
            }
            System.out.print("Invalid choice : ");
        }
    }

    /**
     * Display the action possible during a fight
     *
     * @return The player input
     */
    public int showCombatMenu() {
        printHeader("Your turn");
        System.out.println("(1) Attack");
        System.out.println("(2) NP");
        System.out.print("\n> ");

        return readIntInput();
    }

    /**
     * Display a message
     *
     * @param message The message to display
     */
    public void printMessage(String message) {
        System.out.println("\n> " + message);
    }

    /**
     * Display the game header
     *
     * @param title Title to put inside the header
     */
    public void printHeader(String title) {
        System.out.println("\n========================================");
        System.out.println("   " + title);
        System.out.println("========================================");
    }

    /**
     * Avoid crash if the user enter a string instead of an int
     *
     * @return The user's input
     */
    private int readIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("> ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}
