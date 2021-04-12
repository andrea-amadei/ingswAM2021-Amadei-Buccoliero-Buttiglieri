package it.polimi.ingsw;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.PayloadComponent;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class MessageTest {

    @Test
    public void createValidMessage(){
        List<String> targets = Arrays.asList("p1", "p2");
        List<PayloadComponent> payload = Arrays.asList(new InfoPayload("m1"), new InfoPayload("m2"));
        Message message = new Message(targets, payload);
        assertEquals(targets, message.getTargets());
        assertEquals("INFO: m1\nINFO: m2", message.toString());
    }

    @Test
    public void nullTargetList(){
        assertThrows(NullPointerException.class, ()->new Message(null, Arrays.asList(new InfoPayload("m1"), new InfoPayload("m2"))));
    }

    @Test
    public void nullPayloadList(){
        assertThrows(NullPointerException.class, ()->new Message(Arrays.asList("p1", "p2"), null));
    }
}
