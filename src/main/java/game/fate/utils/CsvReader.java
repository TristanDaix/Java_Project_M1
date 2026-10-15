package game.fate.utils;

import game.fate.exceptions.UnknowServantClassException;
import game.fate.models.Servant;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for reading the different csv file for the application data
 */
public class CsvReader {

    /**
     * Read a csv file to load a list of servant
     *
     * @param filePath The path to the csv file
     * @return The list of servant loaded
     */
    public static List<Servant> loadServants(String filePath) {
        List<Servant> servants = new ArrayList<>();
        Path path = Path.of(filePath);

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.isBlank()) continue;

                servants.add(getServant(line));
            }
        } catch (IOException e) {
            System.err.println("Error: Unable to read file " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Invalid array index " + e.getMessage());
        } catch (UnknowServantClassException e) {
            System.err.println("Error: Unknow servant class " + e.getMessage());
        }

        return servants;
    }

    /**
     * Create the servant from a line of csv
     *
     * @param line The servant's data line
     * @return The servant object
     * @throws NumberFormatException
     * @throws UnknowServantClassException
     */
    private static Servant getServant(String line) throws NumberFormatException, ArrayIndexOutOfBoundsException, UnknowServantClassException {
        String[] fields = line.split(",");

        int id = Integer.parseInt(fields[0].trim());
        String name = fields[1].trim();
        String servantClass = fields[2].trim();
        int hp = Integer.parseInt(fields[3].trim());
        int attack = Integer.parseInt(fields[4].trim());

        return switch (servantClass.toLowerCase()) {
            case "saber" -> new Servant();
            case "archer" -> new Servant();
            case "lancer" -> new Servant();
            case "rider" -> new Servant();
            case "caster" -> new Servant();
            case "assassin" -> new Servant();
            case "berserker" -> new Servant();
            default -> throw new UnknowServantClassException(servantClass);
        };
    }
}
