package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.model.market.MarbleFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MarbleFactoryTest {

    @Test
    public void createBlueMarble(){
        assertEquals(MarbleColor.BLUE, MarbleFactory.createMarble(MarbleColor.BLUE).getColor());
    }

    @Test
    public void createWhiteMarble(){
        assertEquals(MarbleColor.WHITE, MarbleFactory.createMarble(MarbleColor.WHITE).getColor());
    }

    @Test
    public void createGreyMarble(){
        assertEquals(MarbleColor.GREY, MarbleFactory.createMarble(MarbleColor.GREY).getColor());
    }

    @Test
    public void createRedMarble(){
        assertEquals(MarbleColor.RED, MarbleFactory.createMarble(MarbleColor.RED).getColor());
    }

    @Test
    public void createPurpleMarble(){
        assertEquals(MarbleColor.PURPLE, MarbleFactory.createMarble(MarbleColor.PURPLE).getColor());
    }

    @Test
    public void createYellowMarble(){
        assertEquals(MarbleColor.YELLOW, MarbleFactory.createMarble(MarbleColor.YELLOW).getColor());
    }

    @Test
    public void nullColorCreation(){
        assertThrows(NullPointerException.class, ()->MarbleFactory.createMarble(null));
    }
}
