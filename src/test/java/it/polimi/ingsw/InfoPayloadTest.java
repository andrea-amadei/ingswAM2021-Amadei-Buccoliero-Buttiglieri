package it.polimi.ingsw;


import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InfoPayloadTest {

    @Test
    public void validCreation(){
        assertDoesNotThrow(()->new InfoPayloadComponent(""));
    }

    @Test
    public void nullCreation(){
        assertThrows(NullPointerException.class, () -> new InfoPayloadComponent(null));
    }

    @Test
    public void correctCreation(){
        assertEquals("INFO: a message", new InfoPayloadComponent("a message").toString());
    }
}
