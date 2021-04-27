package it.polimi.ingsw.common;

import it.polimi.ingsw.exceptions.IllegalRawConversionException;

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

    @Override
    public PayloadComponent toPayloadComponent() throws IllegalRawConversionException {
        return null;
    }
}
