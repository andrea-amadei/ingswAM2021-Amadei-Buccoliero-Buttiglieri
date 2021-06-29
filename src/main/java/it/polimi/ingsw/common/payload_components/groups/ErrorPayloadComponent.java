package it.polimi.ingsw.common.payload_components.groups;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedGroup;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.PayloadComponent;

@SerializedGroup("update")
@SerializedType("generic_error")
public class ErrorPayloadComponent implements PayloadComponent {

    @SerializedName("message")
    private String message;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public ErrorPayloadComponent() { }

    /**
     * Creates a new error payload
     *
     * @param message the attached message of the error
     * @throws NullPointerException if any attribute is null
     */
    public ErrorPayloadComponent(String message) {
        if(message == null)
            throw new NullPointerException();

        this.message = message;
    }

    @Override
    public boolean isNecessary(){
        return false;
    }

    public String getMessage() {
        return message;
    }
}
