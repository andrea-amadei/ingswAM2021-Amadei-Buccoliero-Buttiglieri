package it.polimi.ingsw;

import it.polimi.ingsw.model.GameParameters;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.Market;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MarketTest {

    //seed for test purpose. Configuration:
    /*
        P
        W B W Y
        P Y W G
        B G R W
     */
    private final int seed = 3;


    @Test
    public void validCreation(){
        assertDoesNotThrow((ThrowingSupplier<Market>) Market::new);
    }

    @Test
    public void validSeededCreation(){
        Market market = new Market(new Random(seed));
    }

    @Test
    public void outOfBoundPickRow(){
        Market market = new Market(new Random(seed));
        assertThrows(IndexOutOfBoundsException.class, ()->market.pickRow(-1));
        assertThrows(IndexOutOfBoundsException.class, ()->market.pickRow(GameParameters.MARKET_ROWS));
    }

    @Test
    public void validPickRow(){
        Market market = new Market(new Random(seed));
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
        Market market = new Market(new Random(seed));
        assertThrows(IndexOutOfBoundsException.class, ()->market.pickCol(-1));
        assertThrows(IndexOutOfBoundsException.class, ()->market.pickCol(GameParameters.MARKET_COLUMNS));
    }
    @Test
    public void validPickCol(){
        Market market = new Market(new Random(seed));
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
        Market m = new Market(new Random(seed));
        assertThrows(IndexOutOfBoundsException.class, () -> m.getMarble(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.getMarble(2, GameParameters.MARKET_COLUMNS));
        assertThrows(IndexOutOfBoundsException.class, () -> m.getMarble(GameParameters.MARKET_ROWS, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.getMarble(2, -1));
    }

}
