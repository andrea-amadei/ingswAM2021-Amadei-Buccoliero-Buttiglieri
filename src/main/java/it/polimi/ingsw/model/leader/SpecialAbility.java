package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.Console;
import it.polimi.ingsw.server.parser.SerializedObject;

public interface SpecialAbility extends SerializedObject {

    void activate(Player player);

    @Override
    default void printDebugInfo() {
        Console.log(toString());
    }
}
