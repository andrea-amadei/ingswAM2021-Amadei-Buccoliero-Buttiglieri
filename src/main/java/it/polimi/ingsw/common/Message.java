package it.polimi.ingsw.common;

import java.util.ArrayList;
import java.util.List;

/**
 * A message sent by the server. It contains a list of targets (the clients that will receive the message).
 * It is an immutable class
 */
public class Message {
    private final List<String> targets;
    private final List<PayloadComponent> payloadComponents;

    /**
     * Creates a new message
     * @param targets the targets of this message (the clients that will receive the message)
     * @param payloadComponents the content of the message
     * @throws NullPointerException if either targets or payloadComponents is null
     */
    public Message(List<String> targets, List<PayloadComponent> payloadComponents){
        if(targets == null || payloadComponents == null)
            throw new NullPointerException();
        this.targets = targets;
        this.payloadComponents = payloadComponents;
    }

    /**
     * Returns the list of all targets of this message
     * @return the list of all targets of this message
     */
    public List<String> getTargets() {
        return new ArrayList<>(targets);
    }

    /**
     * Returns the payload of this message
     * @return the payload of this message
     */
    public List<PayloadComponent> getPayloadComponents() {
        return new ArrayList<>(payloadComponents);
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String newLine = "";
        for(PayloadComponent p : getPayloadComponents()){
            sb.append(newLine).append(p.toString());
            newLine = "\n";
        }
        return sb.toString();
    }
}
