package it.polimi.ingsw.model;

import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.model.production.CraftingCard;

import java.util.*;

/**
 * The shop of the game. It contains a grid of deck of cards grouped by color and level.
 * Cards are inserted when the game stars.
 * The first card of the deck is the only one accessible
 */
public class Shop {
    private final List<Deque<CraftingCard>> grid;
    private final int levelAxisSize;
    private CraftingCard selectedCard;


    /**
     * Creates a new empty shop. Cards will be inserted during the setup of the game
     */
    public Shop(){
        grid = new ArrayList<>();
        levelAxisSize = GameParameters.MAX_CARD_LEVEL;
        int colorAxisSize = FlagColor.values().length;

        for(int i = 0; i < levelAxisSize; i++){
            for(int j = 0; j < colorAxisSize; j++){
                grid.add(new ArrayDeque<>());
            }
        }

        selectedCard = null;
    }

    /**
     * Adds a card to the top of the appropriate deck
     * @param card the card to be inserted
     * @throws NullPointerException if the card is null
     */
    public void addCard(CraftingCard card){
        if(card == null)
            throw new NullPointerException();

        int cardLevel = card.getFlag().getLevel();
        int cardColorIndex = card.getFlag().getColor().ordinal();
        grid.get((levelAxisSize - cardLevel) * FlagColor.values().length + cardColorIndex).addFirst(card);
    }

    /**
     * Retrieves, but does not remove, the first element of the specified deck.
     * If the deck is empty an exception is thrown
     * @param level the level of the desired card
     * @param color the color of the desired card
     * @return the desired card
     * @throws NullPointerException if color is null
     * @throws IllegalArgumentException if level is out of bound
     * @throws NoSuchElementException if the indicated deck is empty
     */
    public CraftingCard getTopCard(int level, FlagColor color){
        if(level < 1 || level > levelAxisSize)
            throw new IllegalArgumentException("Level must be between 1 and " + levelAxisSize);
        return grid.get((levelAxisSize - level)* FlagColor.values().length + color.ordinal()).getFirst();
    }

    /**
     * Retrieves, but does not remove, the first element of the specified deck.
     * If the deck is empty an exception is thrown
     * @param row row index (0-based down->up)
     * @param col col index (0-based left->write)
     * @return the desired card
     * @throws IllegalArgumentException if indexes are out of bound
     * @throws NoSuchElementException if the indicated deck is empty
     */
    public CraftingCard getTopCard(int row, int col){
        if(row < 0 || row >= levelAxisSize || col < 0 || col >= FlagColor.values().length)
            throw new IllegalArgumentException("Row or col are not valid");

        return getTopCard(row+1, FlagColor.values()[col]);
    }

    /**
     * Selects the specified card. It is used to allow the player to select a card and then (later) to select
     * the resources to buy it
     * @param row row index (0-based down->up)
     * @param col col index(0-based left->right)
     * @return the desired card
     * @throws IllegalArgumentException indexes are not allowed
     * @throws NoSuchElementException if the indicated deck is empty
     */
    public CraftingCard selectCard(int row, int col){
        if(row < 0 || row >= levelAxisSize || col < 0 || col >= FlagColor.values().length)
            throw new IllegalArgumentException("Row or col are not valid");
        CraftingCard sCard = getTopCard(row, col);
        selectedCard = sCard;
        return sCard;
    }

    /**
     * Returns the selected card. Null if there is no selected card
     * @return the selected card. Null if there is no selected card
     */
    public CraftingCard getSelectedCard() {
        return selectedCard;
    }

    /**
     * Retrieves and removes the first element of the specified deck. If the deck is empty an exception is thrown
     * @param level the level of the desired card
     * @param color the color of the desired card
     * @return the desired card
     * @throws NullPointerException if color is null
     * @throws IllegalArgumentException if level is out of bound
     * @throws NoSuchElementException if the indicated deck is empty
     */
    public CraftingCard removeCard(int level, FlagColor color){
        if(level < 1 || level > levelAxisSize)
            throw new IllegalArgumentException("Level must be between 1 and " + levelAxisSize);
        return grid.get((levelAxisSize - level)*FlagColor.values().length + color.ordinal()).removeFirst();
    }


}
