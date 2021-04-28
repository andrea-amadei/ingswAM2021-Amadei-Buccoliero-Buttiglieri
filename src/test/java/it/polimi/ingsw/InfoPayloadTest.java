package it.polimi.ingsw;


import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.PayloadComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InfoPayloadTest {

    @Test
    public void validCreation(){
        assertDoesNotThrow(()->new InfoPayload(""));
    }

    @Test
    public void nullCreation(){
        assertThrows(NullPointerException.class, () -> new InfoPayload(null));
    }

    @Test
    public void correctCreation(){
        assertEquals("INFO: a message", new InfoPayload("a message").toString());
    }
}
