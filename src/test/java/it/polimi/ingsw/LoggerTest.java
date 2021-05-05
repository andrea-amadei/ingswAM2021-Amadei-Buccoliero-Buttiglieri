package it.polimi.ingsw;

import it.polimi.ingsw.server.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Console tests")
class LoggerTest {
    @Test
    @DisplayName("Output")
    public void outputTest() {
        for(Logger.Format i : Logger.Format.values())
            for(Logger.Severity j : Logger.Severity.values())
                Logger.log("Output test: " + i.name(), j, i);
    }
}