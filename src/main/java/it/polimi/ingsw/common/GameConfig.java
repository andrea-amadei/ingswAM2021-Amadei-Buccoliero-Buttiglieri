package it.polimi.ingsw.common;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.server.model.basetypes.MarbleColor;
import it.polimi.ingsw.server.model.basetypes.ResourceType;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class GameConfig {

    @SerializedName("max_username_length")
    private Integer maxUsernameLength;

    @SerializedName("min_username_length")
    private Integer minUsernameLength;


    @SerializedName("max_card_level")
    private Integer maxCardLevel;

    @SerializedName("min_card_level")
    private Integer minCardLevel;

    @SerializedName("amount_of_leaders_per_player")
    private Integer amountOfLeaderPerPlayer;

    @SerializedName("amount_of_leaders_to_discard")
    private Integer amountOfLeadersToDiscard;

    @SerializedName("first_player_amount_of_faith_points_on_start")
    private Integer firstPlayerAmountOfFaithPointsOnStart;

    @SerializedName("second_player_amount_of_faith_points_on_start")
    private Integer secondPlayerAmountOfFaithPointsOnStart;

    @SerializedName("third_player_amount_of_faith_points_on_start")
    private Integer thirdPlayerAmountOfFaithPointsOnStart;

    @SerializedName("fourth_player_amount_of_faith_points_on_start")
    private Integer fourthPlayerAmountOfFaithPointsOnStart;


    @SerializedName("first_player_amount_of_resources_on_start")
    private Integer firstPlayerAmountOfResourcesOnStart;

    @SerializedName("second_player_amount_of_resources_on_start")
    private Integer secondPlayerAmountOfResourcesOnStart;

    @SerializedName("third_player_amount_of_resources_on_start")
    private Integer thirdPlayerAmountOfResourcesOnStart;

    @SerializedName("fourth_player_amount_of_resources_on_start")
    private Integer fourthPlayerAmountOfResourcesOnStart;


    @SerializedName("base_cupboard_shelf_names")
    private List<String> baseCupboardShelfNames;

    @SerializedName("base_cupboard_shelf_types")
    private List<ResourceType> baseCupboardShelfTypes;

    @SerializedName("base_cupboard_shelf_sizes")
    private List<Integer> baseCupboardShelfSizes;


    @SerializedName("hand_id")
    private String handId;

    @SerializedName("basket_id")
    private String basketId;

    @SerializedName("chest_id")
    private String chestId;

    @SerializedName("market_rows")
    private Integer marketRows;

    @SerializedName("market_columns")
    private Integer marketColumns;


    @SerializedName("marble_per_color")
    private Map<MarbleColor, Integer> marblePerColor;


    @SerializedName("upgradable_crafting_number")
    private Integer upgradableCraftingNumber;

    @SerializedName("faith_checkpoint_number")
    private Integer faithCheckpointNumber;


    public Integer getMaxUsernameLength() {
        return maxUsernameLength;
    }

    public Integer getMinUsernameLength() {
        return minUsernameLength;
    }

    public Integer getMaxCardLevel() {
        return maxCardLevel;
    }

    public Integer getMinCardLevel() {
        return minCardLevel;
    }

    public Integer getAmountOfLeaderPerPlayer() {
        return amountOfLeaderPerPlayer;
    }

    public Integer getAmountOfLeadersToDiscard() {
        return amountOfLeadersToDiscard;
    }

    public Integer getFirstPlayerAmountOfFaithPointsOnStart() {
        return firstPlayerAmountOfFaithPointsOnStart;
    }

    public Integer getSecondPlayerAmountOfFaithPointsOnStart() {
        return secondPlayerAmountOfFaithPointsOnStart;
    }

    public Integer getThirdPlayerAmountOfFaithPointsOnStart() {
        return thirdPlayerAmountOfFaithPointsOnStart;
    }

    public Integer getFourthPlayerAmountOfFaithPointsOnStart() {
        return fourthPlayerAmountOfFaithPointsOnStart;
    }

    public Integer getFirstPlayerAmountOfResourcesOnStart() {
        return firstPlayerAmountOfResourcesOnStart;
    }

    public Integer getSecondPlayerAmountOfResourcesOnStart() {
        return secondPlayerAmountOfResourcesOnStart;
    }

    public Integer getThirdPlayerAmountOfResourcesOnStart() {
        return thirdPlayerAmountOfResourcesOnStart;
    }

    public Integer getFourthPlayerAmountOfResourcesOnStart() {
        return fourthPlayerAmountOfResourcesOnStart;
    }

    public List<String> getBaseCupboardShelfNames() {
        return baseCupboardShelfNames;
    }

    public List<ResourceType> getBaseCupboardShelfTypes() {
        return baseCupboardShelfTypes;
    }

    public List<Integer> getBaseCupboardShelfSizes() {
        return baseCupboardShelfSizes;
    }

    public String getHandId() {
        return handId;
    }

    public String getBasketId() {
        return basketId;
    }

    public String getChestId() {
        return chestId;
    }

    public Integer getMarketRows() {
        return marketRows;
    }

    public Integer getMarketColumns() {
        return marketColumns;
    }

    public Map<MarbleColor, Integer> getMarblePerColor() {
        return marblePerColor;
    }

    public Integer getUpgradableCraftingNumber() {
        return upgradableCraftingNumber;
    }

    public Integer getFaithCheckpointNumber() {
        return faithCheckpointNumber;
    }
}
