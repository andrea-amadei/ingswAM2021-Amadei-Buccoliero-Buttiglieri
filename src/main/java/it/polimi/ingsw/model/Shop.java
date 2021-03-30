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
        grid.get((cardLevel-1) * levelAxisSize + cardColorIndex).addFirst(card);
    }

    /**
     * Retrieves, but does not remove, the first element of the specified deck. If the deck is empty an exception is thrown
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
        return grid.get((level-1)*levelAxisSize + color.ordinal()).getFirst();
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
        return grid.get((level-1)*levelAxisSize + color.ordinal()).removeFirst();
    }


}
