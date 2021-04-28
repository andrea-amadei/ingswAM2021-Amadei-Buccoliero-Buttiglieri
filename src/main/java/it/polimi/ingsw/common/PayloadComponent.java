package it.polimi.ingsw.common;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;

/**
 * A component of a message
 */
public abstract class PayloadComponent {

    @SerializedName("group")
    private final String group;

    @SerializedName("type")
    private final String type;

    /**
     * Creates a new component of the specified group and type
     * @param group the group of this component (update, possible_action, ...)
     * @param type the type of this component (update_shelf, update_hand, ...)
     * @throws NullPointerException if either group or type is null
     */
    public PayloadComponent(String group, String type){
        if(group == null || type == null)
            throw new NullPointerException();
        this.group = group;
        this.type = type;
    }
}
