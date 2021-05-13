package it.polimi.ingsw;

import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.utils.BackgroundColor;
import it.polimi.ingsw.utils.ForegroundColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Console tests")
class LoggerTest {
    @Test
    @DisplayName("Output")
    public void outputTest() {
        for(ForegroundColor i : ForegroundColor.values())
            for(BackgroundColor j : BackgroundColor.values())
                for(Logger.Severity k : Logger.Severity.values())
                    Logger.log("Output test: " + i.name() + " foreground, " + j.name() + " background", k, i, j);
    }
}