package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.parser.raw.RawSpecialAbility;
import it.polimi.ingsw.server.Console;
import it.polimi.ingsw.parser.SerializableObject;

public interface SpecialAbility extends SerializableObject<RawSpecialAbility> {

    void activate(Player player);

    @Override
    default void printDebugInfo() {
        Console.log(toString());
    }
}
