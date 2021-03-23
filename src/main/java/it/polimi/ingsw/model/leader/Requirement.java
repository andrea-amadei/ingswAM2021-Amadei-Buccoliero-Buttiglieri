package it.polimi.ingsw.model.leader;

import it.polimi.ingsw.model.Player;

/**
 * Interface Requirement is implemented by the specific types of requirements (flag,
 * flag with level, resource)
 * provides isSatisfied method, to verify that requirements are satisfied
 */
public interface Requirement {

    /**
     * isSatisfied verifies if the selected players satisfies all requirements to activate
     * the leader card
     * @param player the player who is being verified
     */
    boolean isSatisfied(Player player);

}
