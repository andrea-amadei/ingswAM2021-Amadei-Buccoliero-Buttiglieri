package it.polimi.ingsw;

import it.polimi.ingsw.server.Console;
import it.polimi.ingsw.server.parser.JSONParser;

import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        try {
            JSONParser.parseCraftingCards(Paths.get("src/main/crafting.json"));
        } catch (Exception e) {
            Console.log(e.getClass().getSimpleName() + " - " + e.getMessage(),
                    Console.Severity.ERROR, Console.Format.RED);
        }
    }
}
