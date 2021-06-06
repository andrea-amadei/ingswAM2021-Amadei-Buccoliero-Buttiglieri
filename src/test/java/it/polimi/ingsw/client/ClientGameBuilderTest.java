package it.polimi.ingsw.client;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
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

       String configJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/config.json"));
       String craftingJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/crafting.json"));
       String faithJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/faith.json"));
       String leadersJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/leaders.json"));

       ClientGameBuilder.buildGame(clientModel, usernames, configJSON, craftingJSON, faithJSON, leadersJSON);

       assertEquals(3, clientModel.getPlayers().size());
       assertEquals(3, clientModel.getPlayers().get(0).getCupboard().size());
       assertEquals(0, clientModel.getPlayers().get(1).getLeaderCards().getLeaderCards().size());
   }

}
