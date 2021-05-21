package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.annotations.SerializedGroup;
import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.UpdatePayloadComponent;

import java.util.List;

@SerializedGroup("update")
@SerializedType("set_initial_configuration")
public class SetInitialConfigurationUpdatePayloadComponent implements UpdatePayloadComponent {

    @SerializedName(value = "config_json", alternate = "configJson")
    private final String configJson;

    @SerializedName(value = "crafting_json", alternate = "craftingJson")
    private final String craftingJson;

    @SerializedName(value = "faith_json", alternate = "faithJson")
    private final String faithJson;

    @SerializedName(value = "leaders_json", alternate = "leadersJson")
    private final String leadersJson;

    private final List<String> usernames;


    public SetInitialConfigurationUpdatePayloadComponent(String configJson, String craftingJson, String faithJson, String leadersJson, List<String> usernames) {
        this.configJson = configJson;
        this.craftingJson = craftingJson;
        this.faithJson = faithJson;
        this.leadersJson = leadersJson;
        this.usernames = usernames;
    }

    public String getConfigJson() {
        return configJson;
    }

    public String getCraftingJson() {
        return craftingJson;
    }

    public String getFaithJson() {
        return faithJson;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public String getLeadersJson() {
        return leadersJson;
    }
}
