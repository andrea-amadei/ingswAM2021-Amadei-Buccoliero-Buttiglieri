package it.polimi.ingsw.common;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;

public class ErrorPayload extends PayloadComponent {

    @SerializedName("message")
    private final String message;

    /**
     * Creates a new error payload
     *
     * @param type the type of this component (invalid_resource_error, illegal_action_error, ...)
     * @param message the attached message of the error
     * @throws NullPointerException if any attribute is null
     */
    public ErrorPayload(String type, String message) {
        super("error", type);

        if(message == null)
            throw new NullPointerException();

        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
