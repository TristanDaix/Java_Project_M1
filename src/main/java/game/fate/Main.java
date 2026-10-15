package game.fate;

import game.fate.models.Servant;
import game.fate.utils.CsvReader;
import game.fate.utils.GameUI;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GameUI ui = new GameUI();

        List<Servant> servants = CsvReader.loadServants("src/main/resources/servants.csv");

        servants.forEach(System.out::println);


        boolean running = true;
        while (running) {
            int choice = ui.showMainMenu();

            switch (choice) {
                case 1:
                    Servant master = ui.showSelectServantMenu(servants);
                    break;

                case 2:
                    ui.printHeader("Continue");
                    break;

                case 3:
                    ui.printMessage("Closing game. See you soon, Master !");
                    running = false;
                    break;

                default:
                    ui.printMessage("Unknown option.");
            }
        }
    }
}