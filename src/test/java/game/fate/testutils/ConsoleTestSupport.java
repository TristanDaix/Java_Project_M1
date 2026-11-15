package game.fate.testutils;

import game.fate.services.ConsoleManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Replaces System.in, System.out and System.err for the duration of a test.
 * Captured text is returned without the ANSI colour codes so that assertions can use plain text.
 */
public final class ConsoleTestSupport implements AutoCloseable {

    private static final String ANSI_CODES = "\u001B\\[[;\\d]*m";

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
    private final ByteArrayOutputStream capturedErr = new ByteArrayOutputStream();

    /**
     * Redirects the standard streams.
     *
     * @param keyboardInput the text the user will type
     */
    public ConsoleTestSupport(String keyboardInput) {
        System.setIn(new ByteArrayInputStream(keyboardInput.getBytes(StandardCharsets.UTF_8)));
        System.setOut(new PrintStream(capturedOut, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(capturedErr, true, StandardCharsets.UTF_8));
    }

    /**
     * Builds a ConsoleManager bound to the scripted keyboard.
     *
     * @return a new console manager
     */
    public ConsoleManager createConsole() {
        return new ConsoleManager();
    }

    /**
     * @return everything printed on System.out, without ANSI colour codes
     */
    public String output() {
        return capturedOut.toString(StandardCharsets.UTF_8).replaceAll(ANSI_CODES, "");
    }

    /**
     * @return everything printed on System.err, without ANSI colour codes
     */
    public String errors() {
        return capturedErr.toString(StandardCharsets.UTF_8).replaceAll(ANSI_CODES, "");
    }

    /**
     * Gives the original standard streams back.
     */
    @Override
    public void close() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
