package it.polimi.ingsw;

import it.polimi.ingsw.server.MatchesManager;
import it.polimi.ingsw.server.clienthandling.ClientHandler;
import it.polimi.ingsw.server.clienthandling.ClientHub;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;


public class ClientHubTest {

    @Test
    public void createHub(){
        ClientHub hub = new ClientHub();
        assertEquals(0, hub.size());
    }

    @Test
    public void validAddClient(){
        ClientHub hub = new ClientHub();
        String username = "pippo";
        ClientHandler clientHandler = new ClientHandler(new Socket(), new MatchesManager());

        assertDoesNotThrow(()->hub.addClient(username, clientHandler));
        assertTrue(hub.contains(username));
        assertNotNull(hub.getClientByName("pippo").getSecond());
    }

    @Test
    public void disconnectClient(){
        ClientHub hub = new ClientHub();
        assertDoesNotThrow(()->hub.addClient("pippo", new ClientHandler(new Socket(), new MatchesManager())));
        hub.disconnectClient("pippo");
        assertNull(hub.getClientByName("pippo").getSecond());
    }

}
