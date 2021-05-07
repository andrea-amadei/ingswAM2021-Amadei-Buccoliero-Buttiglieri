package it.polimi.ingsw;

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
        Socket socket = new Socket();

        assertDoesNotThrow(()->hub.addClient(username, socket));
        assertTrue(hub.contains(username));
        assertNotNull(hub.getClientByName("pippo").getSecond());
    }

    @Test
    public void disconnectClient(){
        ClientHub hub = new ClientHub();
        assertDoesNotThrow(()->hub.addClient("pippo", new Socket()));
        hub.disconnectClient("pippo");
        assertNull(hub.getClientByName("pippo").getSecond());
    }

}
