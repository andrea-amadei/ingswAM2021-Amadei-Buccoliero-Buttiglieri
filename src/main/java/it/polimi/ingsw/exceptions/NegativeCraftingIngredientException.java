package it.polimi.ingsw.exceptions;

public class NegativeCraftingIngredientException extends RuntimeException {
    public NegativeCraftingIngredientException(String message) {
        super(message);
    }
}
