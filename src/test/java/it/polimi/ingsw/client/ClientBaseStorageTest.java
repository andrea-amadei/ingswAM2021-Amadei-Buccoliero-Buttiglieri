package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientmodel.ClientBaseStorage;

import it.polimi.ingsw.common.parser.raw.RawStorage;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ClientBaseStorageTest {

    @Test
    public void creation(){
        ClientBaseStorage clientBaseStorage = new ClientBaseStorage(new RawStorage("chest", new HashMap<>()));
        assertEquals(0, clientBaseStorage.getStorage().getResources().size());
    }

    @Test
    public void changeResources(){
        ClientBaseStorage clientBaseStorage = new ClientBaseStorage(new RawStorage("chest", new HashMap<>(){{
            put("gold", 2);
            put("servant", 1);
        }}));

        RawStorage delta = new RawStorage("asdf", new HashMap<>(){{
            put("servant", -1);
            put("gold", 1);
            put("shield", 2);
        }});

        clientBaseStorage.changeResources(delta);

        assertEquals("chest", clientBaseStorage.getStorage().getId());
        assertEquals(new HashMap<String, Integer>(){{
            put("gold", 3);
            put("shield", 2);
        }}, clientBaseStorage.getStorage().getResources());

    }

    @Test
    public void selectResources(){
        ClientBaseStorage clientBaseStorage = new ClientBaseStorage(new RawStorage("chest", new HashMap<>(){{
            put("gold", 2);
            put("servant", 1);
        }}));

        clientBaseStorage.selectResources("gold", 2);
        clientBaseStorage.selectResources("servant", 1);

        assertEquals(new HashMap<String, Integer>(){{put("gold", 2);put("servant", 1);}}, clientBaseStorage.getSelectedResources());
    }
}
