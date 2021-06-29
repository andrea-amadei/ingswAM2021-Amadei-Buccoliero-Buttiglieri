package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.common.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.SpecificUpdatePayloadComponent;

@SerializedType("add_shelf")
public class AddShelfUpdatePayloadComponent extends SpecificUpdatePayloadComponent {

    @SerializedName("id")
    private String id;

    @SerializedName(value = "resource", alternate = {"resource_type", "resourceType"})
    private String resource;

    @SerializedName("size")
    private int size;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public AddShelfUpdatePayloadComponent() { }

    public AddShelfUpdatePayloadComponent(String player, String id, String resource, int size) {
        super(player);

        if(id == null || resource == null)
            throw new NullPointerException();

        this.id = id;
        this.resource = resource;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public String getResource() {
        return resource;
    }

    public int getSize() {
        return size;
    }
}
