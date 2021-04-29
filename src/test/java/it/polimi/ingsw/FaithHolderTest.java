package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.AlreadyReachedPopeCardException;
import it.polimi.ingsw.exceptions.UsedUnreachedPopeCardException;
import it.polimi.ingsw.model.holder.FaithHolder;
import org.junit.jupiter.api.Test;

public class FaithHolderTest {
    @Test
    public void faithHolderExceptionsTest() {
        FaithHolder fh = new FaithHolder();

        assertThrows(IndexOutOfBoundsException.class, () -> fh.isPopeCardReached(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> fh.isPopeCardActive(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> fh.setPopeCardActive(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> fh.setPopeCardInactive(-1));

        assertThrows(IllegalArgumentException.class, () -> fh.addFaithPoints(-5));

        assertDoesNotThrow(() -> fh.setPopeCardActive(0));
        assertThrows(UsedUnreachedPopeCardException.class, () -> fh.setPopeCardInactive(0));


    }

    @Test
    public void faithHolderGettersAndSettersTest() {
        FaithHolder fh = new FaithHolder();

        assertEquals(fh.getFaithPoints(), 0);
        assertFalse(fh.isPopeCardReached(0));
        assertFalse(fh.isPopeCardActive(0));


        fh.setPopeCardActive(0);
        assertTrue(fh.isPopeCardActive(0));

        fh.addFaithPoints(10);
        assertEquals(fh.getFaithPoints(), 10);
        fh.resetFaithPoints();
        assertEquals(fh.getFaithPoints(), 0);
    }
}
