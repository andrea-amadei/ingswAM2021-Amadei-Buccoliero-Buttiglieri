package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.nodes.ShopBox;
import it.polimi.ingsw.client.model.ClientShop;
import it.polimi.ingsw.client.observables.Listener;
import it.polimi.ingsw.parser.raw.RawCraftingCard;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopGuiUpdater implements Listener<ClientShop> {

    private final ShopBox shopBox;

    public ShopGuiUpdater(ShopBox shopBox, ClientShop clientShop){
        this.shopBox = shopBox;
        this.shopBox.setColNum(clientShop.getColSize());
        this.shopBox.setRowNum(clientShop.getRowSize());
        update(clientShop);
        clientShop.addListener(this);
    }

    @Override
    public void update(ClientShop clientShop) {
        Platform.runLater(()-> shopBox.setCraftingCards(fromGridToList(clientShop.getGrid())));
    }

    private List<RawCraftingCard> fromGridToList(RawCraftingCard[][] rawCraftingCards){
        List<RawCraftingCard> craftingCardsAsList = new ArrayList<>();
        for (RawCraftingCard[] rawCraftingCard : rawCraftingCards) {
            craftingCardsAsList.addAll(Arrays.asList(rawCraftingCard));
        }

        return craftingCardsAsList;
    }
}
