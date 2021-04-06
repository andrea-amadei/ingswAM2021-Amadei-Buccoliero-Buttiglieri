package it.polimi.ingsw;

import it.polimi.ingsw.server.Console;
import it.polimi.ingsw.server.parser.JSONParser;

public class App {
    public static void main(String[] args) {
        try {
            JSONParser.parseLeaders("src/main/cards.json");
        } catch (Exception e) {
            Console.log(e.getClass().getSimpleName() + " - " + e.getMessage(),
                    Console.Severity.ERROR, Console.Format.RED);
        }
    }
}
