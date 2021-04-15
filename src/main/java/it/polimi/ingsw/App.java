package it.polimi.ingsw;

import it.polimi.ingsw.server.Console;
import it.polimi.ingsw.parser.JSONParser;

import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        JSONParser.setDebugMode(false);

        try {
            JSONParser.parseLeaders(Paths.get("src/main/leaders.json"));
            JSONParser.parseCraftingCards(Paths.get("src/main/crafting.json"));
            JSONParser.parseBaseCrafting(Paths.get("src/main/crafting.json"));
            JSONParser.parseFaithPath(Paths.get("src/main/faith.json"));
        } catch (Exception e) {
            Console.log(e.getClass().getSimpleName() + " - " + e.getMessage(),
                    Console.Severity.ERROR, Console.Format.RED);
        }
    }
}
