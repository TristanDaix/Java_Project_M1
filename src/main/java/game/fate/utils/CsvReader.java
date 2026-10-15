package game.fate.utils;

import game.fate.exceptions.UnknowServantClassException;
import game.fate.models.Servant;
import game.fate.models.ServantClassStats;
import game.fate.models.servantClass.*;

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
     * @throws NumberFormatException          Error in number format
     * @throws ArrayIndexOutOfBoundsException Error in number of arguments
     * @throws UnknowServantClassException    Error in servant class
     */
    private static Servant getServant(String line) throws NumberFormatException, ArrayIndexOutOfBoundsException, UnknowServantClassException {
        String[] fields = line.split(",");

        int id = Integer.parseInt(fields[0].trim());
        String name = fields[1].trim();
        String servantClass = fields[2].trim();
        int maxHp = Integer.parseInt(fields[3].trim());
        int atk = Integer.parseInt(fields[4].trim());

        return switch (servantClass.toLowerCase()) {
            case "saber" -> new Saber(id, name, ServantClassStats.SABER, maxHp, atk);
            case "archer" -> new Archer(id, name, ServantClassStats.ARCHER, maxHp, atk);
            case "lancer" -> new Lancer(id, name, ServantClassStats.LANCER, maxHp, atk);
            case "rider" -> new Rider(id, name, ServantClassStats.RIDER, maxHp, atk);
            case "caster" -> new Caster(id, name, ServantClassStats.CASTER, maxHp, atk);
            case "assassin" -> new Assassin(id, name, ServantClassStats.ASSASSIN, maxHp, atk);
            case "berserker" -> new Berserker(id, name, ServantClassStats.BERSERKER, maxHp, atk);
            case "ruler" -> new Ruler(id, name, ServantClassStats.RULER, maxHp, atk);
            case "avenger" -> new Avenger(id, name, ServantClassStats.AVENGER, maxHp, atk);
            case "mooncancer" -> new MoonCancer(id, name, ServantClassStats.MOONCANCER, maxHp, atk);
            case "alterego" -> new AlterEgo(id, name, ServantClassStats.ALTEREGO, maxHp, atk);
            case "foreigner" -> new Foreigner(id, name, ServantClassStats.FOREIGNER, maxHp, atk);
            case "pretender" -> new Pretender(id, name, ServantClassStats.PRETENDER, maxHp, atk);
            case "shielder" -> new Shielder(id, name, ServantClassStats.SHIELDER, maxHp, atk);
            default -> throw new UnknowServantClassException(servantClass);
        };
    }
}
