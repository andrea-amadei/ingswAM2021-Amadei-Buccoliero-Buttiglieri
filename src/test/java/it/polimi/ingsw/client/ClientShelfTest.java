package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientmodel.ClientShelf;
import it.polimi.ingsw.common.parser.raw.RawStorage;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
public class ClientShelfTest {
    @Test
    public void creation(){
        ClientShelf clientShelf = new ClientShelf("BottomShelf", "any", 3, new RawStorage("BottomShelf", new HashMap<>()));
        assertEquals(0, clientShelf.getStorage().getResources().size());
    }

    @Test
    public void changeResources(){
        ClientShelf clientShelf = new ClientShelf("BottomShelf", "any", 3, new RawStorage("BottomShelf", new HashMap<>(){{
            put("gold", 2);
            put("servant", 1);
        }}));

        RawStorage delta = new RawStorage("asdf", new HashMap<>(){{
            put("servant", -1);
            put("gold", 1);
            put("shield", 2);
        }});

        clientShelf.changeResources(delta);

        assertEquals("BottomShelf", clientShelf.getStorage().getId());
        assertEquals(new HashMap<String, Integer>(){{
            put("gold", 3);
            put("shield", 2);
        }}, clientShelf.getStorage().getResources());

    }

    @Test
    public void selectResources(){
        ClientShelf clientShelf = new ClientShelf("BottomShelf", "any", 3, new RawStorage("BottomShelf", new HashMap<>(){{
            put("gold", 2);
            put("servant", 1);
        }}));

        clientShelf.selectResources("gold", 2);
        clientShelf.selectResources("servant", 1);

        assertEquals(new HashMap<String, Integer>(){{put("gold", 2);put("servant", 1);}}, clientShelf.getSelectedResources());
    }
}
