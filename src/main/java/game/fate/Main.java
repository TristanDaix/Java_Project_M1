package game.fate;

import game.fate.services.ConsoleManager;
import game.fate.services.GameManager;

/**
 * The main entry point for the ISEN Holy Grail War application.
 * This class initializes the game components and manages the main menu execution loop.
 */
public class Main {

    /**
     * Manage the game loop, displaying the main menu and processing the player's
     * top-level choices to either start or terminate the application.
     *
     * @param args Command-line arguments passed during application startup.
     */
    public static void main(String[] args) {
        ConsoleManager console = new ConsoleManager();
        GameManager game = new GameManager(console);

        boolean running = true;
        while (running) {
            int choice = console.showMainMenu();

            switch (choice) {
                case 1:
                    console.printSystemDialogue("Starting game...");
                    game.start();
                    break;

                case 2:
                    console.printSystemDialogue("Closing game...");
                    console.printSystemDialogue("See you soon, Master !");
                    running = false;
                    break;

                default:
                    console.printSystemDialogue("Unknown option.");
            }
        }
    }
}

