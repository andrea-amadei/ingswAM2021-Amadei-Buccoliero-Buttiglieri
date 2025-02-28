package it.polimi.ingsw;

import it.polimi.ingsw.common.exceptions.InvalidFaithPathException;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.actions.Action;
import it.polimi.ingsw.server.model.actions.PopeCheckAction;
import it.polimi.ingsw.server.model.faithpath.FaithPath;
import it.polimi.ingsw.server.model.faithpath.FaithPathGroup;
import it.polimi.ingsw.server.model.faithpath.FaithPathTile;
import it.polimi.ingsw.server.model.fsm.InterruptListener;
import it.polimi.ingsw.server.model.global.Board;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.holder.FaithHolder;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.storage.BaseStorage;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.server.model.storage.Storage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Faith Path tests")
class FaithPathTest {

    private static class DummyListener implements InterruptListener {

        public Action toBeLaunched;
        @Override
        public void launchInterrupt(Action interrupt, int priority) {
            toBeLaunched = interrupt;
        }

        public Action getAction(){
            return toBeLaunched;
        }
    }
    @Test
    @DisplayName("Faith path tile test")
    public void faithPathTileTest() {
        assertThrows(IllegalArgumentException.class, () -> new FaithPathTile(-1, 1, 3, 5, 1, false));
        assertThrows(IllegalArgumentException.class, () -> new FaithPathTile(1, -1, 3, 5, 1, false));
        assertThrows(IllegalArgumentException.class, () -> new FaithPathTile(1, 1, -3, 5, 1, false));
        assertThrows(IllegalArgumentException.class, () -> new FaithPathTile(1, 1, 3, -5, 1, false));
        assertThrows(IllegalArgumentException.class, () -> new FaithPathTile(1, 1, 3, 5, -1, false));

        FaithPathTile tile = new FaithPathTile(1, 1, 3, 5, 1, false);
        assertEquals(tile.getX(), 1);
        assertEquals(tile.getY(), 1);
        assertEquals(tile.getOrder(), 3);
        assertEquals(tile.getVictoryPoints(), 5);
        assertEquals(tile.getPopeGroup(), 1);
        assertFalse(tile.isPopeCheck());
    }

    @Test
    @DisplayName("Construction test")
    public void constructionTest() {
        assertThrows(NullPointerException.class, () -> new FaithPath(null, null));
        assertThrows(IllegalArgumentException.class, () -> new FaithPath(new ArrayList<>(), new ArrayList<>()));

        assertThrows(InvalidFaithPathException.class, () ->
                new FaithPath(new ArrayList<>(), new ArrayList<>() {{
                    add(new FaithPathTile(1, 1, 0, 0, 0, false));
                    add(new FaithPathTile(1, 1, 1, 0, 0, false));
                }}));

        assertThrows(InvalidFaithPathException.class, () ->
                new FaithPath(new ArrayList<>(), new ArrayList<>() {{
                    add(new FaithPathTile(1, 1, 0, 0, 0, false));
                    add(new FaithPathTile(2, 1, 1, 0, 0, false));
                    add(new FaithPathTile(3, 1, 1, 0, 0, false));
                }}));

        assertThrows(InvalidFaithPathException.class, () ->
                new FaithPath(
                    new ArrayList<>(){{
                       add(new FaithPathGroup(1, 2));
                    }},
                    new ArrayList<>() {{
                        add(new FaithPathTile(1, 1, 0, 0, 0, false));
                        add(new FaithPathTile(2, 1, 1, 0, 1, true));
                        add(new FaithPathTile(3, 1, 2, 0, 1, true));
                    }}
                ));

        assertThrows(InvalidFaithPathException.class, () ->
                new FaithPath(new ArrayList<>(), new ArrayList<>() {{
                    add(new FaithPathTile(1, 1, 0, 0, 0, false));
                    add(new FaithPathTile(2, 1, 1, 0, 0, false));
                    add(new FaithPathTile(3, 1, 3, 0, 0, false));
                }}));

        assertThrows(InvalidFaithPathException.class, () ->
                new FaithPath(
                        new ArrayList<>(){{
                            add(new FaithPathGroup(1, 2));
                        }},
                        new ArrayList<>() {{
                            add(new FaithPathTile(1, 1, 0, 0, 0, false));
                            add(new FaithPathTile(2, 1, 1, 0, 1, false));
                            add(new FaithPathTile(3, 1, 2, 0, 1, false));
                        }}
                ));

        assertDoesNotThrow(() ->
                new FaithPath(
                        new ArrayList<>(){{
                            add(new FaithPathGroup(1, 2));
                        }},
                        new ArrayList<>() {{
                            add(new FaithPathTile(1, 1, 0, 0, 0, false));
                            add(new FaithPathTile(2, 1, 1, 0, 1, false));
                            add(new FaithPathTile(3, 1, 2, 0, 1, true));
                        }}
                ));
    }

    @Test
    @DisplayName("Getters test")
    public void getterTest() {
        FaithPath fp = new FaithPath(
                        new ArrayList<>(){{
                            add(new FaithPathGroup(1, 2));
                        }},
                        new ArrayList<>() {{
                            add(new FaithPathTile(1, 1, 0, 0, 0, false));
                            add(new FaithPathTile(2, 1, 1, 0, 1, false));
                            add(new FaithPathTile(3, 1, 2, 0, 1, true));
                        }}
                );

        assertEquals(1, fp.getTotalGroups());
        assertEquals(3, fp.getTotalLength());
    }

