package it.polimi.ingsw;

import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ServerBuilderTest {
    private static String configJson;
    private static String leadersJson;
    private static String craftingJson;
    private static String faithJson;

    @BeforeEach
    public void init() throws IOException {
        configJson = Files.readString(ResourceLoader.getPathFromResource("cfg/config.json"));
        craftingJson = Files.readString(ResourceLoader.getPathFromResource("cfg/crafting.json"));
        faithJson = Files.readString(ResourceLoader.getPathFromResource("cfg/faith.json"));
        leadersJson = Files.readString(ResourceLoader.getPathFromResource("cfg/leaders.json"));

    }

    @Test
    public void createModel() throws ParserException {
        GameModel model = ServerBuilder.buildModel(configJson, craftingJson, faithJson, leadersJson, Arrays.asList("Paolo", "Francesca"), false, new Random(3));
        assertFalse(model.getFaithPath().isSinglePlayer());
    }
}
