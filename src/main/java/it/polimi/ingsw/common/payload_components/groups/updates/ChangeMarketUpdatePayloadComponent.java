package it.polimi.ingsw.common.payload_components.groups.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.annotations.SerializedType;
import it.polimi.ingsw.common.payload_components.groups.UpdatePayloadComponent;
import it.polimi.ingsw.parser.raw.RawMarket;

@SerializedType("change_market")
public class ChangeMarketUpdatePayloadComponent implements UpdatePayloadComponent {

    @SerializedName("market")
    private RawMarket market;

    /**
     * This constructor shall only be used to instance payloads through reflection
     */
    public ChangeMarketUpdatePayloadComponent() { }

    public ChangeMarketUpdatePayloadComponent(RawMarket market) {
        if(market == null)
            throw new NullPointerException();

        this.market = market;
    }

    public RawMarket getMarket() {
        return market;
    }
}
