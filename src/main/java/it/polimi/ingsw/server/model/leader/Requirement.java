package it.polimi.ingsw.server.model.leader;

import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.common.parser.raw.RawRequirement;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.common.parser.SerializableObject;

/**
 * Interface Requirement is implemented by the specific types of requirements (flag,
 * flag with level, resource)
 * provides isSatisfied method, to verify that requirements are satisfied
 */
public interface Requirement extends SerializableObject<RawRequirement> {

    /**
     * isSatisfied verifies if the selected players satisfies all requirements to activate
     * the leader card
     * @param player the player who is being verified
     */
    boolean isSatisfied(Player player);

    @Override
    default void printDebugInfo() {
        Logger.log(toString());
    }
}
