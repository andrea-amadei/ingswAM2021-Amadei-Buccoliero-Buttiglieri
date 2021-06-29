package it.polimi.ingsw.common;

import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.utils.ResourceLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameConfigTest {
    @Test
    public void correctlyDeserialize() throws IOException {
        GameConfig config = JSONParser.getGameConfig(ResourceLoader.getPathFromResource("cfg/config.json"));
        assertEquals("BottomShelf", config.getBaseCupboardShelfNames().get(2));
    }
}