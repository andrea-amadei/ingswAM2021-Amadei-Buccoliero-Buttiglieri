package it.polimi.ingsw;

import it.polimi.ingsw.server.model.basetypes.MarbleColor;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.MarbleFactory;
import it.polimi.ingsw.server.model.market.Market;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MarketTest {

    //seed for test purpose. Configuration:
    /*
        P
        W B W Y
        P Y W G
        B G R W
     */
    private final int seed = 3;
    private Market market;

    @BeforeEach
    public void init(){
        List<Marble> marbles = Arrays.asList(
                MarbleFactory.createMarble(MarbleColor.WHITE),
                MarbleFactory.createMarble(MarbleColor.BLUE),
                MarbleFactory.createMarble(MarbleColor.WHITE),
                MarbleFactory.createMarble(MarbleColor.YELLOW),
                MarbleFactory.createMarble(MarbleColor.PURPLE),
                MarbleFactory.createMarble(MarbleColor.YELLOW),
                MarbleFactory.createMarble(MarbleColor.WHITE),
                MarbleFactory.createMarble(MarbleColor.GREY),
                MarbleFactory.createMarble(MarbleColor.BLUE),
                MarbleFactory.createMarble(MarbleColor.GREY),
                MarbleFactory.createMarble(MarbleColor.RED),
                MarbleFactory.createMarble(MarbleColor.WHITE)
        );
        market = new Market(marbles, MarbleFactory.createMarble(MarbleColor.PURPLE), 3, 4);
    }


    @Test
    public void outOfBoundPickRow(){
        assertThrows(IndexOutOfBoundsException.class, ()->market.pickRow(-1));
        assertThrows(IndexOutOfBoundsException.class, ()->market.pickRow(3));
    }

    @Test
    public void validPickRow(){
        List<Marble> selectedMarbles = new ArrayList<>();

        for(int i = 0; i < market.getColSize(); i++)
            selectedMarbles.add(market.getMarble(0, i));
        Collections.reverse(selectedMarbles);

        market.pickRow(0);
        assertEquals(selectedMarbles, market.getSelectedMarbles());
        assertEquals("W\n" +
                     "B W Y P\n" +
                     "P Y W G\n" +
                     "B G R W", market.toString());
    }

    @Test
    public void outOfBoundPickCol(){
        assertThrows(IndexOutOfBoundsException.class, ()->market.pickCol(-1));
        assertThrows(IndexOutOfBoundsException.class, ()->market.pickCol(4));
    }
    @Test
    public void validPickCol(){
        List<Marble> selectedMarbles = new ArrayList<>();

        for(int i = 0; i < market.getRowSize(); i++)
            selectedMarbles.add(market.getMarble(i, 2));
        Collections.reverse(selectedMarbles);

        market.pickCol(2);
        assertEquals(selectedMarbles, market.getSelectedMarbles());
        assertEquals("W\n" +
                "W B W Y\n" +
                "P Y R G\n" +
                "B G P W", market.toString());
    }

    @Test
    public void outOfBoundGetMarble(){
        assertThrows(IndexOutOfBoundsException.class, () -> market.getMarble(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> market.getMarble(2, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> market.getMarble(3, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> market.getMarble(2, -1));
    }

}
