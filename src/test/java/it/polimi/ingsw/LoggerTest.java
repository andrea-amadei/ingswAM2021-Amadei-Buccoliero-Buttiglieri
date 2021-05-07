package it.polimi.ingsw;

import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.utils.BackgroundColors;
import it.polimi.ingsw.utils.ForegroundColors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Console tests")
class LoggerTest {
    @Test
    @DisplayName("Output")
    public void outputTest() {
        for(ForegroundColors i : ForegroundColors.values())
            for(BackgroundColors j : BackgroundColors.values())
                for(Logger.Severity k : Logger.Severity.values())
                    Logger.log("Output test: " + i.name() + " foreground, " + j.name() + " background", k, i, j);
    }
}