    @Test
    @DisplayName("Movement test")
    public void movementTest() {
        FaithPath fp = new FaithPath(
                new ArrayList<>(){{
                    add(new FaithPathGroup(1, 2));
                }},
                new ArrayList<>() {{
                    add(new FaithPathTile(1, 1, 0, 0, 0, false));
                    add(new FaithPathTile(2, 1, 1, 1, 1, false));
                    add(new FaithPathTile(3, 1, 2, 2, 1, true));
                }}
        );

        assertEquals(1, fp.getTotalGroups());
        assertEquals(3, fp.getTotalLength());

        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("testPlayer", 0, b);

        assertEquals(0, player.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(0, player.getPoints());

        assertThrows(NullPointerException.class, () -> fp.executeMovement(1, null));
        assertThrows(IllegalArgumentException.class, () -> fp.executeMovement(0, player));

        assertDoesNotThrow(() -> fp.executeMovement(1, player));
        assertEquals(1, player.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(1, player.getPoints());

        assertDoesNotThrow(() -> fp.executeMovement(3, player));
        assertEquals(2, player.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(3, player.getPoints());
    }

    @Test
    @DisplayName("To String test")
    public void toStringTest() {
        FaithPath fp = new FaithPath(new ArrayList<>(){{
            add(new FaithPathGroup(1,2));
        }},
            new ArrayList<>() {{
            add(new FaithPathTile(1, 1, 0, 0, 1, true));
        }});

        assertEquals("0) (1, 1) 0 pts group 1 CHECK\n", fp.toString());
    }

    @Test
    public void handleMorePopeChecksInAMovement() throws NoSuchFieldException, IllegalAccessException {
        FaithPath fp = new FaithPath(
                new ArrayList<>(){{
                    add(new FaithPathGroup(1, 2));
                    add(new FaithPathGroup(2, 4));
                }},
                new ArrayList<>() {{
                    add(new FaithPathTile(1, 1, 0, 0, 0, false));
                    add(new FaithPathTile(2, 1, 1, 1, 0, false));
                    add(new FaithPathTile(3, 1, 2, 2, 1, false));
                    add(new FaithPathTile(4, 1, 3, 1, 1, true));
                    add(new FaithPathTile(5, 1, 4, 0, 0, false));
                    add(new FaithPathTile(6, 1, 5, 1, 2, false));
                    add(new FaithPathTile(7, 1, 6, 3, 2, true));
                }}
        );
        DummyListener listener = new DummyListener();

        fp.setListener(listener);
        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player p1 = new Player("Alice", 0, b);
        fp.executeMovement(6, p1);
        Field f = ((PopeCheckAction)listener.getAction()).getClass().getDeclaredField("newPopeCheckOrders");
        f.setAccessible(true);
        List<Integer> newPopeCheckOrders = (List<Integer>)f.get((listener.getAction()));
        assertEquals(Arrays.asList(3, 6), newPopeCheckOrders);
        assertEquals(6, p1.getBoard().getFaithHolder().getFaithPoints());
        assertEquals(8, p1.getPoints());
    }

    @Test
    public void addFaithToLorenzoInMultiplayerMode(){
        FaithPath fp = new FaithPath(
                new ArrayList<>(){{
                    add(new FaithPathGroup(1, 2));
                    add(new FaithPathGroup(2, 4));
                }},
                new ArrayList<>() {{
                    add(new FaithPathTile(1, 1, 0, 0, 0, false));
                    add(new FaithPathTile(2, 1, 1, 1, 0, false));
                    add(new FaithPathTile(3, 1, 2, 2, 1, false));
                    add(new FaithPathTile(4, 1, 3, 1, 1, true));
                    add(new FaithPathTile(5, 1, 4, 0, 0, false));
                    add(new FaithPathTile(6, 1, 5, 1, 2, false));
                    add(new FaithPathTile(7, 1, 6, 3, 2, true));
                }}
        );
        DummyListener listener = new DummyListener();

        fp.setListener(listener);

        fp.executeLorenzoMovement(6);
        assertEquals(0, fp.getLorenzoFaith());
    }


    @Test
    public void addFaithToLorenzoInSinglePlayerMode() throws NoSuchFieldException, IllegalAccessException {
        FaithPath fp = new FaithPath(
                new ArrayList<>(){{
                    add(new FaithPathGroup(1, 2));
                    add(new FaithPathGroup(2, 4));
                }},
                new ArrayList<>() {{
                    add(new FaithPathTile(1, 1, 0, 0, 0, false));
                    add(new FaithPathTile(2, 1, 1, 1, 0, false));
                    add(new FaithPathTile(3, 1, 2, 2, 1, false));
                    add(new FaithPathTile(4, 1, 3, 1, 1, true));
                    add(new FaithPathTile(5, 1, 4, 0, 0, false));
                    add(new FaithPathTile(6, 1, 5, 1, 2, false));
                    add(new FaithPathTile(7, 1, 6, 3, 2, true));
                }},
                true
        );
        DummyListener listener = new DummyListener();

        fp.setListener(listener);

        fp.executeLorenzoMovement(6);

        Field f = ((PopeCheckAction)listener.getAction()).getClass().getDeclaredField("newPopeCheckOrders");
        f.setAccessible(true);
        List<Integer> newPopeCheckOrders = (List<Integer>)f.get((listener.getAction()));

        assertEquals(Arrays.asList(3, 6), newPopeCheckOrders);
        assertEquals(6, fp.getLorenzoFaith());
    }


}