package it.polimi.ingsw.common;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;

import java.util.Map;

public class GlobalUpdatePayload extends PayloadComponent {

    @SerializedName("content")
    private final Map<String, Object> content;

    /**
     * Creates a new update payload
     *
     * @param type the type of this component (update_shelf, update_hand, ...)
     * @param content a map containing all changes of the specified type
     * @throws NullPointerException if any attribute is null
     */
    public GlobalUpdatePayload(String type, Map<String, Object> content) {
        super("update", type);

        if(content == null)
            throw new NullPointerException();

        if(content.size() == 0)
            throw new IllegalArgumentException("Content size cannot be zero");

        this.content = content;
    }

    public Map<String, Object> getContent() {
        return content;
    }
}
