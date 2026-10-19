package game.fate;

import game.fate.services.ConsoleManager;
import game.fate.services.GameManager;

public class Main {

    public static void main(String[] args) {
        GameManager game = new GameManager();
        ConsoleManager console = game.getConsole();

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

// TODO: separe action de servant
// TODO: COMMENTS
// TODO: specific class behavior
// TODO: class advantage
// TODO: between fight
// TODO: Win
