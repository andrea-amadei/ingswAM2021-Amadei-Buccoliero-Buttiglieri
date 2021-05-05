package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.parser.raw.RawSpecialAbility;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.parser.SerializableObject;

import java.util.List;

public interface SpecialAbility extends SerializableObject<RawSpecialAbility> {

    List<PayloadComponent> activate(Player player);

    @Override
    default void printDebugInfo() {
        Logger.log(toString());
    }
}
