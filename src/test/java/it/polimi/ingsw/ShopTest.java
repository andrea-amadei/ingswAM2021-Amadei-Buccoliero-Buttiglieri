package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.Shop;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class ShopTest {
    @Test
    public void createEmptyShop(){
        Shop s = new Shop();
        assertThrows(NoSuchElementException.class, ()->s.getTopCard(1, FlagColor.BLUE));
    }

    @Test
    public void addCardLastRowLastCol(){
        Shop s = new Shop();
        Map<ResourceSingle, Integer> cardCost = new HashMap<>() {{
            put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        }};
        Map<ResourceType, Integer> craftingParams = new HashMap<>() {{
            put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        }};
        UpgradableCrafting upgradableCrafting = new UpgradableCrafting(craftingParams, craftingParams, 0, 3);
        CraftingCard card = new CraftingCard(1, new LevelFlag(FlagColor.YELLOW, 3), cardCost, upgradableCrafting, 12);

        s.addCard(card);
        assertEquals(card, s.getTopCard(3, FlagColor.YELLOW));
    }

    @Test
    public void addCardFirstRowFirstCol(){
        Shop s = new Shop();
        Map<ResourceSingle, Integer> cardCost = new HashMap<>() {{
            put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        }};
        Map<ResourceType, Integer> craftingParams = new HashMap<>() {{
            put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        }};
        UpgradableCrafting upgradableCrafting = new UpgradableCrafting(craftingParams, craftingParams, 0, 1);
        CraftingCard card = new CraftingCard(1, new LevelFlag(FlagColor.BLUE, 1), cardCost, upgradableCrafting, 12);

        s.addCard(card);
        assertEquals(card, s.getTopCard(1, FlagColor.BLUE));
    }

    @Test
    public void addCardSecondRowFirstCol(){
        Shop s = new Shop();
        Map<ResourceSingle, Integer> cardCost = new HashMap<>() {{
            put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        }};
        Map<ResourceType, Integer> craftingParams = new HashMap<>() {{
            put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        }};
        UpgradableCrafting upgradableCrafting = new UpgradableCrafting(craftingParams, craftingParams, 0, 2);
        CraftingCard card = new CraftingCard(1, new LevelFlag(FlagColor.BLUE, 2), cardCost, upgradableCrafting, 12);

        s.addCard(card);
        assertEquals(card, s.getTopCard(2, FlagColor.BLUE));
    }

    @Test
    public void getCardToInvalidPosition(){
        Shop s = new Shop();
        assertThrows(IllegalArgumentException.class, ()->s.getTopCard(4, FlagColor.BLUE));
        assertThrows(IllegalArgumentException.class, ()->s.getTopCard(0, FlagColor.BLUE));
        assertThrows(IllegalArgumentException.class, ()->s.getTopCard(-1, FlagColor.BLUE));
    }

    @Test
    public void addNullCard(){
        Shop s = new Shop();
        assertThrows(NullPointerException.class, ()->s.addCard(null));
    }


    @Test
    public void removeCardSecondRowFirstCol(){
        Shop s = new Shop();
        Map<ResourceSingle, Integer> cardCost = new HashMap<>() {{
            put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        }};
        Map<ResourceType, Integer> craftingParams = new HashMap<>() {{
            put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        }};
        UpgradableCrafting upgradableCrafting = new UpgradableCrafting(craftingParams, craftingParams, 0, 2);
        CraftingCard card = new CraftingCard(1, new LevelFlag(FlagColor.BLUE, 2), cardCost, upgradableCrafting, 12);

        s.addCard(card);
        assertEquals(card, s.removeCard(2, FlagColor.BLUE));
        assertThrows(NoSuchElementException.class, ()->s.getTopCard(2, FlagColor.BLUE));
    }

    @Test
    public void removeInvalidCard(){
        Shop s = new Shop();
        assertThrows(IllegalArgumentException.class, ()->s.removeCard(-1, FlagColor.GREEN));
        assertThrows(IllegalArgumentException.class, ()->s.removeCard(4, FlagColor.GREEN));
        assertThrows(NullPointerException.class, ()->s.removeCard(2, null));
    }

    @Test
    public void selectCard(){
        Shop s = new Shop();
        Map<ResourceSingle, Integer> cardCost = new HashMap<>() {{
            put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        }};
        Map<ResourceType, Integer> craftingParams = new HashMap<>() {{
            put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        }};
        UpgradableCrafting upgradableCrafting = new UpgradableCrafting(craftingParams, craftingParams, 0, 2);
        CraftingCard card = new CraftingCard(1, new LevelFlag(FlagColor.BLUE, 2), cardCost, upgradableCrafting, 12);

        s.addCard(card);
        s.selectCard(1, 0);
        assertEquals(s.getTopCard(1, 0), s.getSelectedCard());
    }

}
