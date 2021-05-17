package it.polimi.ingsw.client;

import it.polimi.ingsw.clientproto.model.ClientShop;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawCraftingCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ClientShopTest {

    private static List<RawCraftingCard> craftingCards;

    @BeforeEach
    public void init() throws ParserException, IOException {
        JSONParser.setShowLogs(false);
        craftingCards = JSONParser.parseCraftingCards(Path.of("src/main/crafting.json")).stream().map(CraftingCard::toRaw).collect(Collectors.toList());
    }
    @Test
    public void creation(){
        ClientShop shop  = new ClientShop(3, 4);
        assertEquals(3, shop.getGrid().length);
        assertEquals(4, shop.getGrid()[0].length);
        assertEquals(3, shop.getRowSize());
        assertEquals(4, shop.getColSize());
    }

    @Test
    public void addCard(){
        ClientShop shop  = new ClientShop(3, 4);

        shop.changeCard(2, 0, craftingCards.get(2));
        assertEquals(craftingCards.get(2), shop.getGrid()[2][0]);
    }

    @Test
    public void removeCard(){
        ClientShop shop = new ClientShop(3, 4);

        shop.changeCard(2, 0, craftingCards.get(2));
        shop.removeCard(2, 0);
        assertNull(shop.getGrid()[2][0]);
    }

    @Test
    public void selectCard(){
        ClientShop shop = new ClientShop(3, 4);

        shop.changeCard(2, 0, craftingCards.get(2));
        shop.selectCard(2, 0);

        assertEquals(craftingCards.get(2), shop.getGrid()[shop.getSelectedCardRow()][shop.getSelectedCardCol()]);
    }

    @Test
    public void unselect(){
        ClientShop shop = new ClientShop(3, 4);

        shop.changeCard(2, 0, craftingCards.get(2));
        shop.selectCard(2, 0);
        shop.unselect();

        assertNull(shop.getSelectedCardCol());
        assertNull(shop.getSelectedCardRow());
    }
}
