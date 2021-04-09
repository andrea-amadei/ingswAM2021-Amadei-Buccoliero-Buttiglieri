package it.polimi.ingsw;

import it.polimi.ingsw.server.Console;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Console tests")
class ConsoleTest {
    @Test
    @DisplayName("Output")
    public void outputTest() {
        for(Console.Format i : Console.Format.values())
            for(Console.Severity j : Console.Severity.values())
                Console.log("Output test: " + i.name(), j, i);
    }
}