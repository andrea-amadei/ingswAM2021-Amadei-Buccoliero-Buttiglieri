package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.server.parser.JSONParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Parser tests")
public class ParserTest {
    @Test
    @DisplayName("Syntax error")
    public void leaderParserTest() {
        assertThrows(ParserException.class, () -> JSONParser.parseLeaders(
                "{\"cards\":[{\"id\":1,\"name\":\"aaa\",\"points\":2"));
    }
}
