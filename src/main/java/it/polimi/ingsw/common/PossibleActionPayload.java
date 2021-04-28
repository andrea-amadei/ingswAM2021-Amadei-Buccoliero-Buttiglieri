package it.polimi.ingsw.common;

public class PossibleActionPayload extends PayloadComponent {
    /**
     * Creates a new possible action payload
     *
     * @param type the type of this component (activate_leader, back, ...)
     * @throws NullPointerException if any attribute is null
     */
    public PossibleActionPayload(String type) {
        super("possible_action", type);
    }
}
