package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.server.model.basetypes.MarbleColor;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.market.ConversionActuator;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.MarbleFactory;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.common.utils.PayloadFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangePossibleConversionsUpdatePayloadComponentTest {

    private List<Marble> marbles;
    private List<List<ConversionActuator>> possibleConversions;

    @BeforeEach
    public void init(){
        possibleConversions = new ArrayList<>();
        List<ResourceSingle> output = new ArrayList<>();
        marbles = new ArrayList<>();
        List<ConversionActuator> conversions = new ArrayList<>();
        output.add(ResourceTypeSingleton.getInstance().getGoldResource());
        marbles.add(MarbleFactory.createMarble(MarbleColor.PURPLE));
        marbles.add(MarbleFactory.createMarble(MarbleColor.GREY));
        conversions.add(new ConversionActuator(output,0));
        possibleConversions.add(conversions);
    }

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.changePossibleConversions("Ernestino", marbles, possibleConversions);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"change_possible_conversions\",\"group\":\"update\",\"selected_marbles\"" +
                ":[{\"color\":\"PURPLE\"},{\"color\":\"GREY\"}],\"possible_conversions\":[[{\"resourceOutput\":" +
                "[\"gold\"],\"faithOutput\":0}]],\"player\":\"Ernestino\"}", serialized);
    }

    /*
    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"change_possible_conversions\",\"group\":\"update\",\"selected_marbles\"" +
                ":[{\"color\":\"PURPLE\"},{\"color\":\"GREY\"}],\"possible_conversions\":[[{\"resourceOutput\":" +
                "[\"gold\"],\"faithOutput\":0}]],\"player\":\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = ClientDeserializer.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof ChangePossibleConversionsUpdate);

        ChangePossibleConversionsUpdate update = ((ChangePossibleConversionsUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals("Ernestino", update.getPlayer());
        assertEquals(marbles, update.getSelectedMarbles());
    }

     */

}
