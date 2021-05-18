package it.polimi.ingsw.client;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientGameBuilderTest {

   @Test
   public void correctBuild() throws IOException, ParserException {
       JSONParser.setShowLogs(false);

       ClientModel clientModel = new ClientModel();

       List<String> usernames = new ArrayList<>();
       usernames.add("Ernestino");
       usernames.add("Ernesto");
       usernames.add("Ernestone");

       String configJSON = Files.readString(Path.of("src/main/config.json"));
       String craftingJSON = Files.readString(Path.of("src/main/crafting.json"));
       String faithJSON = Files.readString(Path.of("src/main/faith.json"));
       String leadersJSON = Files.readString(Path.of("src/main/leaders.json"));

       ClientGameBuilder.buildGame(clientModel, usernames, configJSON, craftingJSON, faithJSON, leadersJSON);

       assertEquals(3, clientModel.getPlayers().size());
       assertEquals(3, clientModel.getPlayers().get(0).getCupboard().size());
       assertEquals(0, clientModel.getPlayers().get(1).getLeaderCards().getLeaderCards().size());
   }

}
