package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.server.ServerBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        configJson = Files.readString(Path.of("src/main/config.json"));
        leadersJson = Files.readString(Path.of("src/main/leaders.json"));
        craftingJson = Files.readString(Path.of("src/main/crafting.json"));
        faithJson = Files.readString(Path.of("src/main/faith.json"));

    }

    @Test
    public void createModel() throws ParserException {
        GameModel model = ServerBuilder.buildModel(configJson, craftingJson, faithJson, leadersJson, Arrays.asList("Paolo", "Francesca"), false, new Random(3));
        assertFalse(model.getFaithPath().isSinglePlayer());
    }
}
