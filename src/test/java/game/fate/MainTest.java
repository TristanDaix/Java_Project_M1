package game.fate;

import game.fate.testutils.ConsoleTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for Main, the entry point and main menu loop. The keyboard is simulated with ConsoleTestSupport.
 */
public class MainTest {

    private ConsoleTestSupport io;

    /**
     * Gives the real System.in / System.out back after each test.
     */
    @AfterEach
    void tearDown() {
        if (io != null) {
            io.close();
        }
    }

    /**
     * Choosing 2 displays the goodbye messages and ends the program.
     */
    @Test
    void quitOptionEndsTheProgramWithAGoodbye() {
        io = new ConsoleTestSupport("2\n");

        Main.main(new String[0]);

        assertTrue(io.output().contains("Closing game..."));
        assertTrue(io.output().contains("See you soon, Master !"));
    }

    /**
     * The main menu is displayed before the player chooses.
     */
    @Test
    void mainMenuIsDisplayedAtStartup() {
        io = new ConsoleTestSupport("2\n");

        Main.main(new String[0]);

        assertTrue(io.output().contains("ISEN HOLY GRAIL WAR - MAIN MENU"));
    }

    /**
     * Invalid menu input is refused and the menu waits until option 2 is typed.
     */
    @Test
    void invalidInputIsRefusedUntilAValidChoice() {
        io = new ConsoleTestSupport("7\nstart\n2\n");

        Main.main(new String[0]);

        assertTrue(io.output().contains("Invalid choice."));
        assertTrue(io.output().contains("See you soon, Master !"));
    }
}
