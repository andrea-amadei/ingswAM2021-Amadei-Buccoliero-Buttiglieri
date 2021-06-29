package it.polimi.ingsw.common.exceptions;

public class NegativeCraftingIngredientException extends RuntimeException {
    public NegativeCraftingIngredientException(String message) {
        super(message);
    }
}
