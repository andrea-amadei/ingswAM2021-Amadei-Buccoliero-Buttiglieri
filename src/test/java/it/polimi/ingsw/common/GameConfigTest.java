package it.polimi.ingsw.common;

import it.polimi.ingsw.parser.JSONParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GameConfigTest {
    @Test
    public void correctlyDeserialize() throws IOException {
        GameConfig config = JSONParser.getGameConfig(Path.of("src/main/config.json"));
        assertEquals("BottomShelf", config.getBaseCupboardShelfNames().get(0));
    }
}