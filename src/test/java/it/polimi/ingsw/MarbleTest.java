package it.polimi.ingsw;


import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.market.Marble;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class MarbleTest {

    @Test
    public void validCreateMarble(){
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ConversionActuator actuator = new ConversionActuator(Collections.singletonList(gold), 0);
        Marble m = new Marble(MarbleColor.RED, actuator);

        assertEquals(MarbleColor.RED, m.getColor());
        assertEquals(actuator, m.getBaseConversionActuator());
        assertEquals("R", m.toString());
    }

    @Test
    public void nullCreateMarble(){
        assertThrows(NullPointerException.class, ()->new Marble(null, null));
    }

}
